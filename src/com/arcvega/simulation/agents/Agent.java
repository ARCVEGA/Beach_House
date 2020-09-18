package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;

public abstract class Agent implements Steppable {
    public void randomWalk(Simulation sim){
        Double2D location = sim.space.getObjectLocation(this);
        Double2D newLocation = new Double2D(location.getX() + (sim.random.nextDouble() - 0.5), location.getY() + (sim.random.nextDouble() - 0.5));
        sim.space.setObjectLocation(this, newLocation);
    }
}
