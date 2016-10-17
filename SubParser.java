import java.util.Scanner;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

public class SubParser {
	public static final String[] ERROR_MSG = {"This requires 1 argument"};
	private static LineSet lines;


	public static void main(String[] args) {
			String str;
			String[] lineParts;

			try {
				if (args.length < 1) {
					System.err.println(ERROR_MSG[0]);
				} else {
					File f = new File(args[0]);
					Scanner scan = new Scanner(f);
					lines = new LineSet();

					while (scan.hasNext()) {
						str = scan.nextLine();
						
						lineParts = str.split(",");

						if (lineParts.length == 10 
								&& lineParts[0].equals("Dialogue: 0")) {
							lines.addLine(lineParts, false);
							
						} else if (lineParts.length == 10 
								&& lineParts[0].equals("Dialogue: 1")) {
							lines.addLine(lineParts,true);
						}
					}

					scan.close();

					lines.printLines(System.out);
				}
			} catch (Exception e) {
				 System.err.println(e.toString());
			}
	}
}

class LineSet {
	private ArrayList<Line> lines;
	public static final String[] STRINGS = {"var trackPos = argument0;\n"
													+ "var newString;\n\n"
													, " {pointer=max(-1,pointer-2)}"};


	public LineSet() {
		lines = new ArrayList<>();
	}

	public void addLine(String[] ln, boolean add) {
		Line temp = new Line();

		temp.setText(ln[9]);
		temp.setStartTime(ln[1]);
		temp.setEndTime(ln[2]);
		temp.setAdd(add);

		lines.add(temp);
	}

	public void printLines(PrintStream out) {
		int i;
		Line line;

		out.print(LineSet.STRINGS[0]);

		for (i = 0; i < lines.size(); i++) {
			line = lines.get(i);

			if (line.getAdd()) {
				out.printf(Line.FORMAT_ADD, line.getStartTime(),
					line.getEndTime(),line.getText());
			} else {
				out.printf(Line.FORMAT_REPLACE, line.getStartTime(),
					line.getEndTime(),line.getText());
			}
		}

		out.print(LineSet.STRINGS[1]);
	}
}

class Line {
	private boolean add = false; // whether or not the line is appended
	private double startTime, endTime; // in seconds
	private String txt;

	public static final String FORMAT_REPLACE = "if "
									+ "(trackPos >= %.2f && trackPos < %.2f)"
									+ " {\n   newString = %s;"
									+	"\n   if (string_pos(newString,str) == 0) "
									+ "{\n      str = newString;"
									+ "\n      pointer = 0;\n   }\n} else ";

	public static final String FORMAT_ADD = "if "
									+ "(trackPos >= %.2f && trackPos < %.2f)"
									+ " {\n   newString = %s;"
									+	"\n   if (string_pos(newString,str) == 0) "
									+ "{\n      str += newString;"
									+ "\n   }\n} else ";

	public void setText(String str) {
		txt = "\"" + str + "\"";
	}

	public double getStartTime() {
		return startTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public String getText() {
		return txt;
	}

	public boolean getAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public void setStartTime(String str) {
		String[] split1 = str.split(":"); // separates by decimal

		startTime = 0;
		startTime += Double.parseDouble(split1[2]); // seconds
		startTime += 60 * Double.parseDouble(split1[1]); // minutes
		startTime += 3600 * Double.parseDouble(split1[0]); // hours
	}

	public void setEndTime(String str) {
		String[] split1 = str.split(":"); // separates by decimal

		endTime = 0;
		endTime += Double.parseDouble(split1[2]); // seconds
		endTime += 60 * Double.parseDouble(split1[1]); // minutes
		endTime += 3600 * Double.parseDouble(split1[0]); // hours
	}
}