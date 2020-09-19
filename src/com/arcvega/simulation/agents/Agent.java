package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.SimConfig;
import com.arcvega.simulation.config.Simulation;
import sim.engine.Steppable;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public abstract class Agent implements Steppable {

  private Double2D previousModifier = null;

  /**
   * Function that provides a simple wrapper around moving to a location for readability.
   */
  void walkTowards(Simulation sim, Double2D dest) {
    sim.space.setObjectLocation(this, dest);
  }

  /**
   * Function that just moves agent to random walk.
   */
  void randomWalk(Simulation sim) {
    Double2D location = sim.space.getObjectLocation(this);
    MutableDouble2D modifier;

    if (previousModifier == null) {
      modifier = new MutableDouble2D((sim.random.nextDouble() - 0.5),
          (sim.random.nextDouble() - 0.5));
    } else {
      modifier = new MutableDouble2D(
          previousModifier.getX()
              + (sim.random.nextDouble() - 0.5) * SimConfig.intensityOfRandomWalk,
          previousModifier.getY()
              + (sim.random.nextDouble() - 0.5) * SimConfig.intensityOfRandomWalk);
    }

    //Every time decision return true, they are pulled to the centre slightly
    if (decision(sim, SimConfig.probabiltyToBePulledToCentre)) {
      modifier
          .addIn((SimConfig.SIM_HEIGHT / 2.0 - location.getX()) * SimConfig.intensityOfPullToCentre,
              (SimConfig.SIM_WIDTH / 2.0 - location.getY()) * SimConfig.intensityOfPullToCentre);
    }

    modifier.resize(1);

    Double2D newLocation = new Double2D(location.getX() + modifier.getX(),
        location.getY() + modifier.getY());
    sim.space.setObjectLocation(this, newLocation);

    previousModifier = new Double2D(modifier);
  }

  /**
   * Returns true @param probability amount of times.
   */
  private boolean decision(Simulation state, double probability) {
    double random = state.random.nextDouble();
    return random <= probability;
  }
}
