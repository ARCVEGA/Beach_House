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
