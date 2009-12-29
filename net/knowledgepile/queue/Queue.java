package net.knowledgepile.queue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

public class Queue {
    private ArrayList<Project> projects;

    public static void main(String[] args) {
        Queue queue = new Queue();
        queue.projects = QueueIO.readQ();
        queue.parseArgs(args);
        QueueIO.writeQ(queue.projects);
        QueueIO.writeTasks(queue.getTasks());
    }

    @SuppressWarnings("unchecked")
    private void parseArgs(String[] args) {
        String options = "Options are 'addProject', 'addTask', 'removeProject, 'removeTask', 'done', 'change', and 'review'";
        if (args.length == 0) {
            System.out.println("Enter an argument, e.g.: Q addProject");
            System.out.println(options);
        } else {
            try {
                Class c = this.getClass();
                Method m = c.getMethod(args[0], new Class[0]);
                m.invoke(this);
            } catch (Exception e) {
                System.out.println("Not a valid argument");
                System.out.println(options);
                System.exit(0);
            }
        }
    }

    public Queue() {
        projects = new ArrayList<Project>();
    }

    public void addTask() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter task name >> ");
        Task t = new Task(scanner.nextLine());
        System.out.print("Is this a quick task? (y/n) >> ");
        if (scanner.nextLine().equalsIgnoreCase("n")) {
            t.setSlow();
        }
        System.out.print("Enter associated project name >> ");

        Project p = new Project(scanner.nextLine());
        int index;

        if (this.findProject(t) != null) {
            System.out.print("Task already exists");
        } else if ((index = projects.indexOf(p)) != -1) {
            projects.get(index).addTask(t);
            System.out.println("Task added to project");
        } else {
            System.out.println("No such project");
        }
    }

    public void addProject() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter project name >> ");
        Project p = new Project(scanner.nextLine());
        System.out.print("Is this a front burner project? (y/n) >> ");
        if (scanner.nextLine().equalsIgnoreCase("n")) {
            p.setBackBurner();
        }
        if (projects.indexOf(p) == -1) {
            projects.add(p);
            System.out.println("Project added to list");
        } else {
            System.out.println("Project already exists");
        }
    }

    public void removeProject() {
        Scanner scanner = new Scanner(System.in);
        int index;
        String message;
        System.out.print("Enter project name >> ");
        Project p = new Project(scanner.nextLine());
        if ((index = projects.indexOf(p)) != -1) {
            if (projects.get(index).hasTasks()) {
                message = "Project has tasks, are you sure you want to delete? (y/n) >> ";
            } else {
                message = "Delete project: '" + p.getTitle() + "'? (y/n) >> ";
            }
            System.out.print(message);
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("Y")) {
                projects.remove(index);
            } else {
                System.out.println("Project not deleted");
            }
        } else {
            System.out.println("No such project");
        }
    }

    public void removeTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter task name >> ");
        Task t = new Task(scanner.nextLine());
        Project p = this.findProject(t);
        if (p != null) {
            System.out.print("Delete task: '" + t + "'? (y/n) >> ");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("Y")) {
                p.removeTask(t);
            } else {
                System.out.println("Task not deleted");
            }
        } else {
            System.out.println("No such task");
        }
    }

    public Project findProject(Task t) {
        for (Project p : projects) {
            ArrayList<Task> tasks = p.getTasks();
            for (Task t2 : tasks) {
                if (t2.equals(t)) {
                    return p;
                }
            }
        }
        return null;
    }

    public void done() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Which task number? >> ");
        try {
            this.done(Integer.parseInt(scanner.nextLine()));
        } catch (Exception e) {
            System.out.println("That's not a number");
        }
    }
    
    public void done(int index) {
        ArrayList<Task> tasks = this.getTasks();
        if (index > 0 && index <= tasks.size()) {
            Task t = tasks.get(index-1);
            Project p = this.findProject(t);
            p.removeTask(t);
        } else {
            System.out.println("Not a valid task");
        }
    }

    public void change() {
        System.out.println("Inside change()");
    }

    public void review() {
        System.out.println("Inside review()");
    }
    
    public ArrayList<Task> getTasks() {
        ArrayList<Task> quickTasks = new ArrayList<Task>();
        ArrayList<Task> slowTasks = new ArrayList<Task>();
        for (Project p : projects) {
            if (p.isFrontBurner()) {
                quickTasks.addAll(p.getQuickTasks());
                slowTasks.addAll(p.getSlowTasks());
            } else {
                quickTasks.addAll(p.getQuickTasks());
            }
        }
        quickTasks.addAll(slowTasks);
        return quickTasks;
    }
}
