package ca.ucalgary.iwauta.objects;

import java.util.Objects;

public class Exam extends Project {

    private String location;
    private String reviewTopics;

    /**
     * Constructor
     * @param courseName name of the course
     * @param projectName name of the exam
     * @param projectWeight weighted percentage of the final grade
     * @param projectDeadline deadline {date, month, year}
     * @param location location of the exam
     * @param  reviewTopics topics to be reviewed
     */
    public Exam(String courseName, String projectName, double projectWeight, int[] projectDeadline, String location, String reviewTopics) {
        super(courseName, projectName, projectWeight, projectDeadline);
        this.reviewTopics = reviewTopics;
        this.location = location;
    }

    /**
     * Getter for reviewTopics
     * @return reviewTopics
     */
    public String getReviewTopics() {
        return reviewTopics;
    }

    /**
     * Getter for location
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for reviewTopics
     * @param reviewTopics topics to review for the exam
     */
    public void setReviewTopics(String reviewTopics) {
        this.reviewTopics = reviewTopics;
    }

    /**
     * Setter for location
     * @param location location of the exam
     */
    public void setLocation(String location) {
        this.location = location;
    }


    /**
     * Add special instructions to project description
     * @return string representation of other
ect
     */
    @Override
    public String toString() {
        String instruction = String.format("   %-40s%n", reviewTopics + " @" + location);
        return super.toString() + instruction;
    }

    /**
     * Deep equals. compare contents of the object
     * @param other other object to compare to
     * @return true if the contents are equal or false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        if (!super.equals(other)){
            return false; // Call to superclass equals
        }

        Exam that = (Exam) other;
        return Objects.equals(location, that.location)&&
                Objects.equals(reviewTopics, that.reviewTopics);
    }

    /**
     * used for equals
     * @return hashed 'this'
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), location,reviewTopics);
    }

}
