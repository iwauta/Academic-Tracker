package ca.ucalgary.groupprojectgui.util;

import ca.ucalgary.groupprojectgui.Data;

import java.io.*;
import java.util.Scanner;

/**
 * ReadFile.java - reads stored data from a file.
 *
 * @author Utaha Iwai
 * @tutorial T09
 * @email utaha.iwai@ucalgary.ca
 */
public class FileLoader {

    /**
     * Loads data from saved file.
     *
     * @param file input file to load data from
     */
    public static Data load(File file) {
        Data data = new Data();
        try(Scanner scanner = new Scanner(file)) {
            String line1 = scanner.nextLine();
            if (!line1.equals("Courses")) {
                System.err.println("Could not find course in file.");
                return null;
            }
            boolean foundProject = false;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("Projects")) {
                    foundProject = true;
                    break;
                }
                String[] parts = line.split(",");
                String courseName = parts[0].trim();
                String profName = parts[1];
                String profEmail = parts[2];
                double targetGrade = Double.parseDouble(parts[3]);
                boolean inProgress = Boolean.parseBoolean(parts[4]);


                boolean b = data.storeNewCourse(courseName, profName, profEmail, targetGrade);

                if (!b) {
                    System.err.println("Error in storing course.");
                }

                if (!inProgress) {
                    data.getCourse(courseName).closeCourse();
                } else{
                    data.getCourse(courseName).setInProgress();
                }

            }

            if (!foundProject) {
                return data;
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String[] parts = line.split(",");
                String course = parts[0];
                String projectName = parts[1];
                double projectWeight = Double.parseDouble(parts[2]);

                String d = parts[3].trim();
                String[] dates = d.split(" ");
                int[] deadline = new int[3];

                for (int i = 0; i < dates.length; i++) {
                    deadline[i] = Integer.parseInt(dates[i]);
                }


                    if (parts.length == 5) { // storing assignment
                        String specialInstructions = parts[4];

                        data.storeNewAssignment(course, projectName, projectWeight, deadline, specialInstructions);

                    } else if (parts.length == 6) { // storing exam
                        String location = parts[4];
                        String reviewTopics = parts[5];

                        data.storeNewExam(course, projectName, projectWeight, deadline, location, reviewTopics);

                    }

            }

        } catch (IOException ioe){
            System.err.println("Exception");
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return data;

    }

}
