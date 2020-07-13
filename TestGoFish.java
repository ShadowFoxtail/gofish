// Name: Anthony Yingling
// File: TestGoFish.java
// Date: 3/5/2020
// Final Project
// Desc: The test file that runs GoFish
// Programmer's Notes:  This has been a wonderful experience and yes, I say this as I finish all my coding and run my
//                      final tests on it.  As much as it has stressed me out this past week, I will always keep these
//                      files as a way to remember the fun I sincerely had in this class.  Thank you so much for being
//                      probably one of the best teachers I've had here at GFCMSU... well, you and Steve Robinett are
//                      tied for first, of course. :)


package Final;

import java.util.Scanner;

public class TestGoFish
{
    // Constant Declaration
    private static String PROGNAME = "Go Fish";
    private static String VERSION = "1.4";
    protected static String NL = Foxlib.nextLine;
    private static Scanner input;

    public static void main(String[] args)          // Runs the main program
    {
        // Local Variable Init
        String response;
        boolean cont = true;
        boolean tryagain;

        // Scanner init
        input = new Scanner(System.in);

        Foxlib.header(PROGNAME, VERSION);   // Shows my header from Foxlib (honestly, that shared library has saved me so much code since I built it)
        Foxlib.greeting();                  // Shows my default greeting
        System.out.println(NL + "You are about to experience my greatest piece of programming prowess that I have yet to demonstrate" + NL +
            "and I could NOT be more proud of this achievement.  Thank you so much for allowing me this experience." + NL +
            "It has been a wild ride.  Now, are you ready?" + NL);
        System.out.print("Please press any key to continue... ");
        
        // Pause for effect
        String pause = input.nextLine();
        if(pause.isBlank()){}
        
        // So long as the player keeps saying yes, this game will play and play and play and....
        while(cont == true)
        {
            System.out.println(NL + "Now loading Go Fish...");
            GoFish game = new GoFish();                         // Creates a new GoFish object
            game.playGame();                                    // Runs the game
            do                                                  // When the game is over, do you want to play again?
            {
                tryagain = false;
                System.out.print("Would you like to play again?" + NL + "[Y or N] >> ");
                response = input.nextLine();
                if(response.equalsIgnoreCase("Y"))
                {
                    System.out.println("Great!  Starting a new game!");
                    game = null;                                // If you wanna play again, it clears the original game
                }
                else if(response.equalsIgnoreCase("N")){cont = false;}
                else
                {
                    System.out.println("Invalid response.");    // Gotta check for an invalid response.
                    tryagain = true;                            // Try again!
                }
            }
            while(tryagain == true);
        }

        Foxlib.end();       // Calls my end method from Foxlib
    }
}