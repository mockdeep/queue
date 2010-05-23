package net.knowledgepile.queue;

// our comparable Task object
public class Task implements Comparable<Task> {
    private String title;
    // the order each task should be executed
    private int order;
    // based on how long the task will take, anything less than a few 
    // minutes one might as well go ahead and do
    private boolean isQuick;
    
    // constructor where a task ordering is specified
    // we default the Tasks to being Quick in our constructors so that if there
    // is a mistake the user will have the task front and center.  They can 
    // alter it manually.
    public Task(String title, int order) {
        this.order = order;
        this.title = title.trim();
        this.isQuick = true;
    }

    // constructor, just title
    public Task(String title) {
        this.title = title.trim();
        this.order = 1;
        this.isQuick = true;
    }

    // return the title of the task
    public String getTitle() {
        return title;
    }

    // return a string representing the task, its speed, and order
    public String toString() {
        String str = "    ";
        String speed = this.isQuick() ? "Q" : "S";
        for (int i = order; i > 0; i--) {
            str += "*";
        }
        return str + " " + speed + ": " + title;
    }

    // change the title of a task
    public void setTitle(String title) {
        this.title = title;
    }

    // change the order of execution for the task
    public void setOrder(int order) {
        this.order = order;
    }

    // return the order of execution for a task
    public int getOrder() {
        return order;
    }

    // change a task to being quick
    public void setQuick() {
        this.isQuick = true;
    }
    
    // change a task to being slow
    public void setSlow() {
        this.isQuick = false;
    }

    // return whether the task is quick or not
    public boolean isQuick() {
        return isQuick;
    }
    
    // return whether the task is first order or not
    public boolean isFirstOrder() {
        return order == 1 ? true : false;
    }

    // if two tasks have the same title then we consider them to be the same
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Task)) return false;
        Task that = (Task) obj;
        return this.title.equalsIgnoreCase(that.title) ? true : false;
    }

    // tasks are sorted based on their order of execution and whether they
    // are quick or not
    public int compareTo(Task that) {
        if (this.order > that.order) {
            return 1;
        } else if (this.order < that.order) {
            return -1;
        } else {
            if (this.isQuick()) {
                return -1;
            }
            return 0;
        }
    }
}