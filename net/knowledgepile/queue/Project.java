package net.knowledgepile.queue;

import java.util.ArrayList;
import java.util.Collections;

public class Project {
    // TODO add newline
    // TODO chain constructors
    private static String newline = "\n";
    private String title;
    private ArrayList<Task> tasks;
    private boolean isFrontBurner;

    public Project(String title) {
        tasks = new ArrayList<Task>();
        this.title = title.trim();
        this.isFrontBurner = true;
    }
    
    public Project() {
        tasks = new ArrayList<Task>();
        this.title = "miscellaneous";
        this.isFrontBurner = true;
    }

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

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<Task> getQuickTasks() {
        ArrayList<Task> quickTasks = new ArrayList<Task>();
        for (Task t : tasks) {
            if (t.isQuick() && t.isFirstOrder()) {
                quickTasks.add(t);
            }
        }
        return quickTasks;
    }

    public ArrayList<Task> getSlowTasks() {
        ArrayList<Task> slowTasks = new ArrayList<Task>();
        for (Task t : tasks) {
            if (!t.isQuick() && t.isFirstOrder()) {
                slowTasks.add(t);
            }
        }
        return slowTasks;
    }

    public String getTitle() {
        return title;
    }

    public void setFrontBurner() {
        this.isFrontBurner = true;
    }

    public void setBackBurner() {
        this.isFrontBurner = false;
    }

    public boolean isFrontBurner() {
        return isFrontBurner;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public boolean hasTasks() {
        if (tasks.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Project)) return false;
        Project that = (Project) obj;
        return this.title.equals(that.title) ? true : false;
    }

    public void sortTasks() {
        Collections.sort(tasks);
    }
}
