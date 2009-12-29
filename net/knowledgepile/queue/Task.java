package net.knowledgepile.queue;

public class Task implements Comparable<Task> {
    private String title;
    private int order;
    private boolean isQuick;
    
    public Task(String title, int order) {
        this.order = order;
        this.title = title.trim();
        this.isQuick = true;
    }

    public Task(String title) {
        this.title = title.trim();
        this.order = 1;
        this.isQuick = true;
    }

    public String getTitle() {
        return title;
    }

    public String toString() {
        String str = "    ";
        String speed = this.isQuick() ? "Q" : "S";
        for (int i = order; i > 0; i--) {
            str += "*";
        }
        return str + " " + speed + ": " + title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setQuick() {
        this.isQuick = true;
    }
    
    public void setSlow() {
        this.isQuick = false;
    }

    public boolean isQuick() {
        return isQuick;
    }
    
    public boolean isFirstOrder() {
        return order == 1 ? true : false;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Task)) return false;
        Task that = (Task) obj;
        return this.title.equalsIgnoreCase(that.title) ? true : false;
    }

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