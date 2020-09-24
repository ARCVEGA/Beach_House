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

import com.arcvega.simulation.agents.Casey;
import com.arcvega.simulation.agents.Jim;
import com.arcvega.simulation.agents.Matt;
import java.awt.Color;
import javax.swing.JFrame;
import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.network.NetworkPortrayal2D;
import sim.portrayal.network.SimpleEdgePortrayal2D;
import sim.portrayal.network.SpatialNetwork2D;
import sim.portrayal.simple.OvalPortrayal2D;

public class SimulationWithUI extends GUIState {

  /**
   * Display object, can display multiple portrayals
   */
  private Display2D display;
  /**
   * The frame that will encapsulate the display
   */
  private JFrame displayFrame;
  private ContinuousPortrayal2D spacePortrayal = new ContinuousPortrayal2D();
  private NetworkPortrayal2D agentNetworkPortrayal = new NetworkPortrayal2D();

  public SimulationWithUI(SimState state) {
    super(state);
  }

  public SimulationWithUI() {
    super(new Simulation());
  }

  public Object getSimulationInspectedObject() {
    return state;
  }

  /**
   * Get simulation inspect in order to tweak settings on the fly. The inspector is volatile since
   * it operates during the sim when we get histograms or other data
   *
   * @return Inspector for the entire model
   */
  public Inspector getInspector() {
    Inspector inspector = super.getInspector();
    inspector.setVolatile(true);
    return inspector;
  }

  /**
   * Called when the GUI is initially created. Function created the JFrame window and ensures to
   * register the frame so the console can find it.
   *
   * @param controller Controller class used to register Frames created
   */
  @Override
  public void init(Controller controller) {
    super.init(controller);

    /*Creates our display for the actual simulation*/
    display = new Display2D(920, 920, this);
    display.setClipping(false);

    /*Creates JFrame, configures it and registers it with the console*/
    displayFrame = display.createFrame();
    displayFrame.setTitle("Beach House");
    controller.registerFrame(displayFrame);
    displayFrame.setVisible(true);

    display.attach(spacePortrayal, "2DSpace");
    display.attach(agentNetworkPortrayal, "2DNetworkPortrayal");
  }

  /**
   * Called when the play button is pressed, just before SimState.start() is called.
   */
  @Override
  public void start() {
    super.start();

    spacePortrayal.setField(((Simulation) state).space);
    spacePortrayal.setPortrayalForClass(Casey.class, new OvalPortrayal2D(Color.red, 2));
    spacePortrayal.setPortrayalForClass(Matt.class, new OvalPortrayal2D(Color.black, 2));
    spacePortrayal.setPortrayalForClass(Jim.class, new OvalPortrayal2D(Color.blue, 2));

    agentNetworkPortrayal.setField(
        new SpatialNetwork2D(((Simulation) state).space, ((Simulation) state).agentNetwork));
    agentNetworkPortrayal.setPortrayalForAll(new SimpleEdgePortrayal2D());

    // Quick reset of the frame to make sure there is a clean board
    display.reset();
    display.setBackdrop(new Color(168, 221, 181));
    display.repaint();
  }

  /**
   * Called when the GUI is about to be destroyed.
   */
  @Override
  public void quit() {
    super.quit();
    if (displayFrame != null) {
      displayFrame.dispose();
    }
    displayFrame = null;
    display = null;
  }

  /**
   * Called when a simulation is loaded from a checkpoint.
   *
   * @param state Sets global state variable and reloads portrayals
   */
  @Override
  public void load(SimState state) {
    super.load(state);
  }

  public static void main(String[] args) {
    SimulationWithUI sim = new SimulationWithUI();
    Console console = new Console(sim);
    console.setVisible(true);
  }
}
