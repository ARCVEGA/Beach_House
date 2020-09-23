package com.arcvega.genetics;



/**
 * A generic class which represents a single gene as part of a Chromosome
 */
public abstract class Gene {

  /**The label is used so that we can cross the correct genes during genetic exchange*/
  private final double mutationProbability;
  private final String geneLabel;
  private double geneFitness;


  public Gene(String label, double prob) {
    this.geneLabel = label;
    this.mutationProbability = prob;
  }

  /**
   * Overloaded constructor
   *
   * @param label   Label attributed to the gene in the genetic representation (name, node pairs,
   *                mass in bin-packing etc)
   * @param fitness Fitness value of the gene
   * @param prob    Probability that the gene will mutate given as a floating point between 0 and 1
   */
  public Gene(String label, double fitness, double prob) {
    this.geneLabel = label;
    this.geneFitness = fitness;
    this.mutationProbability = prob;
  }


  /**
   * Defines how a gene will be mutated if it is deemed necessary
   */
  public void mutateGene() {
  }

  private void computeGeneFitness() {
    this.geneFitness = 0;
  }

  public String getGeneLabel() {
    return this.geneLabel;
  }

  public double getMutationProbability() {
    return mutationProbability;
  }

  public double getGeneFitness() {
    return geneFitness;
  }
}
