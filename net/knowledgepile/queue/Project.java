package net.knowledgepile.queue;

import java.util.ArrayList;
import java.util.Collections;

public class Project {
    // TODO add newline
    // TODO chain constructors
    // newline for ease of change
    private static String newline = "\n";
    // title of the project
    private String title;
    // tasks associated with the project
    private ArrayList<Task> tasks;
    // priority for the project
    private boolean isFrontBurner;

    // constructor if the user specifies a title
    public Project(String title) {
        tasks = new ArrayList<Task>();
        this.title = title.trim();
        // default to high priority
        this.isFrontBurner = true;
    }
    
    // default constructor
    public Project() {
        tasks = new ArrayList<Task>();
        // add new tasks to 'miscellaneous' project
        this.title = "miscellaneous";
        this.isFrontBurner = true;
    }

    // the toString() method gets the priority of the project,
    // and patches it together with the title and headings
    public String toString() {
        String burner = isFrontBurner ? "front" : "back";
        String str = "Project:  " + this.title;
        str = str + newline + "Burner:  " + burner;
        for (Task t : tasks) {
            str = str + newline + t;
        }
        str += newline;
        return str;
    }

    // returns all tasks associated with a project
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    // returns the tasks associated with a project that are
    // labeled as being quick and important
    public ArrayList<Task> getQuickTasks() {
        ArrayList<Task> quickTasks = new ArrayList<Task>();
        for (Task t : tasks) {
            if (t.isQuick() && t.isFirstOrder()) {
                quickTasks.add(t);
            }
        }
        return quickTasks;
    }

    // returns tasks associated with a project that are
    // labeled as being slow and important
    public ArrayList<Task> getSlowTasks() {
        ArrayList<Task> slowTasks = new ArrayList<Task>();
        for (Task t : tasks) {
            if (!t.isQuick() && t.isFirstOrder()) {
                slowTasks.add(t);
            }
        }
        return slowTasks;
    }

    // the title of the project
    public String getTitle() {
        return title;
    }

    // change the project to high priority (front burner)
    public void setFrontBurner() {
        this.isFrontBurner = true;
    }

    // change the project to back priority (back burner)
    public void setBackBurner() {
        this.isFrontBurner = false;
    }

    // return the priority of the project
    public boolean isFrontBurner() {
        return isFrontBurner;
    }

    // add a task to the project
    public void addTask(Task task) {
        tasks.add(task);
    }

    // remove a task from the project
    public void removeTask(Task task) {
        tasks.remove(task);
    }

    // find out if the project has tasks associated with it
    public boolean hasTasks() {
        if (tasks.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    // return whether this project matches another project
    // used to check for duplicates when a new project is added
    public boolean equals(Object obj) {
        // if they are references to the same object then they are equal
        if (this == obj) return true;
        // if the object handed in isn't a project then they can't be equal
        if (!(obj instanceof Project)) return false;
        // cast the object passed in to a project
        Project that = (Project) obj;
        // if the titles are the same then we will consider them to be the same
        return this.title.equals(that.title) ? true : false;
    }

    // sort the tasks based on the ordering of tasks
    public void sortTasks() {
        Collections.sort(tasks);
    }
}