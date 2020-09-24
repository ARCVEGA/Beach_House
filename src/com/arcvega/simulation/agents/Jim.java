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
import sim.util.Double2D;
import sim.util.MutableDouble2D;


public class Jim extends Agent {

  private final Casey casey;
  private Matt caseysPairedMatt = null;

  public Jim(Simulation sim, Casey casey) {
    super(sim);
    this.casey = casey;
  }

  @Override
  public void step(SimState simState) {
    Simulation sim = (Simulation) simState;

    if (!this.casey.isCoupled() || hasAcceptedMatt((Matt) this.casey.getCoupledAgent())) {
      keepNearCasey(sim);
    } else {
      chargeAtMatt(sim);
    }

  }

  /**
   * Jim walks randomly while he is within {@link SimConfig#JIM_THRESHOLD_DISTANCE} units of {@link
   * Jim#casey}, if the distance is exceeded Jim moves back to adhere to this constraint
   *
   * @param sim Simulation containing the agents
   */
  private void keepNearCasey(Simulation sim) {
    if (sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(casey))
        < SimConfig.JIM_THRESHOLD_DISTANCE) {
      randomWalk(sim);
    } else {
      walkTowards(sim, getVectorToAgent(sim, casey));
    }
  }


  /**
   * Jim becomes agitated and moves towards Matt for a confrontation rather quickly and aggressively
   * so that he can confront him and play a round of catch
   *
   * @param sim Simulation containing Agents
   */
  private void chargeAtMatt(Simulation sim) {
    if (sim.space.getObjectLocation(this)
        .distance(sim.space.getObjectLocation(this.casey.getCoupledAgent()))
        < SimConfig.JIM_MAX_DISTANCE_FROM_MATT) {
      if (!hasAcceptedMatt((Matt) this.casey.getCoupledAgent()) && this.casey.isCoupled()) {
        playCatch(sim, this.casey.getCoupledAgent());
      }
    } else {
      MutableDouble2D aggressiveVector = new MutableDouble2D(
          getVectorToAgent(sim, casey.getCoupledAgent(), SimConfig.JIM_CHARGE_MATT_SCALAR));
      /*Perform walk to Matt using aggressive vector*/
      walkTowards(sim, new Double2D(aggressiveVector));
    }
  }


  /**
   * Jim and Matt stop walking and play a game of catch where Jim will evaluate {@param agent} which
   * Jim will use to evaluate Matt, aka {@param agent}. If Matt fails he will be uncoupled from
   * Casey and will be blacklisted, otherwise he gets to stay coupled
   *
   * @param sim   Simulation containing all agents
   * @param agent Individual who is paired with {@link Jim#casey}
   */
  @Override
  void playCatch(Simulation sim, Agent agent) {
    Matt matt = (Matt) agent;

    // Start a game of catch
    matt.playCatch(sim, this);
    setPlayingCatch(true);

    if (!isAcceptablePartner(matt)) {
      this.casey.setCoupledAgent(null);
      matt.setCoupledAgent(null);

      this.casey.setOnBlacklist(matt);
      matt.setOnBlacklist(this.casey);

      sim.getAgentNetwork().removeEdge(sim.getAgentNetwork().getEdge(this.casey, matt));

      matt.setPlayingCatch(false);
      this.setPlayingCatch(false);
    } else {
      this.caseysPairedMatt = matt;
      matt.setPlayingCatch(false);
      this.setPlayingCatch(false);
    }

  }


  /**
   * Evaluate {@param matt} and decides if he accepts or rejects the instance
   *
   * @param matt Instance coupled with {@link Jim#casey}
   * @return True if {@param matt} is accepted, else false
   */
  private boolean isAcceptablePartner(Matt matt) {
    // TODO: This method is simple for now, but will grow in complexity once genetics are added
    return (matt.getAffinity() >= this.standard);
  }

  private boolean hasAcceptedMatt(Matt caseyPartner) {
    return (caseyPartner == this.caseysPairedMatt);
  }

}
