import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HyperloopTrainNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageTrainSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;;
    public int numTrainLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<TrainLine> lines;

    /**
     * Method with a Regular Expression to extract integer numbers from the fileContent
     * @return the result as int
     */
    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("\\s*" + varName + "\\s*=\\s*\"(.*)\"");
        Matcher m = p.matcher(fileContent);
        m.find();
        // TODO: Your code goes here
        return m.group(1);
    }

    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("\\s*" + varName + "\\s*=\\s*([0-9]+.[0-9]+|[0-9]+)");
        Matcher m = p.matcher((fileContent));
        m.find();
        // TODO: Your code goes here
        return Double.parseDouble(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {
        Pattern p1 = Pattern.compile("\\s*" + varName + "\\s*=\\s*\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*\\)");
        Matcher m = p1.matcher(fileContent);
        m.find();
        Point p = new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
        // TODO: Your code goes here
        return p;
    }
    public TrainLine getTrainLine(String fileContent){
        StringBuilder nameh = new StringBuilder();
        StringBuilder pointsh = new StringBuilder();
        int a = 0;
        for (int i = 0; fileContent.charAt(i) != '\n'; i++) {
            nameh.append(fileContent.charAt(i));
            a=i;
        }
        for (int i = a+2; i <fileContent.length() ; i++) {
            pointsh.append(fileContent.charAt(i));
        }
        List<String> test = new ArrayList<>();
        List<Integer> openI = new ArrayList<>();
        List<Integer> closeI = new ArrayList<>();
        for (int i = 0; i < pointsh.length(); i++) {
            if(pointsh.charAt(i) == '(') openI.add(i);
            if(pointsh.charAt(i) == ')') closeI.add(i);
        }
        for (int i = 0; i < openI.size(); i++) {
            String x = pointsh.substring(0, openI.get(0)) + pointsh.substring(openI.get(i) , closeI.get(i)+1);
            test.add(x);
        }
        List<Station> st = new ArrayList<>();
        for (int i = 0; i < test.size(); i++) {
            st.add(new Station(getPointVar("train_line_stations" , test.get(i)) , getStringVar("train_line_name" , nameh.toString())+ " Line Station " + (i+1) ));
        }
        TrainLine tl = new TrainLine(getStringVar("train_line_name" , nameh.toString()) , st);
        return tl;
    }

    /**
     * Function to extract the train lines from the fileContent by reading train line names and their 
     * respective stations.
     * @return List of TrainLine instances
     */
    public List<TrainLine> getTrainLines(String fileContent) {
        List<TrainLine> trainLines = new ArrayList<>();
        int a =0;
        List<String> str = new ArrayList<>();
        for (int i = 0; i <fileContent.length() ; i++) {
            if(fileContent.charAt(i) == '\n'){
                str.add(fileContent.substring(a, i));
                a = i+1;
            }
        }
        for (int i = 4; i < str.size(); i+=2) {
            String x = str.get(i) + "\n" + str.get(i+1);
            trainLines.add(getTrainLine(x));
        }
        

        // TODO: Your code goes here

        return trainLines;
    }

    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */
    public void readInput(String filename) {
        try{
            Scanner sc = new Scanner(new File(filename));
            List<String> list = new ArrayList<>();
            while (sc.hasNextLine()){
                list.add(sc.nextLine());
            }
            String[] strs = list.toArray(new String[0]);
            numTrainLines = getIntVar("num_train_lines" , strs[0]);
            startPoint = new Station(getPointVar("starting_point" , strs[1]) , "Starting Point");
            destinationPoint = new Station(getPointVar("destination_point" , strs[2]) , "Final Destination");
            double test = getDoubleVar("average_train_speed" , strs[3]);
            averageTrainSpeed = test*1000/60;
            StringBuilder k1 = new StringBuilder();
            for (int i = 0; i < strs.length ; i++) {
                k1.append(strs[i]).append("\n");
            }
            lines = getTrainLines(String.valueOf(k1));

        }catch (FileNotFoundException e){
            System.out.println("file not found");
        }


        // TODO: Your code goes here

    }
}