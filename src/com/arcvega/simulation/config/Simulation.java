package com.arcvega.simulation.config;

import com.arcvega.simulation.agents.Agent;
import com.arcvega.simulation.agents.Casey;
import com.arcvega.simulation.agents.Jim;
import com.arcvega.simulation.agents.Matt;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.Double2D;

public class Simulation extends SimState {

  public Continuous2D space = new Continuous2D(SimConfig.SIM_DISCRETIZATION, SimConfig.SIM_WIDTH,
      SimConfig.SIM_HEIGHT);
  Network agentNetwork = new Network(false); // TODO: Undirected for now

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

    agentNetwork.clear();

    for (int i = 0; i < SimConfig.CASEY_AMOUNT; i++) {
      Casey casey = new Casey(this);
      Double2D caseyLocation = new Double2D(random.nextInt(200), random.nextInt(200));
      space.setObjectLocation(casey, caseyLocation);
      schedule.scheduleRepeating(casey);

      Jim jim = new Jim(this, casey);
      space.setObjectLocation(jim, caseyLocation);
      schedule.scheduleRepeating(jim);

      agentNetwork.addNode(casey);
      agentNetwork.addNode(jim);

      agentNetwork.addEdge(casey, jim, null); // No info shared for now
    }

    for (int i = 0; i < SimConfig.MATT_AMOUNT; i++) {
      Matt matt = new Matt(this);
      space.setObjectLocation(matt, new Double2D(random.nextInt(200), random.nextInt(200)));
      schedule.scheduleRepeating(matt);

      agentNetwork.addNode(matt);
    }
  }

  public Network getAgentNetwork() {
    return agentNetwork;
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

  /* The following methods are for the model inspector, allowing us to tune simulation parameters
    without needing to restart the entire simulation */

  public int getNumCaseys() {
    return SimConfig.CASEY_AMOUNT;
  }

  public void setNumCaseys(int numCaseys) {
    SimConfig.CASEY_AMOUNT = numCaseys;
  }

  public int getNumMatts() {
    return SimConfig.MATT_AMOUNT;
  }

  public void setNumMatts(int numMatts) {
    SimConfig.MATT_AMOUNT = numMatts;
  }


  public double[] getMattAffinityDistribution() {
    return InspectorHelper.getAffinityDistribution(this, Casey.class);
  }


  public double[] getCaseyAffinityDistribution() {
    return InspectorHelper.getAffinityDistribution(this, Matt.class);
  }

  public boolean[] getCoupledCaseyDistribution() {
    return InspectorHelper.getCoupledDistribution(this, Casey.class);
  }

  public boolean[] getCoupledMattDistribution() {
        return InspectorHelper.getCoupledDistribution(this, Matt.class);
  }
}
