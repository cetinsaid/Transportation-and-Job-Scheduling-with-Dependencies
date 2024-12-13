import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;

public class UrbanInfrastructureDevelopment implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        for (int i = 0; i < projectList.size(); i++) {
            int[]arr = projectList.get(i).getEarliestSchedule();
            projectList.get(i).printSchedule(arr);

        }

        // TODO: YOUR CODE HERE
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();
        List<String> file = new ArrayList<>();
        try{
            Scanner scanner = new Scanner(new File(filename));
            List<Integer> projectI = new ArrayList<>();
            List<Integer> taskI1 = new ArrayList<>();
            List<String> descriptions = new ArrayList<>();
            List<String> projectd = new ArrayList<>();
            int k = 0;
            while (scanner.hasNextLine()){
                String line1 = scanner.nextLine();
                int a = line1.indexOf('<');

                String line = line1.replaceAll("\\s", "");
                if(line1.endsWith("</Name>")){
                    projectd.add(line1);
                }
                if(line1.endsWith("</Description>")){
                    descriptions.add(line1);
                }
                if(!line.equals("<Projects>")  && !line.equals("</Projects>") && !line.isEmpty()){
                    file.add(line);
                    if(line.equals("<Project>")){
                        projectI.add(k);
                    }
                    if(line.equals("<Task>")){
                        taskI1.add(k);
                    }
                    k++;
                }
            }
            String[] strs = file.toArray(new String[0]);
            int currt = 0;
            for (int i = 0; i < projectI.size(); i++) {
                StringBuilder projectID = new StringBuilder();
                List<Task> tasks = new ArrayList<>();
                for (int j = projectd.get(i).length() -8; projectd.get(i).charAt(j) != '>' ; j--){
                    projectID.append(projectd.get(i).charAt(j));
                }
                projectID.reverse();

                for (int m = currt; m < taskI1.size() ; m++) {
                    if(i < projectI.size()-1 && taskI1.get(m) > projectI.get(i+1)){
                        currt = m;
                        break;
                    }
                    int currTask = taskI1.get(m);
                    StringBuilder taskId = new StringBuilder();
                    StringBuilder tastDes = new StringBuilder();
                    StringBuilder taskDur = new StringBuilder();
                    List<Integer> dependencies = new ArrayList<>();
                    for (int j = descriptions.get(m).length() -15;  descriptions.get(m).charAt(j) != '>' ; j--) {
                        tastDes.append(descriptions.get(m).charAt(j));
                    }
                    tastDes.reverse();
                    for (int j = 8; strs[currTask+1].charAt(j) != '<'; j++) {
                        taskId.append(strs[currTask+1].charAt(j));
                    }
                    for (int j = 10; strs[currTask+3].charAt(j) != '<'; j++) {
                        taskDur.append(strs[currTask+3].charAt(j));
                    }
                    if(strs[currTask+4].length() == 14){
                        for (int j = currTask+5; !strs[j].equals("</Dependencies>") ; j++) {
                            StringBuilder depI = new StringBuilder();
                            for (int l = 17; strs[j].charAt(l) != '<' ; l++) {
                                depI.append(strs[j].charAt(l));
                            }
                            dependencies.add(Integer.parseInt(depI.toString()));
                        }
                    }
                    int a = Integer.parseInt(taskId.toString());
                    int b = Integer.parseInt(taskDur.toString());
                    tasks.add(new Task(a , tastDes.toString() , b , dependencies));
                }
                projectList.add(new Project(projectID.toString() , tasks));
            }

        }catch (FileNotFoundException e){
            System.out.println("file cannot be found");
        }






        // TODO: YOUR CODE HERE
        return projectList;
    }
}
