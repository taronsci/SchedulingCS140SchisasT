import java.io.*;
import java.util.StringTokenizer;

public class CourseArray {

	private Course elements[];
	private int period;

	public CourseArray(int numOfCourses, int numOfSlots) {
		period = numOfSlots;
		elements = new Course[numOfCourses];
		for (int i = 1; i < elements.length; i++)
			elements[i] = new Course();
	}

	public void readClashes(String filename) {
		try {
			BufferedReader file = new BufferedReader(new FileReader(filename));
			StringTokenizer line = new StringTokenizer(file.readLine());
			int count = line.countTokens(), i, j, k;
			int index[];
			while (count > 0) {
				if (count > 1) {
					index = new int[count];
					i = 0;
					while (line.hasMoreTokens()) {
						index[i] = Integer.parseInt(line.nextToken());
						i++;
					}

					for (i = 0; i < index.length; i++)
						for (j = 0; j < index.length; j++)
							if (j != i) {
								k = 0;
								while (k < elements[index[i]].clashesWith.size() && elements[index[i]].clashesWith.elementAt(k) != elements[index[j]])
									k++;
								if (k == elements[index[i]].clashesWith.size())
									elements[index[i]].addClash(elements[index[j]]);
							}
				}
				line = new StringTokenizer(file.readLine());
				count = line.countTokens();
			}
			file.close();
		} catch (Exception e) {
		}
	}

	public int length() {
		return elements.length;
	}

	public int status(int index) {
		return elements[index].clashSize();
	}

	public int slot(int index) {
		return elements[index].mySlot;
	}

	public void setSlot(int index, int newSlot) {
		elements[index].mySlot = newSlot;
	}

	public int maxClashSize(int index) {
		return elements[index] == null || elements[index].clashesWith.isEmpty() ? 0 : elements[index].clashesWith.size();
	}

	public int clashesLeft() {
		int result = 0;
		for (int i = 1; i < elements.length; i++)
			result += elements[i].clashSize();

		return result;
	}

	public void iterate(int shifts) {
		for (int index = 1; index < elements.length; index++) {
			elements[index].setForce();
			for (int move = 1; move <= shifts && elements[index].force != 0; move++) {
				elements[index].setForce();
				elements[index].shift(period);
			}
		}
	}

	/**
	 * Gives an array of 0:number of courses in each slot 1:number of clashes in that slot
	 * @param slot
	 * @return
	 */
	public int[] slotStatus(int slot){
		int[] result = new int[2];
		for(int i = 1;i < elements.length; i++)
			if(elements[i].mySlot == slot){
				result[0]++;
				result[1]+= elements[i].clashSize();
			}
		return result;
	}
	public void printSlotStatus(){
		for(int i=0;i<period;i++) {
			int[] a = slotStatus(i);
			System.out.println(i+ " "+ a[0]+" "+ a[1]);
		}
	}
	/**
	 * Returns the specified Timeslot as an int array of -1 and 1 values.
	 * @param index timeslot
	 * @return array of values
	 */
	public int[] getTimeSlot(int index){
		int[] ans = new int[elements.length];
		for(int i = 0;i < elements.length;i++) {
			if (elements[i] != null && slot(i) == index)
				ans[i] = 1;
			else
				ans[i] = -1;
		}
			return ans;
	}
//	public void printTimeSlot(){
//		int[] a = getTimeSlot(0);
//		for(int i = 0;i < a.length;i++)
//			System.out.println(i + "\t"+ a[i]);
//	}
	public void printResult() {
		for (int i = 1; i < elements.length; i++)
			System.out.println(i + "\t" + elements[i]);
	}
}
