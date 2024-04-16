package ca.ucalgary.groupprojectgui;

import java.io.File;

/**
 * CPSC 233 W24 ca.ucalgary.groupprojectgui.objects.Project ca.ucalgary.groupprojectgui.Main.java - Main structure of the
 * project (for shell)
 *
 * @author Utaha Iwai
 * @tutorial T09
 * @email utaha.iwai@ucalgary.ca
 */

public class Main {
    public static void main(String[] args) {
        if (args.length > 2) {
            System.err.println("Expected one command line argument for filename to load from");
        }
        if (args.length == 1) {
            String filename = args[0];
            File file = new File(args[0]);
            if (!file.exists() || !file.canRead()) {
                System.err.println("Can not load from file" + filename);
                System.exit(1);
            }
            Menu.menuLoop(file);
        }
        Menu.menuLoop(null);
    }

}
