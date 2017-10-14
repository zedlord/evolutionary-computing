import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.HashSet;
import java.util.Set;
import java.lang.Math;

public class player02-annealing implements ContestSubmission {
	Random rand;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;

	public player02-annealing()
	{
		rand = new Random();
	}

	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rand.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;

		// Get evaluation properties
		Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }

    public double[] getNeighbour(double[] agent){
		int position = rand.nextInt(10) + 1;
		return agent[position] = -5 + (5 - -5) * rand.nextDouble();
	}

	public double calculateProbability(double fitnessNeighbour, double fitness, int temperature){
        return Math.exp((fitnessNeighbour - fitness) / temperature);
    }

	public void run(){
		int populationSize = 50;

		double[] fitnessArray = new double[populationSize];
	
		System.out.println("populationSize: " + populationSize);
		System.out.println("crossoverRate: " + differentialRate);
		System.out.println("differentialRate: " + crossoverRate);
		// Run your algorithm here
        int evals = 0;
        // init population
		double[][] population = new double[populationSize][10];
		for(int i = 0; i < populationSize; i++){
			for(int j = 0; j < 10; j++){
				population[i][j] = -5 + (5 - -5) * rand.nextDouble();
			}
			fitnessArray[i] = 0;
		}

        // calculate fitness
        while(evals<evaluations_limit_){
			int temperature = evaluations_limit_ - evals;
			// Do annealing for every agent
			for(int i = 0; i < populationSize; i++){
				double[] neighbour = getNeighbour(population[i]);
                Double fitnessNeighbour = (double) evaluation_.evaluate(neighbour);
                if(fitnessNeighbour > fitnessArray[i]){
                    newPopulation[i] = fitnessNeighbour;
                } else {
                    double probability = calculateProbability(fitnessNeighbour, fitnessArray[i], temperature);
                    if( Math.random() < probability){
                        newPopulation[i] = fitnessNeighbour;
                    }
                }
			}

			//Eval new population
			for(int i = 0; i < populationSize; i++){
				Double fitnessNew = (double) evaluation_.evaluate(newPopulation[i]);
				evals++;

				if(fitnessNew < fitnessArray[i]){
					//Keep old agent
					newPopulation[i] = population[i];
				} else {
					fitnessArray[i] = fitnessNew;
				}
			}

			population = newPopulation;

            // Select survivors
        }

	}
}
