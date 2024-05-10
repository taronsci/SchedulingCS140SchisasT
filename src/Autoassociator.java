public class Autoassociator {
	private int weights[][];
	private int trainingCapacity;

	public Autoassociator(CourseArray courses) {
		// TO DO
		// creates a new Hopfield network with the same number of neurons
		// as the number of courses in the input CourseArray
		weights = new int[courses.length()][courses.length()];
		trainingCapacity = (int) (courses.length() * 0.139);
	}

	public int getTrainingCapacity() {
		// TO DO
		return trainingCapacity;
	}

	/**
	 * Trains Hopfield network using the given pattern
	 * @param pattern
	 */
	public void training(int pattern[]) {
		// TO DO, pattern = getTimeSlot()
		for(int i = 0; i < weights.length; i++){
			for(int j = 0; j < weights.length; j++){
				if(i == j)
					weights[i][j] = 0;
				else if(pattern[i] * pattern[j] == 1)
					weights[i][j] += 1;
				else
					weights[i][j] -=1;
			}
		}
	}

	public int unitUpdate(int neurons[]) {
		// TO DO
		// implements a single update step and
		// returns the index of the randomly selected and updated neuron

		return 0;
	}

	public void unitUpdate(int neurons[], int index) {
		// TO DO
		// implements the update step of a single neuron specified by index
	}

	public void chainUpdate(int neurons[], int steps) {
		// TO DO
		// implements the specified number od update steps
	}

	public void fullUpdate(int neurons[]) {
		// TO DO
		// updates the input until the final state achieved
	}
}
