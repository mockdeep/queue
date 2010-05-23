package net.knowledgepile.queue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

public class Queue {
    private ArrayList<Project> projects;

    public static void main(String[] args) {
        // create a new Queue object
        Queue queue = new Queue();
        // populate the Queue object from the storage file
        queue.projects = QueueIO.readQ();
        // figure out what the user wants to do with the queue and execute it
        queue.parseArgs(args);
        // write the new queue out to the file 
        QueueIO.writeQ(queue.projects);
        // write the most pressing tasks out to a task file
        QueueIO.writeTasks(queue.getTasks());
    }

    // This was a hacky attempt to play around with reflection. Ultimately if I
    // come back to this project I think I may have to expand it out into a
    // lengthy if-then-else clause.  Right now it checks to see if what the
    // user typed for their second argument matches one of the existing methods
    // of the Queue class.  If so, it executes the command, otherwise printing
    // out an error.  I worked at it for a while but was unable to come up with
    // an implementation that didn't give me a warning, 
    // hence the @SuppressWarnings()
    @SuppressWarnings("unchecked")
    private void parseArgs(String[] args) {
        // the list of legal command line options
        String options = "Options are 'addProject', 'addTask', 'removeProject, 'removeTask', 'done', 'change', and 'review'";
        // if the user didn't specify and argument...
        if (args.length == 0) {
            System.out.println("Enter an argument, e.g.: Q addProject");
            System.out.println(options);
        } else {
            // try to get the method matching the first command line argument
            // and invoke it on this Queue
            try {
                Class c = this.getClass();
                Method m = c.getMethod(args[0], new Class[0]);
                m.invoke(this);
            // otherwise exit, explaining the user his options
            } catch (Exception e) {
                System.out.println("Not a valid argument");
                System.out.println(options);
                System.exit(0);
            }
        }
    }

    // default constructor
    public Queue() {
        projects = new ArrayList<Project>();
    }

    // get user input from the command line to add a task to an existing project
    public void addTask() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter task name >> ");
        Task t = new Task(scanner.nextLine());
        System.out.print("Is this a quick task? (y/n) >> ");

        // unless the user types 'n' then assume it's a quick task (this is so 
        // that the user will be made aware of the task until they specifically
        // set it to slow)
        if (scanner.nextLine().equalsIgnoreCase("n")) {
            t.setSlow();
        }
        System.out.print("Enter associated project name >> ");

        Project p = new Project(scanner.nextLine());
        int index;

        // find out if the task already exists
        if (this.findProject(t) != null) {
            System.out.print("Task already exists");
        // attempt to add the task to the project if the project exists
        } else if ((index = projects.indexOf(p)) != -1) {
            projects.get(index).addTask(t);
            System.out.println("Task added to project");
        // otherwise give an error
        } else {
            System.out.println("No such project");
        }
    }

    // adds a new project to the queue
    public void addProject() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter project name >> ");
        Project p = new Project(scanner.nextLine());
        System.out.print("Is this a front burner project? (y/n) >> ");
        // projects are set to front burner by default
        if (scanner.nextLine().equalsIgnoreCase("n")) {
            p.setBackBurner();
        }
        // check of the project already exists, add it if not
        if (projects.indexOf(p) == -1) {
            projects.add(p);
            System.out.println("Project added to list");
        } else {
            System.out.println("Project already exists");
        }
    }

    // remove a project from the Queue
    public void removeProject() {
        Scanner scanner = new Scanner(System.in);
        int index;
        String message;
        System.out.print("Enter project name >> ");
        Project p = new Project(scanner.nextLine());
        if ((index = projects.indexOf(p)) != -1) {
            // check if the project has tasks associated with it, if so warn,
            // otherwise just confirm deletion
            if (projects.get(index).hasTasks()) {
                message = "Project has tasks, are you sure you want to delete? (y/n) >> ";
            } else {
                message = "Delete project: '" + p.getTitle() + "'? (y/n) >> ";
            }
            System.out.print(message);
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("Y")) {
                projects.remove(index);
            // default not to delete project
            } else {
                System.out.println("Project not deleted");
            }
        // no project by the name specified exists
        } else {
            System.out.println("No such project");
        }
    }

    // remove a task from a project
    public void removeTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter task name >> ");
        Task t = new Task(scanner.nextLine());
        // find the project associated with the task
        Project p = this.findProject(t);

        // if the project exists, confirm and delete task
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

    // find and return the project associated with a given task
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

    // remove a task from the queue
    public void done() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Which task number? >> ");
        try {
            // chain it forward to done(int)
            this.done(Integer.parseInt(scanner.nextLine()));
        } catch (Exception e) {
            System.out.println("That's not a number");
        }
    }
    
    // remove a task from the queue based on the number input by the user
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

    // method intended to someday change a project or task title
    public void change() {
        System.out.println("Inside change()");
    }

    // method intended to someday allow the user to review the entire Queue
    public void review() {
        System.out.println("Inside review()");
    }
    
    // returns an ArrayList of Tasks from all projects that are either quick
    // or high priority.  This is intended for the purpose of task list creation
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
