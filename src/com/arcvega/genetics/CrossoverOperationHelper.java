package com.arcvega.genetics;

import java.util.ArrayList;

public class CrossoverOperationHelper {


  /**
   * Performs single point crossover, chromosomes are cut at 1/2 of their original length and
   * crossed over. The method expects that both chromosomes are of identical length
   *
   * @param parent1 Chromosome of the first parent
   * @param parent2 Chromosome of the second parent
   * @return Single child chromosome selected randomly
   * @throws Exception Chromosomes are expected to have the same length
   */
  public static Chromosome uniformOnePointX(Chromosome parent1, Chromosome parent2)
      throws Exception {
    if (parent1.getGeneticSequence().size() != parent2.getGeneticSequence().size()) {
      throw new Exception("Chromosomes length must be identical");
    }

    int cutPoint = parent1.getGeneticSequence().size() / 2;
    ArrayList<Gene> genes = new ArrayList<>();

    // Add half of mothers genes
    for (int i = 0; i < cutPoint; i++) {
      genes.add(parent2.getGeneticSequence().get(i));
    }

    // Add half of fathers genes
    for (int i = cutPoint; i < parent1.getGeneticSequence().size(); i++) {
      genes.add(parent1.getGeneticSequence().get(i));
    }

    return new Chromosome(genes);
  }


  /**
   * Replaces first and last quarter of father gene with mother genes
   *
   * @param parent1 Father of the child
   * @param parent2 Mother of the child
   * @return Child Chromosome after 2PX
   * @throws Exception Chromosomes must have identical length and be divisible by 4
   */
  public static Chromosome uniformTwoPointX(Chromosome parent1, Chromosome parent2)
      throws Exception {
    if (parent1.getGeneticSequence().size() != parent2.getGeneticSequence().size()) {
      throw new Exception("Chromosomes length must be identical");
    } else if (parent1.getGeneticSequence().size() % 4 != 0) {
      throw new Exception("Uniform two point crossover expects chromosome to be divisible by 4");
    }

    int cutPoint1 = parent1.getGeneticSequence().size() / 4;
    int cutPoint2 = 3 * cutPoint1;
    ArrayList<Gene> genes = new ArrayList<>();

    // Replace father genes with mother genes
    for (int i = 0; i < cutPoint1; i++) {
      genes.add(parent2.getGeneticSequence().get(i));
    }

    // Add father genes
    for (int i = cutPoint1; i < cutPoint2; i++) {
      genes.add(parent1.getGeneticSequence().get(i));
    }

    // Add mother genes
    for (int i = cutPoint2; i < parent1.getGeneticSequence().size(); i++) {
      genes.add(parent2.getGeneticSequence().get(i));
    }

    return new Chromosome(genes);
  }

}