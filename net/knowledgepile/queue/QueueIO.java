package net.knowledgepile.queue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class QueueIO {
    private static String pathToQ = "/home/explorer/workspace/Queue/bin/Q.txt";
    private static String outPath = "/home/explorer/workspace/Queue/bin/Q2.txt";
    private static String pathToTasks = "/home/explorer/workspace/Queue/bin/tasks.txt";
//    private static String pathToQ = "/home/explorer/Documents/Q.txt";
//    private static String outPath = "/home/explorer/Documents/Q.txt";
//    private static String pathToTasks = "/home/explorer/Documents/tasks.txt";

    // read projects/tasks from Q.txt
    public static ArrayList<Project> readQ() {
        ArrayList<Project> projects = new ArrayList<Project>();
        // if no Q file exists then just return an empty ArrayList of projects
        if (!(new File(pathToQ)).exists()) {
            System.out.println("No Q.txt file found");
            return projects;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathToQ));
            String line = null;
            Project p = null;
            // A lot of code to try and very generously read the Q file.
            // The idea being that a user might be inclined to edit the file
            // manually and might not be as accurate as we might like.
            // We do our best to read the file, parse it accurately, and
            // write it much more cleanly.
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Project:")) {
                    p = new Project(line.substring(line.indexOf(':')+1));
                    projects.add(p);
                } else if (line.startsWith("Burner:")) {
                    if (p == null) {
                        p = new Project();
                        projects.add(p);
                    }
                    if (line.substring(line.indexOf(":")+1).trim().equalsIgnoreCase("back")) {
                        p.setBackBurner();
                    } else {
                        p.setFrontBurner();
                    }
                } else if (!line.equals("")) {
                    if (p == null) {
                        p = new Project();
                        projects.add(p);
                    }
                    int count = 0;
                    // this is for intended future functionality that allows
                    // users to specify the order in which tasks should be done
                    // based on the number of *s in front of the task
                    while (line.startsWith("*")) {
                        line = line.substring(1).trim();
                        count++;
                    }
                    if (count == 0) count++;
                    Task t = new Task(line, count);
                    if (line.length() > 1 && line.charAt(1)==':') {
                        if (line.substring(0,1).equalsIgnoreCase("S")) {
                            t.setSlow();
                        } else {
                            t.setQuick();
                        }
                        t.setTitle(line.substring(2).trim());
                    }
                    p.addTask(t);
                }	
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }			
        return projects;
    }

    // write out projects/tasks to Q.txt
    public static void writeQ(ArrayList<Project> projects) {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
            for (Project p : projects) {
                p.sortTasks();
                if (p.isFrontBurner()) {
                    bw.append(p.toString());
                    bw.newLine();
                }
            }
            for (Project p : projects) {
                if (!p.isFrontBurner()) {
                    bw.append(p.toString());
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // write quick and high priority tasks to tasks.txt
    public static void writeTasks(ArrayList<Task> tasks) {
        BufferedWriter bw;
        try
        {
            bw = new BufferedWriter(new FileWriter(pathToTasks));
            int i = 1;
            for (Task t : tasks) {
                bw.append(i + ": " + t.getTitle());
                bw.newLine();
                i++;
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}