package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: Create method that keeps Casey within proximity of coupled Matt
public class Casey extends Agent {

  private final double thresholdDistance = 10;
  private Matt coupledMatt = null;
  private final double minCouplingDistance = 2;

  @Override
  public void step(SimState simState) {
    Simulation sim = (Simulation) simState;
    Bag potentialMatts = getMattsNearby(sim);

    if (coupledMatt == null && !potentialMatts.isEmpty()) {
      walkTowards(sim, getVectorToMostAttractiveMatt(sim, potentialMatts));
    } else if (coupledMatt != null) {
      if (sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(coupledMatt))
          > minCouplingDistance) {
        walkTowards(sim, getVectorToAgent(sim, coupledMatt));
      }
    } else {
      randomWalk((Simulation)simState);
    }

  }

  /**
   * TODO: Refactor this to give back most attractive matt rather than vector Produces a vector
   * derived from the Casey's current location and the most attractive Matt A Matt is ignored if it
   * is coupled TODO: Get Casey to claim a Matt for herself
   *
   * @param sim            Simulation containing Agents
   * @param potentialMatts Bag of all potential Matts to pick from
   * @return Vector from Casey to Matt
   */
  private Double2D getVectorToMostAttractiveMatt(Simulation sim, Bag potentialMatts) {

    // TODO: Ensure that initial matt cant be paired if hes coupled already
    Matt mostAttractiveMatt = null;

    for (Object obj : potentialMatts) {
      Matt matt = (Matt) obj;

      if (mostAttractiveMatt == null) {
        mostAttractiveMatt = matt;
      } else if (matt.getCaseyAffinity() > mostAttractiveMatt
          .getCaseyAffinity()) {
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

    if (!isCoupled() && vectorTowardsMatt.distance(sim.space.getObjectLocation(mostAttractiveMatt))
        <= minCouplingDistance) {
      setCoupledMatt(mostAttractiveMatt);
      coupledMatt.setCoupledCasey(this);
    }

    return new Double2D(vectorTowardsMatt);
  }

  /**
   * @param sim
   * @return
   */
  private Bag getMattsNearby(Simulation sim) {
    Bag potentialMatts = new Bag();
    Bag neighbours = sim.space.getAllObjects();

    Stream<Object> stream = neighbours.stream();
    potentialMatts.addAll(stream
        .filter(obj -> obj instanceof Matt)
        .filter(obj -> sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(obj))
            < thresholdDistance)
        .filter(obj -> !((Matt) obj).isCoupled())
        .collect(Collectors.toList()));

    return potentialMatts;
  }

  public void setCoupledMatt(Matt matt) {
    this.coupledMatt = matt;
  }

  public boolean isCoupled() {
    return coupledMatt != null;
  }
}
