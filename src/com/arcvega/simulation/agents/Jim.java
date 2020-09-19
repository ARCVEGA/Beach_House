package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import com.arcvega.simulation.config.SimConfig;
import sim.engine.SimState;
import sim.util.Double2D;
import sim.util.MutableDouble2D;


// TODO: Get JIM to move towards MATT if his Casey is coupled
public class Jim extends Agent {

  private final Casey casey;

  public Jim(Casey casey) {
    this.casey = casey;
  }

  @Override
  public void step(SimState simState) {
    Simulation sim = (Simulation) simState;

    if (!this.casey.isCoupled()) {
      stepForUncoupled(sim);
    } else {
      stepForCoupled(sim);
    }

  }

  /**
   * Defines the way Jim walks if Casey is not coupled with a Matt
   *
   * @param sim Simulation containing the agents
   */
  private void stepForUncoupled(Simulation sim) {
    if (sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(casey))
        < SimConfig.JIM_THRESHOLD_DISTANCE) {
      randomWalk(sim);
    } else {
      walkTowards(sim, getVectorToAgent(sim, casey));
    }
  }


  /**
   * Jim becomes agitated and moves towards Matt for a confrontation
   *
   * @param sim Simulation containing Agents
   */
  private void stepForCoupled(Simulation sim) {
    if (sim.space.getObjectLocation(this)
        .distance(sim.space.getObjectLocation(this.casey.getCoupledMatt()))
        < SimConfig.JIM_MAX_DISTANCE_FROM_MATT) {
      randomWalk(sim);
    } else {
      MutableDouble2D aggressiveVector = new MutableDouble2D(
          getVectorToAgent(sim, casey.getCoupledMatt()));
      /**Make Jim move towards Matt rapidly*/
      aggressiveVector.addIn(
          new Double2D((aggressiveVector.x - sim.space.getObjectLocation(this).x)
              * SimConfig.JIM_CHARGE_MATT_SCALAR,
              (aggressiveVector.y - sim.space.getObjectLocation(this).y)
              * SimConfig.JIM_CHARGE_MATT_SCALAR));
      walkTowards(sim, new Double2D(aggressiveVector));
    }
  }

}
