import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.HashSet;
import java.util.Set;

public class player02Differential implements ContestSubmission
{
	Random rand;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;

	public player02Differential()
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

    private int[] chooseRandomAgents(int pSize, int agentExclude, int pertubationSize){
		
		int[] randomAgents = new int[2*pertubationSize + 1];
		Set<Integer> set = new HashSet<Integer>();

		set.add(agentExclude);
		while (set.size() < (2*pertubationSize + 1)){
			set.add(rand.nextInt(pSize));
		}
		set.remove(agentExclude);

		int i = 0;
		for (Integer val : set){
			randomAgents[i++] = val;
			
		}
		return randomAgents;
	}

	private int returnBestAgent(double[] myArray){

		// return argmax

		int bestAgent = rand.nextInt(10);
		for(int k = 0; k < myArray.length; k++){
			if(myArray[k] > myArray[bestAgent]){
				bestAgent = k;
			}
		}

		return bestAgent;
	}

	public void run()
	{
		// parameters
		// double crossoverRate = 0.5;
		// double differentialRate = 0.69;
		// int populationSize = 20;
		// int pertubationSize = 2; // in {1,2,..., populationSize/2 - 1}
		// Boolean bestBase = false; // otherwise best

        Random randParameters = new Random();
		double crossoverRate = randParameters.nextDouble(); // min 0 max 1
		double differentialRate = 2 * randParameters.nextDouble(); // min 0, max 2
		int populationSize = 4 + randParameters.nextInt(47); // min 4, max?
		int pertubationSize = 1 + randParameters.nextInt(Math.min(3, (populationSize-1)/2));
		Boolean bestBase = false; // of rand.nextBoolean(), maar werkt slecht.

		System.out.println("populationSize: " + populationSize);
		System.out.println("crossoverRate: " + differentialRate);
		System.out.println("differentialRate: " + crossoverRate);
		System.out.println("pertubationSize: " + pertubationSize);
		System.out.println("bestBase: " + bestBase);
		
		// Run your algorithm here
        int evals = 0;

        // init population
		double[] fitnessArray = new double[populationSize];
		double[][] population = new double[populationSize][10];
		for(int i = 0; i < populationSize; i++){
			for(int j = 0; j < 10; j++){
				population[i][j] = -5 + 10 * rand.nextDouble();
			}
		}

        // calculate fitness
        while(evals + populationSize <evaluations_limit_){

			double[][] newPopulation = new double[populationSize][10];
			//Loop through all agents
			for(int i = 0; i < populationSize; i++){

				//Create array to choose random agents from the population
				int[] randomAgents = chooseRandomAgents(populationSize, i, pertubationSize);
				int randomIndex = rand.nextInt(10);

				// Compute new position of agent
				for(int j = 0; j < 10; j++){
					double uniDistrNumber = 0 + (1 - 0) * rand.nextDouble();
					if(uniDistrNumber < crossoverRate || j == randomIndex){
						// base vector
						if(bestBase){
							int bestAgent = returnBestAgent(fitnessArray);
							newPopulation[i][j] = population[bestAgent][j];
						} else {
							newPopulation[i][j] = population[randomAgents[0]][j];
						}
						// pertubation vectors
						for(int k = 0; k < pertubationSize; k++){
							newPopulation[i][j] += differentialRate * (population[randomAgents[2*k + 1]][j] - population[randomAgents[2*k + 2]][j]);
						}	
					} else {
						newPopulation[i][j] = population[i][j];
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
        }

	}
}
