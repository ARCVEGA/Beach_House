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

    /*If not coupled, find a potential matt, couple and move towards him*/
    if (coupledMatt == null && !potentialMatts.isEmpty()) {
      Matt mostAttractiveMatt = getMostAttractiveMatt(potentialMatts);
      Double2D mattVector = getVectorToAgent(sim, mostAttractiveMatt);

      // TODO: Need to address case when Matt is taken and no coupling happens
      evalAndCouple(sim, mostAttractiveMatt, mattVector);
      walkTowards(sim, mattVector);

    } else if (coupledMatt != null) {
      if (sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(coupledMatt))
          > minCouplingDistance) {
        walkTowards(sim, getVectorToAgent(sim, coupledMatt));
      }
    } else {
      randomWalk((Simulation) simState);
    }

  }

  /**
   * Find most desirable Matt within Casey's proximity
   *
   * @param potentialMatts Bag of all potential Matts to pick from
   * @return Most Attractive Matt
   */
  private Matt getMostAttractiveMatt(Bag potentialMatts) {

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

    return mostAttractiveMatt;
  }


  /**
   * Filters all Matt agents which are uncoupled and within {@link Casey#thresholdDistance}
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
            < thresholdDistance)
        .filter(obj -> !((Matt) obj).isCoupled())
        .collect(Collectors.toList()));

    return potentialMatts;
  }

  /**
   * Evaluates if a Matt is ready to be coupled with, if so then the couple is formed otherwise
   * nothing happens and Casey remains unpaired
   *
   * @param potentialPartner {@link Matt} which has potential to be partnered
   */
  private void evalAndCouple(Simulation sim, Matt potentialPartner, Double2D vectorToMatt) {
    if (!isCoupled() && vectorToMatt.distance(sim.space.getObjectLocation(potentialPartner))
        <= minCouplingDistance) {
      setCoupledMatt(potentialPartner);
      coupledMatt.setCoupledCasey(this);
    }
  }

  public void setCoupledMatt(Matt matt) {
    this.coupledMatt = matt;
  }

  public boolean isCoupled() {
    return coupledMatt != null;
  }
}
