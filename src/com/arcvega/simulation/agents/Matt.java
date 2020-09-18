package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import sim.engine.SimState;

public class Matt extends Agent {

  /**Casey affinity is the quantified amount that a Casey
   * likes a particular Matt*/
  private final int caseyAffinity;

  public Matt(SimState simState){
    caseyAffinity = simState.random.nextInt(100);
  }

  @Override
  public void step(SimState simState) {
    randomWalk((Simulation) simState);
  }

  public int getCaseyAffinity() {
    return caseyAffinity;
  }
}
