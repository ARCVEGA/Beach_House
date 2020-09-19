package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import com.arcvega.simulation.config.SimConfig;
import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Casey extends Agent {

  @Override
  public void step(SimState simState) {
    Simulation sim = (Simulation) simState;
    Bag potentialMatts = getMattsNearby(sim);

    if (potentialMatts.isEmpty()) {
      randomWalk((Simulation) simState);
    } else {
      walkTowards(sim, getVectorToMostAttractiveMatt(sim, potentialMatts));
    }
  }

  /**
   * Produces a vector derived from the Casey's current location and the most attractive Matt
   *
   * @param sim Simulation containing Agents
   * @param potentialMatts Bag of all potential Matts to pick from
   * @return Vector from Casey to Mat
   */
  private Double2D getVectorToMostAttractiveMatt(Simulation sim, Bag potentialMatts) {
    Matt mostAttractiveMatt = (Matt) potentialMatts.get(0);
    for (Object obj : potentialMatts) {
      Matt matt = (Matt) obj;

      if (matt.getCaseyAffinity() > mostAttractiveMatt.getCaseyAffinity()) {
        mostAttractiveMatt = matt;
      }
    }

    MutableDouble2D vectorTowardsMatt = new MutableDouble2D(
        sim.space.getObjectLocation(mostAttractiveMatt).getX() - sim.space.getObjectLocation(this)
            .getX(),
        sim.space.getObjectLocation(mostAttractiveMatt).getY() - sim.space.getObjectLocation(this)
            .getY());

    // Prevent teleportation by scaling to unit vector
    if (vectorTowardsMatt.length() != 0) {
      vectorTowardsMatt.resize(1);
    }

    vectorTowardsMatt.addIn(sim.space.getObjectLocation(this));
    return new Double2D(vectorTowardsMatt);
  }

  private Bag getMattsNearby(Simulation sim) {
    Bag potentialMatts = new Bag();
    Bag neighbours = sim.space.getAllObjects();

    Stream<Object> stream = neighbours.stream();
    potentialMatts.addAll(stream
        .filter(obj -> obj instanceof Matt)
        .filter(obj -> sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(obj))
            < SimConfig.CASEY_THRESHOLD_DISTANCE)
        .collect(Collectors.toList()));

    return potentialMatts;
  }
}
