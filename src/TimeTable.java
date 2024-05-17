import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;


public class TimeTable extends JFrame implements ActionListener {

	private JPanel screen = new JPanel(), tools = new JPanel();
	private JButton tool[];
	private JTextField field[];
	private CourseArray courses;
	private Color CRScolor[] = {Color.RED, Color.GREEN, Color.BLACK};
	private Autoassociator a;
	public int count = 0;


	public TimeTable() {
		super("Dynamic Time Table");
		setSize(550, 750);
		setLayout(new FlowLayout());

		screen.setPreferredSize(new Dimension(400, 800));
		add(screen);

		setTools();
		add(tools);

		setVisible(true);
	}

	public void setTools() {
		String capField[] = {"Slots:", "Courses:", "Clash File:", "Iters:", "Shift:"};
		field = new JTextField[capField.length];

		String capButton[] = {"Load", "Start", "Step", "Print", "Exit", "Continue", "Train"};
		tool = new JButton[capButton.length];

		tools.setLayout(new GridLayout(2 * capField.length + capButton.length, 1));

		for (int i = 0; i < field.length; i++) {
			tools.add(new JLabel(capField[i]));
			field[i] = new JTextField(5);
			tools.add(field[i]);
		}

		for (int i = 0; i < tool.length; i++) {
			tool[i] = new JButton(capButton[i]);
			tool[i].addActionListener(this);
			tools.add(tool[i]);
		}

		field[0].setText("22"); //19
		field[1].setText("190"); //461
		field[2].setText("ear-f-83.stu"); //kfu-s-93.stu
		field[3].setText("1");
	}

	public void draw() {
		Graphics g = screen.getGraphics();
		int width = Integer.parseInt(field[0].getText()) * 10;
		for (int courseIndex = 1; courseIndex < courses.length(); courseIndex++) {
			g.setColor(CRScolor[courses.status(courseIndex) > 0 ? 0 : 1]);
			g.drawLine(0, courseIndex, width, courseIndex);
			g.setColor(CRScolor[CRScolor.length - 1]);
			g.drawLine(10 * courses.slot(courseIndex), courseIndex, 10 * courses.slot(courseIndex) + 10, courseIndex);
		}
	}

	private int getButtonIndex(JButton source) {
		int result = 0;
		while (source != tool[result]) result++;
		return result;
	}

	public void actionPerformed(ActionEvent click) {
		int min, step, clashes;
		switch (getButtonIndex((JButton) click.getSource())) {
			case 0:
				int slots = Integer.parseInt(field[0].getText());
				courses = new CourseArray(Integer.parseInt(field[1].getText()) + 1, slots); //ear-(190,22), kfu-(461,19)
				courses.readClashes(field[2].getText());
				draw();
				break;
			case 1:
				min = Integer.MAX_VALUE;
				step = 0;
				for (int i = 1; i < courses.length(); i++) courses.setSlot(i, 0);

				for (int iteration = 1; iteration <= Integer.parseInt(field[3].getText()); iteration++) {
					courses.iterate(Integer.parseInt(field[4].getText()));
					draw();
					clashes = courses.clashesLeft();
					if (clashes < min) {
						min = clashes;
						step = iteration;
					}
				}
				System.out.println("Shift = " + field[4].getText() + "\tMin clashes = " + min + "\tat step " + step);
				setVisible(true);
				break;
			case 2:
				courses.iterate(Integer.parseInt(field[4].getText()));
				draw();
				break;
			case 3:
				courses.printSlotStatus();
				System.out.println();
//				System.out.println("Exam\tSlot\tClashes");
//				for (int i = 1; i < courses.length(); i++)
//					System.out.println(i + "\t" + courses.slot(i) + "\t" + courses.status(i));
				break;
			case 4:
				System.exit(0);
			case 5:
//				Random rand = new Random();
//				int index = rand.nextInt(Integer.parseInt(field[1].getText()));
//				int slot = rand.nextInt(Integer.parseInt(field[0].getText()));
				for (int i = 0; i < 22; i++) {
					a.fullUpdate(i);
				}

				draw();
			case 6:
				trainingEAR();
		}

	}

	public void trainingEAR(){
		//19-1445, 13-610, 21-842
		if(a == null)
			a = new Autoassociator(courses);

		if(count == 0) {
			int[] tr20 = {1, 4, 6, 7, 8, 17, 18, 20, 21};
			for (int i : tr20)
				a.training(courses.getTimeSlot(i));
			count++;
			a.printWeights(10);
		}else if(count == 1){
			int[] tr20 = {2,4,11,13,15,19};
			for (int i : tr20)
				a.training(courses.getTimeSlot(i));
			count++;
			a.printWeights(10);
		}else if(count == 2){
			int[] tr20 = {0,1,5,6,7,8,10,11,12,21};
			for (int i : tr20)
				a.training(courses.getTimeSlot(i));
			count++;
			a.printWeights(10);
		}
	}

	public static void main(String[] args) {
		new TimeTable();
	}
}
