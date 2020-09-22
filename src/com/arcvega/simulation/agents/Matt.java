package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.SimConfig;
import com.arcvega.simulation.config.Simulation;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Double2D;

public class Matt extends Agent {

  /**
   * Casey affinity is the quantified amount that a Casey likes a particular Matt
   */
  private final int caseyAffinity;


  public Matt(Simulation sim) {
    super(sim);
    caseyAffinity = sim.random.nextInt(100);
  }

  @Override
  public void step(SimState simState) {
    Simulation sim = (Simulation) simState;

    if (!isCoupled()) {
      uncoupledWalk(sim);
    } else {
      coupledWalk(sim);
    }
  }


  /**
   * Defines how Matt moves if he is currently not coupled to a Casey
   *
   * @param sim Simulation containing all agents
   */
  private void uncoupledWalk(Simulation sim) {
    // TODO: This could probably be a generic method in the Agent abstract class
    //  since its identical for Casey and Matt, just need to adjust getter for nearby agent
    Bag potentialCaseys = getPotentialCaseys(sim);

    if (potentialCaseys.isEmpty()) {
      randomWalk(sim);
      return;
    }

    Casey mostAttractiveCasey = getMostAttractiveCasey(potentialCaseys);
    Double2D vecToCasey = getVectorToAgent(sim, mostAttractiveCasey);

    // TODO: Only one of the agents should perform the coupling process, otherwise the same
    //  process happens twice and is a waste of computational resources
    walkTowards(sim, vecToCasey);
    evalAndCouple(sim, mostAttractiveCasey, vecToCasey);
  }


  /**
   * Defines how Matt behaves if he is coupled to Casey, he will follow her and stay in a given
   * radius defined by {@link SimConfig#MATT_MINIMUM_COUPLING_DISTANCE}
   *
   * @param sim Simulation containing all agents
   */
  private void coupledWalk(Simulation sim) {
    if (sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(coupledAgent))
        > SimConfig.CASEY_MINIMUM_COUPLING_DISTANCE) {
      if (!isPlayingCatch()) {
        walkTowards(sim, getVectorToAgent(sim, coupledAgent, SimConfig.FLIGHT_RESPONSE));
      }
    } else {
      randomWalk(sim, SimConfig.FLIGHT_RESPONSE);
    }
  }

  /**
   * Filters all Casey agents which are uncoupled and withing a range of {@link
   * SimConfig#MATT_THRESHOLD_DISTANCE}
   *
   * @param sim Simulation containing agents
   * @return Bag of potential Casey agents that Matt may want to couple with
   */
  private Bag getPotentialCaseys(Simulation sim) {
    Bag potentialCaseys = new Bag();
    Bag neighbours = sim.space.getAllObjects();

    Stream<Object> stream = neighbours.stream();
    potentialCaseys.addAll(stream.filter(obj -> obj instanceof Casey)
        .filter(obj -> sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(obj))
            < SimConfig.MATT_THRESHOLD_DISTANCE)
        .filter(obj -> !getAgentBlacklist().contains(obj))
        .filter(obj -> !((Casey) obj).isCoupled())
        .filter(obj -> ((Casey) obj).getAffinity() > this.getStandard())
        .collect(Collectors.toList()));

    return potentialCaseys;
  }


  /**
   * Find most desirable Casey from {@param potentialCaseys} which isn't blacklisted
   *
   * @param potentialCaseys Caseys within a distance of {@link SimConfig#MATT_THRESHOLD_DISTANCE}
   * @return Fittest Casey found in {@param potentialCaseys}
   */
  private Casey getMostAttractiveCasey(Bag potentialCaseys) {
    Casey mostAttractiveCasey = null;

    for (Object obj : potentialCaseys) {
      Casey casey = (Casey) obj;

      if (mostAttractiveCasey == null) {
        mostAttractiveCasey = casey;
      } else if (casey.getAffinity() > mostAttractiveCasey.getAffinity()) {
        mostAttractiveCasey = casey;
      }
    }

    return mostAttractiveCasey;
  }

  // TODO: This seems like it would be a good fit for a generic method
  //  just need to rename setter for couple and give every agent a consistent naming for the partner

  /**
   * Evaluates if a Casey is ready to be coupled, if so then the couple is formed otherwise nothing
   * happens and Casey remains unpaired
   *
   * @param sim              Simulation containing all agents
   * @param potentialPartner {@link Casey} which has potential to be partnered
   * @param vectorToCasey    Vector which Matt will use to follow Casey
   */
  private void evalAndCouple(Simulation sim, Casey potentialPartner, Double2D vectorToCasey) {
    if (!isCoupled() &&
        vectorToCasey.distance(sim.space.getObjectLocation(potentialPartner))
            <= SimConfig.MATT_MINIMUM_COUPLING_DISTANCE) {
      if (potentialPartner.isWillingToCouple(this)) {
        setCoupledAgent(potentialPartner);
        potentialPartner.setCoupledAgent(this);

        // Add visible edge
        // TODO: Info can be anything, maybe how much they want to stay together
        sim.getAgentNetwork().addEdge(this, potentialPartner, null);
      } else {
        setOnBlacklist(potentialPartner);
      }
    }
  }

  @Override
  public int getAffinity() {
    return caseyAffinity;
  }

  public Casey getCoupledCasey() {
    return (Casey) this.coupledAgent;
  }

}
