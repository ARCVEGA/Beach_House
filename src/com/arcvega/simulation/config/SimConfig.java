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

public class SimConfig {

  /**Discretization of the 2D space*/
  public static final int SIM_DISCRETIZATION = 1;
  /**Width of the 2D space */
  public static final int SIM_WIDTH = 200;
  /**Height of the 2D space */
  public static final int SIM_HEIGHT = 200;
  /**Height of the 2D space */
  public static final double INTENSITY_OF_RANDOM_WALK = 0.4;
  /**Height of the 2D space */
  public static final double INTENSITY_OF_PULL_TO_CENTRE = 0.1;
  /**Height of the 2D space */
  public static final double PROBABILITY_TO_BE_PULLED_TO_CENTRE = 0.01;

  /**Amount of CASEY agents in the simulation, not final so that inspector can use it*/
  public static int CASEY_AMOUNT = 10;
  /**Distance that a MATT has to be within to be considered by a CASEY*/
  public static final double CASEY_THRESHOLD_DISTANCE = 10;
  /**Minimum distance required for CASEY to couple with MATT*/
  public static final double CASEY_MINIMUM_COUPLING_DISTANCE = 2;


  /**Max distance that a JIM is from his corresponding CASEY*/
  public static final double JIM_THRESHOLD_DISTANCE = 50;
  /**Maximum distance JIM can be from MATT if CASEY is coupled*/
  public static final double JIM_MAX_DISTANCE_FROM_MATT = 2;
  /**Scalar for JIM movement vector when JIM moves towards coupled MATT*/
  public static final double JIM_CHARGE_MATT_SCALAR = 2.5;

  /**Amount of MATT agents in the simulation, not final so inspector can use it*/
  public static int MATT_AMOUNT = 50;
  /**Max radius where potential CASEY partners can exist*/
  public static final double MATT_THRESHOLD_DISTANCE = 100;
  /**Minimum distance needed for MATT to couple with CASEY*/
  public static final double MATT_MINIMUM_COUPLING_DISTANCE = 15;


  /**Scalar which dictates flight response for now*/
  public static final double FLIGHT_RESPONSE = 2.0; // TODO: This will eventually be done by genetics
  /**Maximum number of individuals that can be blacklisted*/
  public static final int AGENT_MAXIMUM_BLACKLIST_SIZE  = 5;
}
