/*******************************************************************************
 * Copyright [2020] [Philipp and Francisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.arcvega.simulation.config;

import com.arcvega.simulation.agents.Agent;
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
