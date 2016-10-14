import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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

                        if (lineParts.length == 10 && lineParts[0].equals("Dialogue: 0")) {
                            lines.addLine(lineParts);
                        }
                    }

                    scan.close();

                    System.out.println(lines);
                }
            } catch (Exception e) {
                 System.err.println(e.toString());
            }
    }
}

class LineSet {
    private ArrayList<Line> lines;
    public static final String[] STRINGS = {"var trackPos = argument0;\n\n"
                                        , " {}"};

    public LineSet() {
        lines = new ArrayList<>();
    }

    public void addLine(String[] ln) {
        Line temp = new Line();

        temp.setText(ln[9]);
        temp.setStartTime(ln[1]);
        temp.setEndTime(ln[2]);
        System.out.println(temp);
        lines.add(temp);
    }
    
    public String toString() {
        String out = STRINGS[0];
        int i;
        
        for (i = 0; i < lines.size() - 1; i++) {
            out += lines.get(i);
        }
        
        out += STRINGS[1];
        
        return out;
    }
}

class Line {
    private double startTime, endTime; // in seconds
    private String txt;
    
    private static final String[] strings =  {"if (trackPos >= ", " && " 
            + "trackPos < ", ") {\n\tif (linePos == 0) {\n\t\tstr = \"" 
            , "\";\n\t}\n} else "};

    // constant strings used when converting to a string
    public String toString() {
        String out = "";
        
        out += strings[0] + startTime + strings[1] + endTime + strings[2] + txt
                + strings[3];

        return out;
    }

    public void setText(String str) {
        txt = str;
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