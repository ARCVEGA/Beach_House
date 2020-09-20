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
  /**
   * Instance of coupled Casey initially set to null
   */
  private Casey coupledCasey = null;

  public Matt(SimState simState) {
    caseyAffinity = simState.random.nextInt(100);
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
    Bag potentialCaseys = getCaseysNearby(sim);

    if (potentialCaseys.isEmpty()) {
      randomWalk(sim);
      return;
    }

    Casey mostAttraciveCasey = getMostAttractiveCasey(potentialCaseys);
    Double2D vecToCasey = getVectorToAgent(sim, mostAttraciveCasey);

    walkTowards(sim, vecToCasey);
    evalAndCouple(sim, mostAttraciveCasey, vecToCasey);
  }


  /**
   * Defines how Matt behaves if he is coupled to Casey, he will follow her and stay in a given
   * radius defined by {@link SimConfig#MATT_MINIMUM_COUPLING_DISTANCE}
   *
   * @param sim Simulation containing all agents
   */
  private void coupledWalk(Simulation sim) {
    if (sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(coupledCasey))
        > SimConfig.CASEY_MINIMUM_COUPLING_DISTANCE) {
      walkTowards(sim, getVectorToAgent(sim, coupledCasey));
    } else {
      randomWalk(sim);
    }
  }

  /**
   * Filters all Casey agents which are uncoupled and withing a range of {@link
   * SimConfig#MATT_THRESHOLD_DISTANCE}
   *
   * @param sim Simulation containing agents
   * @return Bag of potential Casey agents that Matt may want to couple with
   */
  private Bag getCaseysNearby(Simulation sim) {
    Bag potentialCaseys = new Bag();
    Bag neighbours = sim.space.getAllObjects();

    Stream<Object> stream = neighbours.stream();
    potentialCaseys.addAll(stream.filter(obj -> obj instanceof Casey)
        .filter(obj -> sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(obj))
            < SimConfig.MATT_THRESHOLD_DISTANCE)
        .filter(obj -> !((Casey) obj).isCoupled())
        .collect(Collectors.toList()));

    return potentialCaseys;
  }


  /**
   * Find most desirable Casey from {@param potentialCaseys}
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
      } else if (casey.getMattAffinity() > mostAttractiveCasey.getMattAffinity()) {
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
   * @param potentialPartner {@link Casey} which has potential to be partnered
   */
  private void evalAndCouple(Simulation sim, Casey potentialPartner, Double2D vectorToCasey) {
    if (!isCoupled() && vectorToCasey.distance(sim.space.getObjectLocation(potentialPartner))
        <= SimConfig.MATT_MINIMUM_COUPLING_DISTANCE) {
      setCoupledCasey(potentialPartner);
      this.coupledCasey.setCoupledMatt(this);
    }
  }

  public int getCaseyAffinity() {
    return caseyAffinity;
  }

  public void setCoupledCasey(Casey casey) {
    coupledCasey = casey;
  }

  public boolean isCoupled() {
    return coupledCasey != null;
  }
}
