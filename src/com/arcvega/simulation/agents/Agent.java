package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import sim.engine.Steppable;
import sim.util.Double2D;

public abstract class Agent implements Steppable {
    void randomWalk(Simulation sim){
        Double2D location = sim.space.getObjectLocation(this);
        Double2D newLocation = new Double2D(location.getX() + (sim.random.nextDouble() - 0.5), location.getY() + (sim.random.nextDouble() - 0.5));
        sim.space.setObjectLocation(this, newLocation);
    }

    double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
