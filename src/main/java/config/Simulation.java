package main.java.config;

import sim.engine.SimState;

public class Simulation extends SimState {

    /**
     * Constructor that automatically sets seed to the current time.
     */
    public Simulation() {
        this(System.currentTimeMillis());
    }

    /** Constructor that sets custom seed. */
    public Simulation(long seed) {
        super(seed);
    }

    /**
     * Function called at the start of running the model. Clears and initializes the objects in the
     * simulation.
     */
    @Override
    public void start() {
        super.start();
    }

    /**
     * Function called either at the end of a simulation. Used for clean-up.
     */
    @Override
    public void finish() {
        super.finish();
    }

    public static void main(String[] args) {
        /* doLoop steps:
        - Create instance of SimState subclass and initialize random number generator
        - Call start() from your subclass
        - Repeatedly call step()
        - When schedule entirely empty of agents call finish() to clean up */
        doLoop(Simulation.class, args);

        /* Since the framework uses threads, exit(0) has to be called, just in case
        you forget to turn your user threads into daemon threads. Daemon threads do not prevent
        the JVM from shutting down, whilst user threads do. */
        System.exit(0);
    }
}
