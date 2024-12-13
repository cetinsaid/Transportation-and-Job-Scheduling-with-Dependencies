import java.io.Serializable;
import java.util.*;

public class Project implements Serializable {
    static final long serialVersionUID = 33L;
    private final String name;
    private final List<Task> tasks;
    boolean[] marked;
    int[]dpT;

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
        marked = new boolean[tasks.size()];
        dpT = new int[tasks.size()];
    }

    /**
     * @return the total duration of the project in days
     */
    public int getProjectDuration() {
        // TODO: YOUR CODE HERE
        for (int i = 0; i < tasks.size(); i++) {
            if(!marked[i]){
                dfs(i);
            }
        }
        return tasks.get(dpT.length-1).getDuration() + dpT[dpT.length-1];
    }
    public void dfs(int id){
        marked[id]  = true;
        for (int i = 0; i < tasks.get(id).getDependencies().size(); i++) {
            if(!marked[tasks.get(id).getDependencies().get(i)]){
                dfs(tasks.get(id).getDependencies().get(i));
            }
            if((dpT[tasks.get(id).getDependencies().get(i)] + tasks.get(tasks.get(id).getDependencies().get(i)).getDuration()) > dpT[id] ){
                dpT[id] = dpT[tasks.get(id).getDependencies().get(i)] + tasks.get(tasks.get(id).getDependencies().get(i)).getDuration();
            }
        }

    }
    public int[] getEarliestSchedule() {
        for (int i = 0; i < tasks.size(); i++) {
            if(!marked[i]){
                dfs(i);
            }
        }
        // TODO: YOUR CODE HERE

        return dpT;
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return tasks;
    }
    public void printer(){
        for (int i = 0; i < tasks.size(); i++) {
            System.out.print(tasks.get(i).getTaskID() + " " + tasks.get(i).getDescription() + " " + tasks.get(i).getDuration() + " Dependencies: " );
//            System.out.println();
            for (int j = 0; j < tasks.get(i).getDependencies().size(); j++) {
                System.out.print( tasks.get(i).getDependencies().get(j) + " ");
            }
            System.out.println();
        }
    }

    public static void printlnDash(int limit, char symbol) {
        for (int i = 0; i < limit; i++) System.out.print(symbol);
        System.out.println();
    }

    /**
     * Some free code here. YAAAY! 
     */
    public void printSchedule(int[] schedule) {
        int limit = 65;
        char symbol = '-';
        printlnDash(limit, symbol);
        System.out.println(String.format("Project name: %s", name));
        printlnDash(limit, symbol);

        // Print header
        System.out.println(String.format("%-10s%-45s%-7s%-5s","Task ID","Description","Start","End"));
        printlnDash(limit, symbol);
        for (int i = 0; i < schedule.length; i++) {
            Task t = tasks.get(i);
            System.out.println(String.format("%-10d%-45s%-7d%-5d", i, t.getDescription(), schedule[i], schedule[i]+t.getDuration()));
        }
        printlnDash(limit, symbol);
        System.out.println(String.format("Project will be completed in %d days.", tasks.get(schedule.length-1).getDuration() + schedule[schedule.length-1]));
        printlnDash(limit, symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;

        int equal = 0;

        for (Task otherTask : ((Project) o).tasks) {
            if (tasks.stream().anyMatch(t -> t.equals(otherTask))) {
                equal++;
            }
        }

        return name.equals(project.name) && equal == tasks.size();
    }

}
