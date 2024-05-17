import java.util.Arrays;
import java.util.Random;

public class Autoassociator {
	private static int weights[][];
	private int trainingCapacity;
	private CourseArray courses;

	public Autoassociator(CourseArray courses) {
		weights = new int[courses.length()][courses.length()];
		trainingCapacity = (int) (courses.length() * 0.139);
		this.courses = courses;
	}

	public int getTrainingCapacity() {
		return trainingCapacity;
	}

	/**
	 * Trains Hopfield network using the given pattern
	 * @param pattern
	 */
	public void training(int[] pattern) {
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

	public int unitUpdate(int slot) {
		Random rand = new Random();
		int index = rand.nextInt(weights.length);
		unitUpdate(slot,index);
		return index;
	}
	public void unitUpdate(int slot, int index) {
		int[] neurons = courses.getTimeSlot(slot);
		int sum = dotProduct(neurons, getColumn(getWeights(),index));

		if(neurons[index] != f_act(sum)) { //must update

			if(neurons[index] == -1){
				courses.setSlot(index, slot);
				System.out.println("Moved course "+ index +" to slot "+slot);
				return;
			}

			//try updating using Network
			for(int i = 0;i < neurons.length; i++){
				if(i == slot)
					continue;
				neurons = courses.getTimeSlot(i);

				if(f_act(dotProduct(neurons, getColumn(getWeights(),index))) == 1) {
					courses.setSlot(index, i);
					System.out.println("Using Network moved course "+ index +" to slot "+i);
					return;
				}
			}

			//turning 1 into -1. Picking which slot to turn to 1
			//if Network doesn't help, we update by selecting slot randomly
			Random rand = new Random();
			while(true){
				int randomSlot = rand.nextInt(neurons.length);
				if (slot != randomSlot){
					courses.setSlot(index, randomSlot);
					System.out.println("Randomly moved course "+ index +" to slot "+randomSlot);
					return;
				}
			}
		}
//		else
//			System.out.println("network says force in index should stay the same");
	}

	public void chainUpdate(int slot, int steps) {
		boolean[] updated = new boolean[weights.length];
		int index;

		Random rand = new Random();
		for(int i = 0;i < steps; i++){
			index = rand.nextInt(weights.length);
			if(!updated[index]) {
				unitUpdate(slot, index);
				updated[index] = true;
			}else
				i--;
		}
	}

	public void fullUpdate(int slot) {
		for(int i = 0;i < weights.length; i++) {
			unitUpdate(slot, i);
		}
	}

	/**
	 * Prints first n rows and cols of the weights matrix
	 * @param n
	 */
	public void printWeights(int n){
		int[][] w= getWeights();

		for(int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				System.out.print(w[i][j] + ", ");
			System.out.println();
		}
	}
}
