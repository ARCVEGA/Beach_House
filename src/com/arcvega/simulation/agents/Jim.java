package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import sim.engine.SimState;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public class Jim extends Agent {
  private Casey casey;
  private final double thresholdRadius = 5;

  public Jim(Casey casey){
    this.casey = casey;
  }

  @Override
  public void step(SimState simState) {
    Simulation sim = (Simulation) simState;
    if(sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(casey)) < thresholdRadius)
      randomWalk((Simulation) simState);
    else{
      walkTowards(sim, getVectorToAgent(sim, casey));
    }
  }

}
