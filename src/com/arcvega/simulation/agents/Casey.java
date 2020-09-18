package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Casey extends Agent {
    private final double thresholdDistance = 100;

    @Override
    public void step(SimState simState) {
        Simulation sim = (Simulation) simState;

        Bag potentialMatts = getMattsNearby(sim);
        if(potentialMatts.isEmpty())
            randomWalk((Simulation)simState);
        else{
            MutableDouble2D vectorTowardsMatt = new MutableDouble2D(sim.space.getObjectLocation(potentialMatts.get(0)).getX() - sim.space.getObjectLocation(this).getX(),
                    sim.space.getObjectLocation(potentialMatts.get(0)).getY() - sim.space.getObjectLocation(this).getY());
            vectorTowardsMatt.resize(1);
            vectorTowardsMatt.addIn(sim.space.getObjectLocation(this));
            sim.space.setObjectLocation(this, new Double2D(vectorTowardsMatt));
        }
    }

    private Bag getMattsNearby(Simulation sim){
        Bag potentialMatts = new Bag();
        Bag neighbours = sim.space.getAllObjects();

        Stream<Object> stream = neighbours.stream();
        potentialMatts.addAll(stream
                .filter(obj -> obj instanceof Matt)
                .filter(obj -> calculateDistance(
                        sim.space.getObjectLocation(obj).getX(),
                        sim.space.getObjectLocation(obj).getY(),
                        sim.space.getObjectLocation(this).getX(),
                        sim.space.getObjectLocation(this).getY())
                        < thresholdDistance)
                .collect(Collectors.toList()));

        return potentialMatts;
    }
}
