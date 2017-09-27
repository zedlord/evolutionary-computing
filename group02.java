import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.HashSet;
import java.util.Set;

public class group02 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;

	public group02()
	{
		rnd_ = new Random();
	}

	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
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

    private int[] chooseRandomAgents(int pSize, int agentExclude){
		Random rand = new Random(); // use rnd_ instead? or set seed
		int[] randomAgents = new int[3];
		Set<Integer> set = new HashSet<Integer>();

		set.add(agentExclude);
		while (set.size() < 4){
			set.add(rand.nextInt(pSize));
		}
		set.remove(agentExclude);

		int i = 0;
		for (Integer val : set) randomAgents[i++] = val;

		return randomAgents;
	}

	public void run()
	{
		double[] fitnessArray = new double[10];
		Random rand = new Random(); // use rnd_ instead? or set seed 
		double crossoverRate = 0.5;
		double differentialRate = 1.0;
		int populationSize = 10;
		// Run your algorithm here
        int evals = 0;
        // init population
		double[][] population = new double[populationSize][10];
		for(int i = 0; i < populationSize; i++){
			for(int j = 0; j < 10; j++){
				population[i][j] = -5 + (5 - -5) * rand.nextDouble();
			}
		}

        // calculate fitness
        while(evals<evaluations_limit_){

			double[][] newPopulation = new double[populationSize][10];
			//Loop through all agents
			for(int i = 0; i < populationSize; i++){

				//Create array to choose 3 random agents from the population
				int[] randomAgents = chooseRandomAgents(populationSize, i);
				int randomIndex = rand.nextInt(10);

				// Compute new position of agent
				for(int j = 0; j < 10; j++){
					double uniDistrNumber = 0 + (1 - 0) * rand.nextDouble();
					if(uniDistrNumber < crossoverRate || j == randomIndex){
						newPopulation[i][j] = population[randomAgents[0]][j] + differentialRate * (population[randomAgents[1]][j] - population[randomAgents[2]][j]);
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

            // Select survivors
        }

	}
}
