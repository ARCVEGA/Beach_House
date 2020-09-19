package com.arcvega.simulation.config;

public class SimConfig {

  /**Discretization of the 2D space*/
  public static final int SIM_DISCRETIZATION = 1;
  /**Width of the 2D space */
  public static final int SIM_WIDTH = 200;
  /**Height of the 2D space */
  public static final int SIM_HEIGHT = 200;
  /**Height of the 2D space */
  public static final double intensityOfRandomWalk = 0.4;
  /**Height of the 2D space */
  public static final double intensityOfPullToCentre = 0.1;
  /**Height of the 2D space */
  public static final double probabiltyToBePulledToCentre = 0.01;

  /**Amount of CASEY agents in the simulation*/
  public static final int CASEY_AMOUNT = 200;
  /**Distance that a MATT has to be within to be considered by a CASEY*/
  public static final double CASEY_THRESHOLD_DISTANCE = 10;
  /**Minimum distance required for CASEY to couple with MATT*/
  public static final double CASEY_MINIMUM_COUPLING_DISTANCE = 2;

  /**Max distance that a JIM is from his corresponding CASEY*/
  public static final double JIM_THRESHOLD_DISTANCE = 50;
  /**Maximum distance JIM can be from MATT if CASEY is coupled*/
  public static final double JIM_MAX_DISTANCE_FROM_MATT = 2;
  /**Scalar for JIM movement vector when JIM moves towards coupled MATT*/
  public static final double JIM_CHARGE_MATT_SCALAR = 2.0;

  /**Amount of MATT agents in the simulation*/
  public static final int MATT_AMOUNT = 200;
  /**Max radius where potential CASEY partners can exist*/
  public static final double MATT_THRESHOLD_DISTANCE = 10;
  /**Minimum distance needed for MATT to couple with CASEY*/
  public static final double MATT_MINIMUM_COUPLING_DISTANCE = 15;
}
