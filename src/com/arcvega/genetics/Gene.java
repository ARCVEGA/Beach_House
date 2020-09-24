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

package com.arcvega.genetics;



/**
 * A generic class which represents a single gene as part of a Chromosome
 */
public class Gene {

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
