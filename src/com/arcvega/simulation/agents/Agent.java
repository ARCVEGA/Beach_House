package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import sim.engine.Steppable;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public abstract class Agent implements Steppable {

    /**Function that provides a simple wrapper around moving to a location for readability.*/
    void walkTowards(Simulation sim, Double2D dest){
        sim.space.setObjectLocation(this, dest);
    }

    /**Function that just moves agent to random walk.*/
    void randomWalk(Simulation sim){
        Double2D location = sim.space.getObjectLocation(this);
        Double2D newLocation = new Double2D(location.getX() + (sim.random.nextDouble() - 0.5), location.getY() + (sim.random.nextDouble() - 0.5));
        sim.space.setObjectLocation(this, newLocation);
    }

    /**
     *
     * @param sim
     * @param agent
     * @return
     */
    Double2D getVectorToAgent(Simulation sim, Agent agent) {
        MutableDouble2D vectorTowardsAgent = new MutableDouble2D(
            sim.space.getObjectLocation(agent).getX() - sim.space.getObjectLocation(this)
                .getX(),
            sim.space.getObjectLocation(agent).getY() - sim.space.getObjectLocation(this)
                .getY());
        if (vectorTowardsAgent.length() != 0) {
            vectorTowardsAgent.resize(1);
        }

        vectorTowardsAgent.addIn(sim.space.getObjectLocation(this));
        return new Double2D(vectorTowardsAgent);
    }
}
