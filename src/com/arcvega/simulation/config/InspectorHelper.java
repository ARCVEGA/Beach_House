package com.arcvega.simulation.config;

import com.arcvega.simulation.agents.Agent;
import com.arcvega.simulation.agents.Casey;
import com.arcvega.simulation.agents.Matt;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import sim.util.Bag;

public class InspectorHelper {


  /**
   * Helper class which will get the affinity of an agent depending on its class
   *
   * @param sim Simulation containing the agent
   * @param cls The agents class
   * @return Affinity distribution
   */
  // TODO: This can definitely be rewritten in a nicer way
  public static double[] getAffinityDistribution(Simulation sim, Class<? extends Agent> cls) {
    Bag agents = new Bag();
    Stream<Agent> stream = sim.getAgentNetwork().getAllNodes().stream();
    agents.addAll(stream.filter(cls::isInstance).collect(Collectors.toList()));
    double[] distrib = new double[agents.numObjs];

    for (int i = 0; i < agents.numObjs; i++) {
      distrib[i] = (cls.cast(agents.get(i))).getAffinity();
    }

    return distrib;
  }


  /**
   * Helper class which gets the distribution of coupled agents
   *
   * @param sim Simulation containing all agens
   * @param cls The type of agent which is of interest
   * @return The distribution of coupled agents
   */
  public static boolean[] getCoupledDistribution(Simulation sim, Class<? extends Agent> cls) {
    Bag agents = new Bag();
    Stream<Agent> stream = sim.getAgentNetwork().getAllNodes().stream();
    agents.addAll(stream.filter(cls::isInstance).collect(Collectors.toList()));
    boolean[] distrib = new boolean[agents.numObjs];

    for (int i = 0; i < agents.numObjs; i++) {
      distrib[i] = (cls.cast(agents.get(i))).isCoupled();
    }

    return distrib;
  }

}
