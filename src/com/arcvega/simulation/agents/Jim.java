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
      walkTowards(sim, getVectorToCasey(sim));
    }
  }

  private Double2D getVectorToCasey(Simulation sim) {
    MutableDouble2D vectorTowardsCasey = new MutableDouble2D(
        sim.space.getObjectLocation(casey).getX() - sim.space.getObjectLocation(this)
            .getX(),
        sim.space.getObjectLocation(casey).getY() - sim.space.getObjectLocation(this)
            .getY());
    if (vectorTowardsCasey.length() != 0) {
      vectorTowardsCasey.resize(1);
    }

    vectorTowardsCasey.addIn(sim.space.getObjectLocation(this));
    return new Double2D(vectorTowardsCasey);
  }
}
