package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import sim.engine.SimState;

public class Matt extends Agent {
    @Override
    public void step(SimState simState) {
        randomWalk((Simulation) simState);
    }
}
