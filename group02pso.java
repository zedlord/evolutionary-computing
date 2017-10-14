import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.lang.Math;
import java.util.Properties;
import java.util.HashSet;
import java.util.Set;

public class group02pso implements ContestSubmission
{
	Random rand;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;

	public group02pso()
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

	public void run()
	{

		// Set parameters
		double weightCurrent = 1;
		double weightIndividual = 2;
		double weightSocial = weightIndividual;
		int populationSize = 20;
		double velocityMax = 10;

		// Init population arrays
		double[][] population = new double[populationSize][10];
		double[][] velocity = new double[populationSize][10];
		double[] fitnessArray = new double[populationSize];

		double[][] personalBestPosition = new double[populationSize][10];
		double[] personalBestFitness = new double[populationSize];
		
		double[] globalBestPosition = new double[10];
		double globalBestFitness = 0.0;

		System.out.println("weightCurrent: " + weightCurrent);
		System.out.println("weightIndividual: " + weightIndividual);
		System.out.println("weightSocial: " + weightSocial);
		System.out.println("populationSize: " + populationSize);
		System.out.println("velocityMax: " + velocityMax);

		// Run your algorithm here
        int evals = 0;

        // init population
		for(int i = 0; i < populationSize; i++){
			for(int j = 0; j < 10; j++){
				population[i][j] = -5 + (5 - -5) * rand.nextDouble();
			}
			fitnessArray[i] = (double) evaluation_.evaluate(population[i]);
			evals++;
			personalBestFitness[i] = fitnessArray[i];
			personalBestPosition[i] = population[i];

			if(personalBestFitness[i] > globalBestFitness){
				globalBestFitness = personalBestFitness[i];
				globalBestPosition = population[i];
			}
		}

		// init velocity
		for(int i = 0; i < populationSize; i++){
			for(int j = 0; j < 10; j++){
				velocity[i][j] = -velocityMax + 2*velocityMax*rand.nextDouble();
			}
		}

        // calculate fitness
        while(evals<evaluations_limit_){
			//Loop through all agents
			for(int i = 0; i < populationSize; i++){
				// Compute new position of agent

				// Draw random components (vector wise?)
				double uniDraw1 = rand.nextDouble();
				double uniDraw2 = rand.nextDouble();

				for(int j = 0; j < 10; j++){

					// compute new position and velocity
					velocity[i][j] = weightCurrent * velocity[i][j] +  weightIndividual*uniDraw1*(personalBestPosition[i][j] - population[i][j]) + weightSocial*uniDraw2*(globalBestPosition[j]-population[i][j]);
					velocity[i][j] = Math.max(-velocityMax, Math.min(velocityMax, velocity[i][j]));
					population[i][j] = population[i][j] + velocity[i][j];

					// what if a member leaves the search space?
					if(population[i][j] < -5){
						population[i][j] = -5; // Nearest method
						// population[i][j] = -5 + 10*rand.nextDouble();  // random 
						 
						// velocity[i][j] = 10*rand.nextDouble(); // random back method
						// velocity[i][j] = -velocityMax + 2*velocityMax*rand.nextDouble(); // random
						// velocity[i][j] = 0; // absorb method
						velocity[i][j] = -.5*velocity[i][j]; // deterministic back method 
					}
					if(population[i][j] > 5){
						population[i][j] = 5; // Nearest method
						// population[i][j] = -5 + 10*rand.nextDouble(); // random
						
						// velocity[i][j] = -10*rand.nextDouble(); // random back method
						// velocity[i][j] = -velocityMax + 2*velocityMax*rand.nextDouble(); // random
						// velocity[i][j] = 0; // absorb method
						velocity[i][j] = -.5*velocity[i][j]; // deterministic back method
					}

					// System.out.println(population[i][j] + " " + velocity[i][j]);

				}

				// evaluate new position
				double newFitness = (double) evaluation_.evaluate(population[i]);
				evals++;
				

				if (newFitness > personalBestFitness[i]){
					personalBestFitness[i] = newFitness;
					personalBestPosition[i] = population[i];

					if (newFitness > globalBestFitness){
						globalBestFitness = newFitness;
						globalBestPosition = population[i];
					}
				}

				fitnessArray[i] = newFitness;
			}
			// System.out.print("[ ");
			// for(int j = 0; j < 10; j++){
			// 	System.out.print(globalBestPosition[j] + " ");
			// }
			// System.out.print("] \n");
    	}
	}
}
