# Academic Tracker GUI

This is an academic tracker, where the user is able to input courses they
are taking at the University of Calgary. The user is able to add projects(exam/assignments)
under a course, along with information about the project. The user can then
track their grades, updating their grades after completing projects, and track their grades
based on the weight of the assignment or exam and how they are doing in the course.

The functionality of the GUI includes showing course information, project information,
as well as the grades of the user under different tabs. If the user withdraws from a course,
they are able to remove it from their dashboard, and can choose to only see courses
in progress in their dashboard. Similarly, users can mark assignments complete and
only choose to see in-progress projects.

## How to run

Ensure your system has jdk-22, and java.
Download javafx-sdk-22 from https://gluonhq.com/products/javafx/ into C:/Users/Program Files

Download jar file into directory of choice. Navigate to said directory in the terminal and paste the following into command line:

java --module-path "C:\Program Files\javafx-sdk-22\lib" --add-modules javafx.controls,javafx.fxml -jar GroupProjectGUI.jar

into command line.