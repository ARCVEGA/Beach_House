package com.arcvega.simulation.config;

import com.arcvega.simulation.agents.Casey;
import com.arcvega.simulation.agents.Jim;
import com.arcvega.simulation.agents.Matt;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.util.Double2D;

public class Simulation extends SimState {

  public Continuous2D space = new Continuous2D(1, 200, 200);

  /**
   * Constructor that automatically sets seed to the current time.
   */
  public Simulation() {
    this(System.currentTimeMillis());
  }

  /**
   * Constructor that sets custom seed.
   */
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

    space.clear();

    for (int i = 0; i < 20; i++) {
      Casey casey = new Casey();
      Double2D caseyLocation = new Double2D(random.nextInt(200), random.nextInt(200));
      space.setObjectLocation(casey, caseyLocation);
      schedule.scheduleRepeating(casey);

      Jim jim = new Jim(casey);
      space.setObjectLocation(jim, caseyLocation);
      schedule.scheduleRepeating(jim);
    }

    for (int i = 0; i < 20; i++) {
      Matt matt = new Matt(this);
      space.setObjectLocation(matt, new Double2D(random.nextInt(200), random.nextInt(200)));
      schedule.scheduleRepeating(matt);
    }
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