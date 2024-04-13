package ca.ucalgary.groupprojectgui.util;

import ca.ucalgary.groupprojectgui.Data;
import ca.ucalgary.groupprojectgui.objects.Assignment;
import ca.ucalgary.groupprojectgui.objects.Course;
import ca.ucalgary.groupprojectgui.objects.Exam;
import ca.ucalgary.groupprojectgui.objects.Project;

import java.io.*;
import java.util.Arrays;

/**
 * WriteFile.java - stores the data onto a file.
 */
public class FileSaver {

    /**
     * ca.ucalgary.groupprojectgui.Main structure of the class
     * @return true if data stored successfully or false otherwise
     */
    public static boolean save(File file, Data data){

        try (FileWriter fw = new FileWriter(file)) {
            fw.write("Courses\n");
            for (Course course : data.getAllCourses()) {
                fw.write(String.format("%s, %s, %s, %s, %s\n", course.getCourseName(), course.getProfName(), course.getProfEmail(), course.getTargetGrade(), Boolean.toString(course.isInProgress())));

            }
            fw.write("Projects\n");
            for (Project project : data.getAllProjects()) {
                String completion;
                if (project.isProjectComplete()) {
                    completion = "Complete";
                } else {
                    completion = "Incomplete";
                }

                if (project instanceof Exam exam) {
                    String deadline = ArrayToString(Arrays.toString(exam.getProjectDeadline()));
                    fw.write(String.format("%s, %s, %s, %s, %s, %s\n", exam.getCourseName(), exam.getProjectName(), exam.getProjectWeight(), deadline, exam.getLocation(), exam.getReviewTopics()));
                } else if (project instanceof Assignment assignment) {
                    String deadline = ArrayToString(Arrays.toString(assignment.getProjectDeadline()));
                    fw.write(String.format("%s, %s, %s, %s, %s\n", assignment.getCourseName(), assignment.getProjectName(), assignment.getProjectWeight(), deadline, assignment.getSpecialInstructions()));
                }
            }
            return true;

        } catch (IOException ioe) {
            return false;
        }

    }

    private static String ArrayToString(String string) {

        string = string.replace("[", "").replace("]", "");
        string = string.replace(",", "");

        return string;
    }

}
