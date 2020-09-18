package com.arcvega.simulation.config;

public class SimConfig {

  /**Discretization of the 2D space*/
  public static final int SIM_DISCRETIZATION = 1;
  /**Width of the 2D space */
  public static final int SIM_WIDTH = 200;
  /**Height of the 2D space */
  public static final int SIM_HEIGHT = 200;

  /**Amount of CASEY agents in the simulation*/
  public static final int CASEY_AMOUNT = 20;
  /**Distance that a MATT has to be within to be considered by a CASEY*/
  public static final double CASEY_THRESHOLD_DISTANCE = 10;

  /**Max distance that a JIM is from his corresponding CASEY*/
  public static final double JIM_THRESHOLD_DISTANCE = 5;

  /**Amount of MATT agents in the simulation*/
  public static final int MATT_AMOUNT = 20;
}
