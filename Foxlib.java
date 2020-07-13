// Foxlib Version 1.1
// This is merely a library that I'm going to be using to preload more commonly used commands and strings to
// hopefully save on lines of code later on.

package Final;

public class Foxlib
{
    // Common variables
    public static String nextLine = "\n";                                   // Easy way for me to add next line escape characters into my code

    // Common methods
    public static void header(String progName, String version)              // Default header to go on top of my program outputs.
    {
        System.out.println(progName + " Program ver " + version + "\n" +
            "Copyright (c) 2020 by Shadow Foxtail.  All rights reserved.");
    }
    public static void greeting()
    {
        System.out.print(nextLine + "Hello human!" + nextLine);             // Default greeting to the user.
    }
    public static void end()                                                // Default ending message to go on the bottom of my program outputs.
    {
        System.out.println("\n" + "Until next time!" + "\n" + "END");
    }
}
