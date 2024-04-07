package ca.ucalgary.groupprojectgui.objects;

import java.util.Objects;

public class Assignment extends Project {

    String specialInstructions;

    public Assignment(String courseName, String projectName, double projectWeight, int[] projectDeadline, String specialInstructions) {
        super(courseName, projectName, projectWeight, projectDeadline);
        this.specialInstructions = specialInstructions;
    }

    /**
     * getter for specialInstructions
     * @return special instructions about the assignment
     */
    public String getSpecialInstructions(){
        return this.specialInstructions;
    }

    /**
     * Add special instructions to project description
     * @return string representation of object
     */
    @Override
    public String toString() {
        String instruction = String.format("   %-40s",specialInstructions);
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

        Assignment that = (Assignment) other;
        return Objects.equals(specialInstructions, that.specialInstructions);
    }

    /**
     * used for equals
     * @return hashed 'this'
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), specialInstructions);
    }
}
