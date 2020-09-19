package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import com.arcvega.simulation.config.SimConfig;
import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Double2D;

import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Casey extends Agent {

  private Matt coupledMatt = null;
  /**
   * the affinity describes the how attractive this Casey is to the Matt agent from 0 to 100
   */
  private final int mattAffinity;


  public Casey(SimState simState) {
    this.mattAffinity = simState.random.nextInt(100);
  }

  @Override
  public void step(SimState simState) {
    Simulation sim = (Simulation) simState;
    randomWalk(sim); // Only does random walk for now
  }


  /**
   * Defines behaviour of how Casey walks if she is not currently coupled with a Matt but is
   * actively looking for a potential partner
   *
   * @param sim Simulation containing agents
   */
  private void uncoupledWalk(Simulation sim) {
    Bag potentialMatts = getMattsNearby(sim);

    if (potentialMatts.isEmpty()) {
      randomWalk(sim);
      return;
    }

    Matt mostAttractiveMatt = getMostAttractiveMatt(potentialMatts);
    Double2D mattVector = getVectorToAgent(sim, mostAttractiveMatt);

    walkTowards(sim, mattVector);
    evalAndCouple(sim, mostAttractiveMatt, mattVector);
  }


  /**
   * Defines how Casey walks if she is coupled with a Matt at this time
   *
   * @param sim Simulation containing agents
   */
  private void coupledWalk(Simulation sim) {
    if (sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(coupledMatt))
        > SimConfig.CASEY_MINIMUM_COUPLING_DISTANCE) {
      walkTowards(sim, getVectorToAgent(sim, coupledMatt));
    } else {
      randomWalk(sim);
    }
  }

  /**
   * Find most desirable Matt within Casey's proximity
   *
   * @param potentialMatts Bag of all potential Matts to pick from
   * @return Most Attractive Matt
   */
  private Matt getMostAttractiveMatt(Bag potentialMatts) {

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

    return mostAttractiveMatt;
  }


  /**
   * Filters all Matt agents which are uncoupled and within predefined threshold distance of {@link
   * SimConfig#CASEY_MINIMUM_COUPLING_DISTANCE}
   *
   * @param sim Simulation where entities exist
   * @return Bag of filtered Matt agens
   */
  private Bag getMattsNearby(Simulation sim) {
    Bag potentialMatts = new Bag();
    Bag neighbours = sim.space.getAllObjects();

    Stream<Object> stream = neighbours.stream();
    potentialMatts.addAll(stream
        .filter(obj -> obj instanceof Matt)
        .filter(obj -> sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(obj))
            < SimConfig.CASEY_THRESHOLD_DISTANCE)
        .filter(obj -> !((Matt) obj).isCoupled())
        .collect(Collectors.toList()));

    return potentialMatts;
  }

  /**
   * Evaluates if a Matt is ready to be coupled, if so then the couple is formed otherwise nothing
   * happens and Casey remains unpaired
   *
   * @param potentialPartner {@link Matt} which has potential to be partnered
   */
  private void evalAndCouple(Simulation sim, Matt potentialPartner, Double2D vectorToMatt) {
    if (!isCoupled() && vectorToMatt.distance(sim.space.getObjectLocation(potentialPartner))
        <= SimConfig.CASEY_MINIMUM_COUPLING_DISTANCE) {
      setCoupledMatt(potentialPartner);
      coupledMatt.setCoupledCasey(this);
    }
  }

  public Matt getCoupledMatt() {
    return this.coupledMatt;
  }

  public void setCoupledMatt(Matt matt) {
    this.coupledMatt = matt;
  }

  public boolean isCoupled() {
    return coupledMatt != null;
  }

  public int getMattAffinity() {
    return mattAffinity;
  }
}
