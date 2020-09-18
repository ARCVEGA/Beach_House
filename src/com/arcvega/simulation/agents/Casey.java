package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;

public class Casey extends Agent {
    @Override
    public void step(SimState simState) {
        randomWalk((Simulation)simState);
    }
}
