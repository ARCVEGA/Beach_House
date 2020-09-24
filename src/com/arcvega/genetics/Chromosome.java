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


import java.util.ArrayList;

public class Chromosome {

  private ArrayList<Gene> geneticSequence = null;


  public Chromosome(ArrayList<Gene> genes) {
    this.geneticSequence = genes;
  }


  /**
   * Takes two lists of genes and combines them into a chromosome
   *
   * @param lhGenes Genes which start the genetic sequence
   * @param rhGenes Genes which end the genetic sequence
   */
  public Chromosome(ArrayList<Gene> lhGenes, ArrayList<Gene> rhGenes) {
    lhGenes.addAll(rhGenes);
    this.geneticSequence = lhGenes;
  }

  public ArrayList<Gene> getGeneticSequence() {
    return this.geneticSequence;
  }
}
