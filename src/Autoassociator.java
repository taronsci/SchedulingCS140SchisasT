import java.util.Arrays;
import java.util.Random;

public class Autoassociator {
	private static int weights[][];
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

	/**
	 * Returns clone of weights matrix
	 * @return
	 */
	public static int[][] getWeights(){
		return weights.clone();
	}

	/**
	 * calculates output value of F_act function given the sum
	 * @param sum Sum(w_jk * x_j(t-1))
	 * @return x_k(t)
	 */
	private static int f_act(int sum){
		return sum >= 0 ? 1: -1;
	}

	/**
	 * Calculates dot product of two int arrays
	 * @param x_j neurons array
	 * @param w_jk weights array
	 * @return
	 */
	private static int dotProduct(int[] x_j,int[] w_jk){
		int ans = 0;
		for (int i = 0; i < x_j.length; i++) {
			ans += x_j[i] * w_jk[i];
		}
		return ans;
	}

	/**
	 * Returns k-th column of the weights matrix
	 * @param w_jk weights matrix
	 * @param k column number
	 * @return
	 */
	private static int[] getColumn(int[][] w_jk, int k){
		return Arrays.stream(w_jk).mapToInt(ints -> ints[k]).toArray();
	}

	public int unitUpdate(int neurons[]) {
		// TO DO
		// implements a single update step and
		// returns the index of the randomly selected and updated neuron
		Random rand = new Random();
		int index = rand.nextInt(neurons.length);
		unitUpdate(neurons,index);
		return index;
	}

	public static void unitUpdate(int neurons[], int index) {
		// TO DO
		// implements the update step of a single neuron specified by index
		int sum = dotProduct(neurons, getColumn(getWeights(),index));
		if(neurons[index] != f_act(sum)) {
			int a = neurons[index];

			boolean change = false;
			for(int i = 0;i < neurons.length;i++){
				if(i == index)
					continue;

				if(neurons[i] != a)
					if(f_act(dotProduct(neurons, getColumn(getWeights(),i))) == a) {
						neurons[i] = a;
						change = true;
					}
			}
			if(!change)
				if(index != 0)
					neurons[0] = -neurons[0];
				else
					neurons[1] = -neurons[1];

			neurons[index] = f_act(sum);
		}
	}

	public void chainUpdate(int neurons[], int steps) {
		// TO DO
		// implements the specified number od update steps
		boolean[] updated = new boolean[neurons.length];
		int index;

		Random rand = new Random();
		for(int i = 0;i < steps; i++){
			index = rand.nextInt(neurons.length);
			if(!updated[index]) {
				unitUpdate(neurons, index);
				updated[index] = true;
			}else
				i--;
		}
	}

	public void fullUpdate(int neurons[]) {
		// TO DO
		// updates the input until the final state achieved
		for(int i = 0;i< neurons.length;i++)
			unitUpdate(neurons, i);
	}

	/**
	 * Prints first n rows and cols of the weights matrix
	 * @param n
	 */
	public void printWeights(int n){
		int[][] w= getWeights();

		for(int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				System.out.print(w[i][j] + " ");
			System.out.println();
		}
	}
}
