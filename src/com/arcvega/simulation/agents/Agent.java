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

import com.arcvega.simulation.config.SimConfig;
import com.arcvega.simulation.config.Simulation;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import sim.engine.Steppable;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public abstract class Agent implements Steppable {

  private Double2D previousModifier = null;
  private LinkedList<Agent> agentBlacklist = new LinkedList<>(); // This will cause type erasure
  final double affinity; /*The affinity describes how attractive this agent is as seen by others*/
  Agent coupledAgent = null;
  double standard;
  boolean playingCatch = false;

  Agent(Simulation sim) {
    standard = sim.random.nextGaussian();
    this.affinity = sim.random.nextGaussian();
  }

  /**
   * Function that provides a simple wrapper around moving to a location for readability.
   */
  void walkTowards(Simulation sim, Double2D dest) {
    sim.space.setObjectLocation(this, dest);
  }

  /**
   * Function that just moves agent to random walk.
   *
   * @param sim Simulation containing all agents
   */
  void randomWalk(Simulation sim) {
    randomWalk(sim, 1.0);
  }


  /**
   * Randomly walks the agent and scales the resultant vector by {@param vecScalar}
   *
   * @param sim       Simulation containing agent
   * @param vecScalar Scalar by which vector will be adjusted
   */
  void randomWalk(Simulation sim, double vecScalar) {

    Double2D location = sim.space.getObjectLocation(this);
    MutableDouble2D modifier;

    if (previousModifier == null) {
      modifier = new MutableDouble2D((sim.random.nextDouble() - 0.5),
          (sim.random.nextDouble() - 0.5));
    } else {
      modifier = new MutableDouble2D(
          previousModifier.getX()
              + (sim.random.nextDouble() - 0.5) * SimConfig.INTENSITY_OF_RANDOM_WALK,
          previousModifier.getY()
              + (sim.random.nextDouble() - 0.5) * SimConfig.INTENSITY_OF_RANDOM_WALK);
    }

    //Every time decision return true, they are pulled to the centre slightly
    if (decision(sim, SimConfig.PROBABILITY_TO_BE_PULLED_TO_CENTRE)) {
      modifier
          .addIn((SimConfig.SIM_HEIGHT / 2.0 - location.getX())
                  * SimConfig.INTENSITY_OF_PULL_TO_CENTRE,
              (SimConfig.SIM_WIDTH / 2.0 - location.getY())
                  * SimConfig.INTENSITY_OF_PULL_TO_CENTRE);
    }

    if (modifier.length() != 0) {
      modifier.resize(vecScalar);
    }

    Double2D newLocation = new Double2D(location.getX() + modifier.getX(),
        location.getY() + modifier.getY());
    sim.space.setObjectLocation(this, newLocation);

    previousModifier = new Double2D(modifier);
  }

  /**
   * Function that gets a unit vector in the direction of an agent
   *
   * @param sim   Simulation object
   * @param agent Agent to move towards
   * @return A unit vector in the direction of {@param agent}
   */
  Double2D getVectorToAgent(Simulation sim, Agent agent) {
    return getVectorToAgent(sim, agent, 1.0);
  }

  /**
   * Function that gets a vector in the direction of an agent with length {@param scalar}
   *
   * @param sim    Simulation object
   * @param agent  Agent to move towards
   * @param scalar Length of vector
   * @return A vector in the direction of {@param agent}
   */
  Double2D getVectorToAgent(Simulation sim, Agent agent, double scalar) {
    MutableDouble2D vectorTowardsAgent = new MutableDouble2D(
        sim.space.getObjectLocation(agent).getX() - sim.space.getObjectLocation(this)
            .getX(),
        sim.space.getObjectLocation(agent).getY() - sim.space.getObjectLocation(this)
            .getY());
    if (vectorTowardsAgent.length() != 0) {
      vectorTowardsAgent.resize(scalar);
    }

    vectorTowardsAgent.addIn(sim.space.getObjectLocation(this));
    return new Double2D(vectorTowardsAgent);
  }


  /**
   * Returns True n times where n is given by {@param probability}
   *
   * @param state       Simulation Containing all agents
   * @param probability The probability that True is returned
   * @return Value depending outcome dictated by {@param probability}
   */
  private boolean decision(Simulation state, double probability) {
    double random = state.random.nextDouble();
    return random <= probability;
  }


  /**
   * Follow an agent denoted by {@param agentToFollow} and stay within the {@param minSeparation}
   * distance
   *
   * @param sim           Simulation containing agents
   * @param agentToFollow The agent who I will follow
   * @param maxSeparation The maximum distance for the agent to stray away from the partner
   */
  private void coupledWalk(Simulation sim, Agent agentToFollow, double maxSeparation) {
    if (sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(agentToFollow))
        > maxSeparation) {
      walkTowards(sim, getVectorToAgent(sim, agentToFollow));
    } else {
      randomWalk(sim);
    }
  }


  /**
   * Method that sets agent onto the blacklist and keeps its size in check
   *
   * @param agent Agent which is added to blacklist
   */
  void setOnBlacklist(Agent agent) {
    if (!this.agentBlacklist.contains(agent)) {
      this.agentBlacklist.addLast(agent); // Append to list O(1)
      // TODO: Change temp blacklist size when appropriate
      if (this.agentBlacklist.size() > SimConfig.AGENT_MAXIMUM_BLACKLIST_SIZE) {
        this.agentBlacklist.removeFirst(); // Pop from head O(1)
      }
    }
  }


  /**
   * Standard behaviour if agent is playing catch is that it does nothing
   *
   * @param sim   Simulation containing all agents
   * @param agent Agent who Im playing catch with
   */
  void playCatch(Simulation sim, Agent agent) {
    if (agent.isPlayingCatch() && isPlayingCatch()) {
      return;
    }
  }


  /**
   * Standard implementation of how an agent evaluates if pairing is favorable
   *
   * @param desiredAgent The agent which is seen as a superior partner over the agent which is
   *                     currently coupled to this instance
   */
  void competeForAgent(Agent desiredAgent) {
    if (desiredAgent == null) {
      return;
    }

    if (isCoupled() && desiredAgent.isCoupled()) {
      if (desiredAgent.getAffinity() > this.coupledAgent.getAffinity()
          && this.affinity > desiredAgent.getCoupledAgent().getAffinity()) {
        this.coupledAgent.setCoupledAgent(null); // Break up with my partner
        desiredAgent.getCoupledAgent()
            .setCoupledAgent(null); // Desired agent breaks up with its partner

        setCoupledAgent(desiredAgent);
        desiredAgent.setCoupledAgent(this);
      }
    } else if (!isCoupled() && desiredAgent.isCoupled()) {
      if (this.affinity > desiredAgent.getCoupledAgent().getAffinity()) {
        desiredAgent.getCoupledAgent().setCoupledAgent(null);
        desiredAgent.setCoupledAgent(this);
        setCoupledAgent(desiredAgent);
      }
    }
  }


  /**
   * Standard method to get agents nearby despite being. Agents are allowed to be coupled, but can
   * not be on the blacklist
   *
   * @param sim             Simulation containing all agents
   * @param cls             Type of agents which aught to be found
   * @param minDistToAgents Minimum distance potential agents can be from current instance
   * @return All agents which pass the filtering constrains
   */
  Bag getAgentsNearby(Simulation sim, Class<? extends Agent> cls, double minDistToAgents) {
    Bag agentsNearby = new Bag();
    Bag neighbours = sim.space.getAllObjects();

    Stream<Object> stream = neighbours.stream();
    agentsNearby.addAll(stream.filter(cls::isInstance).filter(
        obj -> sim.space.getObjectLocation(this)
            .distance(sim.space.getObjectLocation(obj)) < minDistToAgents)
        .filter(obj -> !getAgentBlacklist().contains(obj)).collect(
            Collectors.toList()));

    return agentsNearby;
  }

  public void setCoupledAgent(Agent agent) {
    this.coupledAgent = agent;
  }

  public Agent getCoupledAgent() {
    return this.coupledAgent;
  }

  public boolean isCoupled() {
    return coupledAgent != null;
  }

  public double getAffinity() {
    return affinity;
  }

  public LinkedList<Agent> getAgentBlacklist() {
    return this.agentBlacklist;
  }

  public double getStandard() {
    return standard;
  }

  public void setPlayingCatch(boolean isPlaying) {
    this.playingCatch = isPlaying;
  }

  public boolean isPlayingCatch() {
    return playingCatch;
  }
}
