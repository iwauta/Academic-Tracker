package ca.ucalgary.groupprojectgui;
import ca.ucalgary.groupprojectgui.objects.*;
import ca.ucalgary.groupprojectgui.util.FileLoader;
import ca.ucalgary.groupprojectgui.util.FileSaver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * DataTest.java - test the functionality of the methods in data.java.
 *
 * @author Utaha Iwai, Dipti Kumar
 * @tutorial T09
 * @profEmail utaha.iwai@ucalgary.ca, dipti.kumar@ucalgary.ca
 */
class DataTest {

    Data data;

    @BeforeEach
    void BeforeEach(){
        data = new Data();
    }

    static String courseName = "CPSC600";
    static String profName = "John Doe";
    static String profEmail = "john.doe@ucalgary.ca";
    static Double targetGrade = 0.0;

    static String courseName2 = "CPSC601";
    static String profName2 = "John Doe";
    static String profEmail2 = "john.doe@ucalgary.ca";
    static Double targetGrade2 = 0.0;

    static String projectName = "assignment1";
    static double projectWeight = 15.00;
    static int[] deadline = {15, 3, 2024};
    static String specialInstructions = "";


    // Course

    @Test
    void storeNewCourse()  {
        assertEquals(0, data.getAllCourses().size());
        boolean success = data.storeNewCourse(courseName, profName, profEmail, targetGrade);
        assertEquals(1, data.getAllCourses().size());
        assertEquals(courseName, data.getAllCourses().get(0).getCourseName());
        assertEquals(profName, data.getAllCourses().get(0).getProfName());
        assertEquals(profEmail, data.getAllCourses().get(0).getProfEmail());
        assertTrue((Boolean) data.getAllCourses().get(0).isInProgress());
        assertTrue(success);
    }

    @Test
    void storeNewCourse2()  {

        String courseName = "CPSC600";
        String profName = "John Doe";
        String profEmail = "john.doe@ucalgary.ca";
        double targetGrade = 0;
        assertEquals(0, data.getAllCourses().size());
        boolean success = data.storeNewCourse(courseName, profName, profEmail, targetGrade);
        assertEquals(1, data.getAllCourses().size());
        assertEquals(courseName, data.getAllCourses().get(0).getCourseName());
        assertEquals(profName, data.getAllCourses().get(0).getProfName());
        assertEquals(profEmail, data.getAllCourses().get(0).getProfEmail());
        assertTrue((Boolean) data.getAllCourses().get(0).isInProgress());
        assertTrue(success);
    }

    @Test
    void storeTwoCourses()  {
        assertEquals(0, data.getAllCourses().size());
        boolean success = data.storeNewCourse(courseName, profName, profEmail, targetGrade);
        // core.objects.Course 2
        assertEquals(1, data.getAllCourses().size());
        success = data.storeNewCourse(courseName2, profName2, profEmail2, targetGrade2);
        assertEquals(2, data.getAllCourses().size());
        assertEquals(courseName2, data.getAllCourses().get(1).getCourseName());
        assertEquals(profName2, data.getAllCourses().get(1).getProfName());
        assertEquals(profEmail2, data.getAllCourses().get(1).getProfEmail());
        assertTrue((Boolean) data.getAllCourses().get(1).isInProgress());
        assertTrue(success);
    }


    @Test
    void storeThreeCourses()  {
        assertEquals(0, data.getAllCourses().size());
        boolean success = data.storeNewCourse(courseName, profName, profEmail, targetGrade);
        // core.objects.Course 2
        assertEquals(1, data.getAllCourses().size());
        success = data.storeNewCourse(courseName2, profName, profEmail, targetGrade);
        assertEquals(2, data.getAllCourses().size());
        assertEquals(courseName2, data.getAllCourses().get(1).getCourseName());
        assertEquals(profName, data.getAllCourses().get(1).getProfName());
        assertEquals(profEmail, data.getAllCourses().get(1).getProfEmail());
        assertTrue((Boolean) data.getAllCourses().get(1).isInProgress());
        assertTrue(success);

        assertEquals(2, data.getAllCourses().size());
        success = data.storeNewCourse("DPTI990", profName, profEmail, targetGrade);
        assertEquals(3, data.getAllCourses().size());
        assertTrue(success);

    }

    @Test
    void storeTwoCoursesFail()  {
        assertEquals(0, data.getAllCourses().size());
        boolean success = data.storeNewCourse(courseName, profName, profEmail, targetGrade);
        // Course 2
        // Adding a course with the same name
        assertEquals(1, data.getAllCourses().size());
        success = data.storeNewCourse(courseName, profName2, profEmail2, targetGrade2);
        assertEquals(1, data.getAllCourses().size());
        assertFalse(success); // Adding should fail
    }

    @Test
    void checkExistCourseNoCourseAdded() {
        assertFalse(data.checkExistCourse(courseName));
    }

    @Test
    void checkExistCourseTrue()  {
        data.storeNewCourse(courseName, profName, profEmail, targetGrade);
        assertTrue(data.checkExistCourse(courseName));
    }

    @Test
    void getAllCourses()  {
        ArrayList<Course> expected = new ArrayList<>();
        data.storeNewCourse(courseName, profName, profEmail,targetGrade); // storing course (1)
        data.storeNewCourse(courseName2, profName2, profEmail2,targetGrade2); // storing course (2)

        // Manually create Course objects and add to list
        expected.add(new Course(courseName, new Professor(profName, profEmail), targetGrade));
        expected.add(new Course(courseName2, new Professor(profName2, profEmail2), targetGrade2));

        // comparing ArrayLists of expected and actual course information
        Assertions.assertEquals(expected, data.getAllCourses());
    }

    @Test
    void getAllCoursesNoCourses() {
        // no course stored
        assertEquals(0, data.getAllCourses().size());
    }

    @Test
    void sortCourses()  {
        Professor professor = new Professor(profName, profEmail);
        ArrayList<Course> expected = new ArrayList<>();
        ArrayList<Course> actual = new ArrayList<>();
        String courseName1 = "CPSC600";
        String courseName2 = "ART201";
        String courseName3 = "ART300";
        String courseName4 = "PHYS200";
        Course course1 = new Course(courseName1, professor, targetGrade);
        Course course2 = new Course(courseName2, professor, targetGrade);
        Course course3 = new Course(courseName3, professor, targetGrade);
        Course course4 = new Course(courseName4, professor, targetGrade);
        data.storeNewCourse(courseName1, profName, profEmail, targetGrade);
        data.storeNewCourse(courseName2, profName, profEmail, targetGrade);
        data.storeNewCourse(courseName3, profName, profEmail, targetGrade);
        data.storeNewCourse(courseName4, profName, profEmail, targetGrade);

        // before sorting
        expected.add(course1);
        expected.add(course2);
        expected.add(course3);
        expected.add(course4);
        assertEquals(expected, data.getAllCourses());

        // after sorting (should be alphabetical)
        expected.clear();
        expected.add(course2); // ART201
        expected.add(course3); // ART300
        expected.add(course1); // CPSC600
        expected.add(course4); // PHYS200
        actual = data.sortCourses();
        assertEquals(expected, actual);

        // closed courses should come last
        course1.closeCourse();
        course3.closeCourse();
        data.getCourse(courseName1).closeCourse();
        data.getCourse(courseName3).closeCourse();
        expected.clear();
        expected.add(course2); // ART201
        expected.add(course4); // PHYS200
        expected.add(course3); // ART300 (closed)
        expected.add(course1); // CPSC600 (closed)
        actual.clear(); actual = data.sortCourses();
        assertEquals(expected, actual);
    }


    // Project...

    @Test
    void storeNewAssignment()  {

        data.storeNewCourse(courseName, profName, profEmail,targetGrade);

        assertEquals(0, data.getAllProjects().size());
        boolean success = data.storeNewAssignment(courseName, projectName, projectWeight, deadline, specialInstructions);
        assertEquals(1, data.getAllProjects().size());

        assertEquals(courseName, data.getAllProjects().getFirst().getCourseName());
        assertEquals(projectName, data.getAllProjects().getFirst().getProjectName());
        assertEquals(projectWeight, data.getAllProjects().getFirst().getProjectWeight());
        assertEquals(deadline, data.getAllProjects().getFirst().getProjectDeadline());
        assertEquals(specialInstructions,((Assignment)data.getAllProjects().getFirst()).getSpecialInstructions());

        assertTrue(success);
    }

    @Test
    void storeNewTwoAssignment2()  {

        String courseName = "CPSC600";
        String profName = "John Doe";
        String profEmail = "john.doe@ucalgary.ca";
        double targetGrade = 0;

        data.storeNewCourse(courseName, profName, profEmail,targetGrade);

        assertEquals(0, data.getAllProjects().size());
        boolean success = data.storeNewAssignment(courseName, projectName, projectWeight, deadline, specialInstructions);
        assertEquals(1, data.getAllProjects().size());

        assertEquals(courseName, data.getAllProjects().getFirst().getCourseName());
        assertEquals(projectName, data.getAllProjects().getFirst().getProjectName());
        assertEquals(projectWeight, data.getAllProjects().getFirst().getProjectWeight());
        assertEquals(deadline, data.getAllProjects().getFirst().getProjectDeadline());
        assertEquals(specialInstructions,((Assignment)data.getAllProjects().getFirst()).getSpecialInstructions());

        assertTrue(success);

        String project2 = "assignment2";
        double projectWeight2 = 15.00;
        int[] deadline2 = {10, 3, 2024};
        String specialInstructions2 = "";

        assertEquals(1, data.getAllProjects().size());
        boolean success2 = data.storeNewAssignment(courseName, project2, projectWeight2, deadline2, specialInstructions2);
        assertEquals(2, data.getAllProjects().size());

        assertEquals(courseName, data.getAllProjects().get(1).getCourseName());
        assertEquals(project2, data.getAllProjects().get(1).getProjectName());
        assertEquals(projectWeight2, data.getAllProjects().get(1).getProjectWeight());
        assertEquals(deadline2, data.getAllProjects().get(1).getProjectDeadline());
        assertEquals(specialInstructions2,((Assignment)data.getAllProjects().get(1)).getSpecialInstructions());

        assertTrue(success2);
    }

    @Test
    void storeNewExam()  {

        data.storeNewCourse(courseName, profName, profEmail,targetGrade);

        String project = "exam1";
        double projectWeight = 15.00;
        int[] deadline = {15, 3, 2024};
        String location = "ict111";
        String reviewTopics = "";

        assertEquals(0, data.getAllProjects().size());
        boolean success = data.storeNewExam(courseName, project, projectWeight, deadline, location, reviewTopics);
        assertEquals(1, data.getAllProjects().size());


        assertEquals(courseName, data.getAllProjects().getFirst().getCourseName());
        assertEquals(project, data.getAllProjects().getFirst().getProjectName());
        assertEquals(projectWeight, data.getAllProjects().getFirst().getProjectWeight());
        assertEquals(deadline, data.getAllProjects().getFirst().getProjectDeadline());

        assertTrue(success);
    }

    @Test
    void storeNewProjectError()  {
        data.storeNewCourse(courseName, profName, profEmail, targetGrade);
        assertEquals(0, data.getAllProjects().size());
        data.storeNewAssignment(courseName, projectName, projectWeight, deadline, specialInstructions);
        assertEquals(1, data.getAllProjects().size());
        // Storing project with same courseName, projectName
        boolean success = data.storeNewAssignment(courseName, projectName, 30, new int[] {1,1,2024}, "");
        assertFalse(success); // false because project should not be stored, since the course it is for is not stored
        assertEquals(1, data.getAllProjects().size());
    }

    @Test
    void storeNewExam2()  {
        data.storeNewCourse(courseName, profName, profEmail,targetGrade);

        String project = "exam1";
        double projectWeight = 15.00;
        int[] deadline = {15, 3, 2024};
        String location = "ict111";
        String reviewTopics = "";

        assertEquals(0, data.getAllProjects().size());
        boolean success = data.storeNewExam(courseName, project, projectWeight, deadline, location, reviewTopics);
        assertEquals(1, data.getAllProjects().size());


        assertEquals(courseName, data.getAllProjects().getFirst().getCourseName());
        assertEquals(project, data.getAllProjects().getFirst().getProjectName());
        assertEquals(projectWeight, data.getAllProjects().getFirst().getProjectWeight());
        assertEquals(deadline, data.getAllProjects().getFirst().getProjectDeadline());

        assertTrue(success);

        String project2 = "exam2";
        double projectWeight2 = 15.00;
        int[] deadline2 = {15, 3, 2024};
        String location2 = "ict111";
        String reviewTopics2 = "";

        assertEquals(1, data.getAllProjects().size());
        boolean success2 = data.storeNewExam(courseName, project2, projectWeight2, deadline2, location2, reviewTopics2);
        assertEquals(2, data.getAllProjects().size());


        assertEquals(courseName, data.getAllProjects().get(1).getCourseName());
        assertEquals(project2, data.getAllProjects().get(1).getProjectName());
        assertEquals(projectWeight2, data.getAllProjects().get(1).getProjectWeight());
        assertEquals(deadline2, data.getAllProjects().get(1).getProjectDeadline());

        assertTrue(success2);

    }

    @org.junit.jupiter.api.Test
    void storeNewProjectError2() {
        String courseName = "CPSC600"; // course has not been stored

        String project = "assignment1";
        double weight = 15.00;
        int[] deadline = {15, 3, 2024};
        String specialInstruction = "";
        data.storeNewAssignment(courseName, project, weight, deadline, specialInstruction);
    }

    @org.junit.jupiter.api.Test
    void storeNewProjectError3() {
        String courseName = "CPSC600"; // course has not been stored
        String project = "exam1";
        double weight = 15.00;
        int[] deadline = {15, 3, 2024};
        String location = "ms133";
        String reviewTopics = "arrays, lists";
        boolean success = data.storeNewExam(courseName, project, weight, deadline, location, reviewTopics);
        assertFalse(success);
    }

    @org.junit.jupiter.api.Test
    void storeNewProjectError4() {

        String courseName = "CPSC600"; // course has not been stored
        assertEquals(0, data.getAllCourses().size());

    }

    @Test
    void checkProjectExistInCourse()  {
        data.storeNewCourse(courseName, profName, profEmail,targetGrade); // storing course

        assertEquals(0, data.getAllProjects().size());
        boolean success = data.storeNewAssignment(courseName, projectName, projectWeight, deadline, specialInstructions); // storing project in course
        assertEquals(1, data.getAllProjects().size());

        assertEquals(courseName, data.getAllProjects().getFirst().getCourseName());
        assertEquals(projectName, data.getAllProjects().getFirst().getProjectName());
        assertEquals(projectWeight, data.getAllProjects().getFirst().getProjectWeight());
        assertEquals(deadline, data.getAllProjects().getFirst().getProjectDeadline());
        assertFalse((Boolean) data.getAllProjects().getFirst().isProjectComplete());

        assertTrue(success);

        assertTrue(data.checkProjectExistInCourse(courseName, projectName)); // true, since the project and course have been stored

    }

    @Test
    void checkProjectExistInCourseNoProject() {
        assertEquals(0, data.getAllProjects().size());
        assertFalse(data.checkProjectExistInCourse(courseName, projectName)); // false, since no project was stored
    }

    @Test
    void getAllProjects()  {
        ArrayList<Object> expected = new ArrayList<>();
        data.storeNewCourse(courseName, profName, profEmail,targetGrade); // storing course
        assertEquals(0, data.getAllProjects().size());

        data.storeNewAssignment(courseName, projectName, projectWeight, deadline, specialInstructions); // storing 1st project in course
        assertEquals(1, data.getAllProjects().size());

        // Create ArrayList of Projects manually
        expected.add(new Assignment(courseName,projectName, projectWeight, deadline, specialInstructions));

        Assertions.assertEquals(expected, data.getAllProjects());
    }

    @Test
    void getAllProjectsNoProjects() {

        // no project or course stored
        assertEquals(0, data.getAllProjects().size());
    }

    @Test
    void getAllProjectsNoCourse() {
        // no project or course stored
        assertEquals(0, data.getAllCourses().size());
    }

    @Test
    void markProjectComplete()  {

        data.storeNewCourse(courseName, profName, profEmail,targetGrade); // store course

        assertEquals(0, data.getAllProjects().size());
        data.storeNewAssignment(courseName, projectName, projectWeight, deadline, specialInstructions); // store project under stored course
        assertEquals(1, data.getAllProjects().size());

        boolean success = data.markProjectComplete(courseName, projectName); // Mark project Complete
        assertTrue(success); // project should be marked complete

        // Create Project manually
        ArrayList<Object> expected = new ArrayList<>();
        Assignment a = new Assignment(courseName, projectName, projectWeight, deadline, specialInstructions);
        a.setProjectComplete(); // mark complete
        expected.add(a);

        // Compare
        Assertions.assertEquals(expected, data.getAllProjects());

    }

    @Test
    void markProjectComplete2()  {
        data.storeNewCourse(courseName, profName, profEmail, targetGrade); // store course
        data.storeNewAssignment(courseName, projectName, projectWeight, deadline, specialInstructions);
        data.storeNewCourse(courseName2, profName2, profEmail2, targetGrade2); // store course (2)
        data.storeNewAssignment(courseName2, projectName, projectWeight, deadline, specialInstructions);

        // Mark second one complete
        data.markProjectComplete(courseName2, projectName);

        // Create Project manually
        ArrayList<Object> expected = new ArrayList<>();
        Assignment a = new Assignment(courseName, projectName, projectWeight, deadline, specialInstructions);
        Assignment a2 = new Assignment(courseName2, projectName, projectWeight, deadline, specialInstructions);
        a2.setProjectComplete(); // mark complete
        expected.add(a);
        expected.add(a2);

        assertEquals(expected, data.getAllProjects());

    }

    @Test
    void sortProjects()  {
        Course course = new Course(courseName, new Professor(profName, profEmail), targetGrade);
        data.storeNewCourse(courseName,profName, profEmail, targetGrade);
        ArrayList<Project> expected = new ArrayList<>();
        ArrayList<Project> actual = new ArrayList<>();
        String projectName1 = "assignment1";
        String projectName2 = "assignment2";
        String projectName3 = "assignment3";
        String projectName4 = "assignment4";
        int[] deadline1 = {30,3,2024};
        int[] deadline2 = {30,3,2023};
        int[] deadline3 = {30,2,2024};
        int[] deadline4 = {1,4,2024};
        Project project1 = new Assignment(courseName, projectName1, projectWeight, deadline1, specialInstructions);
        Project project2 = new Assignment(courseName, projectName2, projectWeight, deadline2, specialInstructions);
        Project project3 = new Assignment(courseName, projectName3, projectWeight, deadline3, specialInstructions);
        Project project4 = new Assignment(courseName, projectName4, projectWeight, deadline4, specialInstructions);
        data.storeNewAssignment(courseName,projectName1, projectWeight, deadline1,specialInstructions);
        data.storeNewAssignment(courseName,projectName2, projectWeight, deadline2,specialInstructions);
        data.storeNewAssignment(courseName,projectName3, projectWeight, deadline3,specialInstructions);
        data.storeNewAssignment(courseName,projectName4, projectWeight, deadline4,specialInstructions);

        // before sorting
        expected.add(project1);
        expected.add(project2);
        expected.add(project3);
        expected.add(project4);
        assertEquals(expected, data.getAllProjects());

        // after sorting (should be chronological)
        expected.clear();
        expected.add(project2); // 30/03/2023
        expected.add(project3); // 30/02/2024
        expected.add(project1); // 30/03/2024
        expected.add(project4); // 01/04/2024
        actual = data.sortProjects();
        assertEquals(expected, actual);

        // completed projects should come last
        project1.setProjectComplete();
        project3.setProjectComplete();
        for(Project project: data.getAllProjects()){
            if(project.getProjectName().equals(projectName1) || project.getProjectName().equals(projectName3)){
                project.setProjectComplete();
            }
        }
        expected.clear();
        expected.add(project2); // 30/03/2023
        expected.add(project4); // 01/04/2024
        expected.add(project3); // 30/02/2024 (complete)
        expected.add(project1); // 30/03/2024 (complete)
        actual = data.sortProjects();
        assertEquals(expected, actual);
    }


    // Save and Load...

    @org.junit.jupiter.api.Test
    void testSaveAndLoad()  {
        Data data1 = new Data();
        int[] deadline = {1, 3, 2024};
        data1.storeNewCourse("CPSC600", "John Hudson", "jhuds@ucalgary.ca", 90.0);
        data1.storeNewAssignment("CPSC600", "A1", 15.5, deadline, "will be marked");
        data1.storeNewExam("CPSC600", "E1", 15.5, deadline, "ict122", "arrays, lists");

        File file = new File("dataTest.csv");

        FileSaver.save(file, data1);
        Data data2 = FileLoader.load(file);

        assertNotNull(data2);

        assertEquals("CPSC600", data2.getAllCourses().getFirst().getCourseName());
        assertEquals(" John Hudson", data2.getAllCourses().getFirst().getProfName());
        assertEquals(" jhuds@ucalgary.ca", data2.getAllCourses().getFirst().getProfEmail());
        assertEquals(90.0, data2.getAllCourses().getFirst().getTargetGrade());

        assertEquals(" A1", data2.getAllProjects().getFirst().getProjectName());

    }

    @org.junit.jupiter.api.Test
    void testSaveAndLoad2()  {
        Data data1 = new Data();
        int[] deadline = {1, 3, 2024};
        data1.storeNewCourse("DPT666", "John Hudson", "jhuds@ucalgary.ca", 90.0);
        data1.storeNewAssignment("DPT666", "A1", 15.5, deadline, "will be marked");
        data1.storeNewExam("DPT666", "E1", 15.5, deadline, "ict122", "arrays, lists");

        File file = new File("dataTest.csv");

        FileSaver.save(file, data1);
        Data data2 = FileLoader.load(file);

        assertNotNull(data2);

        assertEquals("DPT666", data2.getAllCourses().getFirst().getCourseName());
        assertEquals(" John Hudson", data2.getAllCourses().getFirst().getProfName());
        assertEquals(" jhuds@ucalgary.ca", data2.getAllCourses().getFirst().getProfEmail());
        assertEquals(90.0, data2.getAllCourses().getFirst().getTargetGrade());

        assertEquals(" A1", data2.getAllProjects().getFirst().getProjectName());

    }

    @org.junit.jupiter.api.Test
    void testSaveAndLoadError() {

        assertThrows(NoSuchElementException.class, () -> {
            Data data1 = new Data();

            File file = new File("dataTest2.csv");
            FileSaver.save(file, data1);
            Data data2 = FileLoader.load(file);
            data2.getAllCourses().getFirst().getCourseName();
        });
    }




}