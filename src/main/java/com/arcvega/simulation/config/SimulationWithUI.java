package com.arcvega.simulation.config;

import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;

import javax.swing.*;

public class SimulationWithUI extends GUIState {

    /** Display object, can display multiple portrayals */
    private Display2D display;
    /** The frame that will encapsulate the display */
    private JFrame displayFrame;

    public SimulationWithUI(SimState state) {
        super(state);
    }

    public SimulationWithUI(){
        super(new Simulation());
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
    }

    /** Called when the play button is pressed, just before SimState.start() is called. */
    @Override
    public void start() {
        super.start();
    }

    /** Called when the GUI is about to be destroyed. */
    @Override
    public void quit() {
        super.quit();
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
