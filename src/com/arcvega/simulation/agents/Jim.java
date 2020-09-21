package com.arcvega.simulation.agents;

import com.arcvega.simulation.config.Simulation;
import com.arcvega.simulation.config.SimConfig;
import sim.engine.SimState;
import sim.util.Double2D;
import sim.util.MutableDouble2D;


public class Jim extends Agent {

  private final Casey casey;
  private boolean hasAcceptedMatt = false;

  public Jim(Simulation sim, Casey casey) {
    super(sim);
    this.casey = casey;
  }

  @Override
  public void step(SimState simState) {
    Simulation sim = (Simulation) simState;

    if (!this.casey.isCoupled()) {
      keepNearCasey(sim);
    } else {
      chargeAtMatt(sim);
    }

  }

  /**
   * Defines the way Jim walks if Casey is not coupled with a Matt
   *
   * @param sim Simulation containing the agents
   */
  private void keepNearCasey(Simulation sim) {
    if (sim.space.getObjectLocation(this).distance(sim.space.getObjectLocation(casey))
        < SimConfig.JIM_THRESHOLD_DISTANCE) {
      randomWalk(sim);
    } else {
      walkTowards(sim, getVectorToAgent(sim, casey));
    }
  }


  /**
   * Jim becomes agitated and moves towards Matt for a confrontation rather quickly and
   * aggressively
   *
   * @param sim Simulation containing Agents
   */
  private void chargeAtMatt(Simulation sim) {
    if (sim.space.getObjectLocation(this)
        .distance(sim.space.getObjectLocation(this.casey.getCoupledMatt()))
        < SimConfig.JIM_MAX_DISTANCE_FROM_MATT) {
      if (!hasAcceptedMatt && this.casey.isCoupled()) {
        playCatch(this.casey.getCoupledMatt());
      }
    } else {
      MutableDouble2D aggressiveVector = new MutableDouble2D(
          getVectorToAgent(sim, casey.getCoupledMatt(), SimConfig.JIM_CHARGE_MATT_SCALAR));
      /*Perform walk to Matt using aggressive vector*/
      walkTowards(sim, new Double2D(aggressiveVector));
    }
  }


  /**
   * Jim and {@param matt} stop walking and play a game of catch where Jim will evaluate {@param
   * matt}
   *
   * @param agent Individual who is paired with {@link Jim#casey}
   */
  @Override
  void playCatch(Agent agent) {
    Matt matt = (Matt) agent;

    // Start a game of catch
    matt.playCatch(this);
    setPlayingCatch(true);

    if (!isAcceptablePartner(matt)) {
      // Make casey uncouple matt
      this.casey.setCoupledAgent(null);

      // Make matt uncouple with casey
      matt.setCoupledAgent(null);

      // Place them on each others blacklist
      this.casey.setOnBlacklist(matt);
      matt.setOnBlacklist(this.casey);

      // Stop playing catch
      matt.setPlayingCatch(false);
      this.setPlayingCatch(false);
    } else {
      this.hasAcceptedMatt = true;

      matt.setPlayingCatch(false);
      this.setPlayingCatch(false);
    }

  }


  /**
   * Evaluate {@param matt} and decides if he accepts or rejects the instance
   *
   * @param matt Instance coupled with {@link Jim#casey}
   * @return True if {@param matt} is accepted, else false
   */
  private boolean isAcceptablePartner(Matt matt) {
    // TODO: This method is simple for now, but will grow in complexity once genetics are added
    return matt.getCaseyAffinity() >= this.standard;
  }

}
