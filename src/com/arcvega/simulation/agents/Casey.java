/*******************************************************************************
 * Copyright [2020] [Philipp and Francisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import com.arcvega.simulation.config.SimConfig;
import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Double2D;

import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Casey extends Agent {


  public Casey(Simulation sim) {
    super(sim);
  }

  @Override
  public void step(SimState simState) {
    Simulation sim = (Simulation) simState;
    if (!isCoupled()) {
      randomWalk(sim); // Only does random walk for now
    } else {
      coupledWalk(sim);
    }
  }


  /**
   * // TODO: This method should either be refactored into something useful or deleted Defines
   * behaviour of how Casey walks if she is not currently coupled with a Matt but is actively
   * looking for a potential partner
   *
   * @param sim Simulation containing agents
   * @apiNote This method is currently not used, since {@link Matt} does the coupling
   */
  @Deprecated
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
   * When coupled, Casey and Matt will run until Jim catches up and asks Matt to play ball, if they
   * remain coupled they will continue random walking
   *
   * @param sim Simulation containing agents
   */
  private void coupledWalk(Simulation sim) {
    if (!this.coupledAgent.isPlayingCatch()) {
      randomWalk(sim, SimConfig.FLIGHT_RESPONSE);

      if (sim.random.nextInt(100) > 10) {
        Bag potentialMatts = getAgentsNearby(sim, Matt.class,
            SimConfig.CASEY_MINIMUM_COUPLING_DISTANCE);

        competeForAgent(getMostAttractiveMatt(potentialMatts));
      }
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
      } else if (matt.getAffinity() > mostAttractiveMatt
          .getAffinity()) {
        mostAttractiveMatt = matt;
      }
    }

    return mostAttractiveMatt;
  }


  /**
   * Filters all Matt agents which are uncoupled and within predefined threshold distance of {@link
   * SimConfig#CASEY_MINIMUM_COUPLING_DISTANCE}, only get Agents which are not blacklisted
   *
   * @param sim Simulation where entities exist
   * @return Bag of filtered Matt agens
   * @deprecated Method has been replaced by {@link Agent#getAgentsNearby(Simulation, Class,
   * double)}
   */
  @Deprecated
  private Bag getMattsNearby(Simulation sim) {
    Bag potentialMatts = new Bag();
    Bag neighbours = sim.space.getAllObjects();

    Stream<Object> stream = neighbours.stream();
    potentialMatts.addAll(stream
        .filter(obj -> obj instanceof Matt)
        .filter(obj -> sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(obj))
            < SimConfig.CASEY_THRESHOLD_DISTANCE)
        .filter(obj -> !getAgentBlacklist().contains(obj))
        .filter(obj -> !((Matt) obj).isCoupled())
        .collect(Collectors.toList()));

    return potentialMatts;
  }


  /**
   * Evaluates if a Matt is ready to be coupled, if so then the couple is formed otherwise nothing
   * happens and Casey remains unpaired
   *
   * @param sim              Simulation containing all agents
   * @param potentialPartner {@link Matt} which has potential to be partnered
   * @param vectorToMatt     Vector which moves Casey towards Matt
   * @apiNote This method is currently not used, since {@link Matt} does the coupling
   * @deprecated It is up to the Matt agent to search for a potential mate not Casey. Casey only
   * competes for superior agents using {@link Agent#competeForAgent(Agent)}
   */
  @Deprecated
  private void evalAndCouple(Simulation sim, Matt potentialPartner, Double2D vectorToMatt) {
    if (!isCoupled() && vectorToMatt.distance(sim.space.getObjectLocation(potentialPartner))
        <= SimConfig.CASEY_MINIMUM_COUPLING_DISTANCE &&
        this.getAffinity() > potentialPartner.getStandard()) {
      setCoupledAgent(potentialPartner);
      coupledAgent.setCoupledAgent(this);
    }
  }


  /**
   * @param potentialPartner Partner which is being asked if they want to couple
   * @return Response that Casey gives to Matt
   */
  public boolean isWillingToCouple(Matt potentialPartner) {
    if (this.isCoupled()) {
      return false;
    }

    if (potentialPartner.getAffinity() > this.getStandard()) {
      return true;
    }

    setOnBlacklist(potentialPartner);
    return false;
  }

}
