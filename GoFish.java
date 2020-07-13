// Name: Anthony Yingling
// File: GoFish.java
// Date: 3/2/2020
// Final Project
// Desc: All the methods that make up the game Go Fish

package Final;

import Final.Player.Gender;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.*;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;

/** Main Class for the Go Fish Game
 *  Copyright by Shadow Foxtail (c) 2020
 *  All rights reserved.
 */
public class GoFish
{
    // Constants
    private static String NL = Foxlib.nextLine;

    // Global Data Fields
    public static Deck d;
    private int STARTING_HAND;      // Number of cards each player starts with.
    static Scanner input;
    static ArrayList<Player> players = new ArrayList<Player>();
    ArrayList<Player> savedPlayers = new ArrayList<Player>();
    final static String DELIMITER = ",";

    // Path Init
    static Path playerList = Paths.get("Week 8\\Final\\GoFishPlayers.txt");

    /** Constructor to start a new game */
    public GoFish()
    {
        // Create and Shuffle deck
        d = new Deck();
        d.shuffle(true);
    }   

    /** Method to add the computer player */
    public void addComputerPlayer()
    {
        // Local Variable Init
        boolean computerFound = false;

        System.out.println(NL + "Adding computer player...");
        for(int i = 0; i < savedPlayers.size(); i++)
        {
            // Looks for the Player named "Computer"
            if(savedPlayers.get(i).playerName.equals("Computer"))
            {
                players.add(savedPlayers.get(i));
                computerFound = true;                
            }
        }
        // Returns an error if the computer for whatever reason wasn't found.
        if(computerFound == false){System.out.println("Error: Computer player not found!");}
    }
    /** Add new player method */
    public void addPlayer()
    {
        // Local Variable Init
        String name;
        String sex;
        Gender gender = Gender.Neutral;         // Default gender
        boolean sentinel = false;

        // Scanner init
        input = new Scanner(System.in);
        
        System.out.print("What is your name? ");
        name = input.nextLine();
        
        // Begin logic loop, ending when a valid name was chosen
        while(sentinel == false)
        {
            // Ensures no one can name themselves the add command.
            if(name.equalsIgnoreCase("Add") || name.equals(""))
            {
                System.out.print("You cannot name a player that.  Try again: ");
                name = input.nextLine();
            }
            // for loop that looks to see if the name already exists
            for(Player p : savedPlayers)
            {
                if(name.equalsIgnoreCase(p.playerName))
                {
                    System.out.print("This name already exists.  Try again: ");
                    name = input.nextLine();
                    break;
                }
                else{sentinel = true;}
            }
            
        }
        System.out.print(NL + "M = Male, F = Female, N = Neutral/Other" + NL +
                "Okay, " + name + ", what is your gender? [M,F,N] ");
        sex = input.nextLine();
        sex = sex.toUpperCase();
        // Ensures that no one can make themselves an AI.
        if(sex.equals("C"))
        {
            System.out.println("You cannot make yourself a computer.  Defaulting to Neutral/Other.");
            sex = "N";
        }
        // Parses the letter into the Gender enum
        gender = Gender.parseSex(sex);
        System.out.println("Creating player...");

        // Adds the player to both the savedPlayers array and the current players (players) array.
        savedPlayers.add(new Player(name, gender));
        players.add(new Player(name, gender));
    }
    /** Check for End of Game method */
    public boolean checkforEOG()
    {
        // Initializes the total score variable
        int totalScore = 0;

        // for loop that runs through the players in the game and counts up the total score
        for (Player p : players)
        {
            totalScore += p.getScore();
        }
        if (totalScore >= 13){return true;}     // If the combined score is 13 (or somehow higher), then all 13 'books' have been played and
        else{return false;}                     // the game is over.
    }
    /** Starting hand method */
    public void dealCards(int numPlayers)
    {
        // Init Players' hands
        for(int i = 0; i < numPlayers + 1; i++)
        {
            players.get(i).hand = new Hand();
        }

        // Starting hand size based on the number of players
        if(numPlayers <= 3){STARTING_HAND = 7;} // 2 - 3 players start with 7 cards each
        else{STARTING_HAND = 5;}                // 5 - 7 players start with 5 cards each

        // Deal cards to players
        for(int i = 0; i < (STARTING_HAND); i++)
        {
            for(int j = 0 ; j < numPlayers + 1; j++)
            {
                players.get(j).hand.addCard(d.deal());
            }
        }
    }
    /** Reads the GoFishPlayers.txt file, parses and stores saved players into an array for player selection and re-saving data back to the file */
    public ArrayList<Player> getPlayerList()
    {
        // Local Variable Init
        String line;
        String[] fields = new String[4];
        InputStream input = null;
        String name;
        Gender sex;
        int wins;
        int losses;

        // Local Array Init
        ArrayList<Player> chooseList = new ArrayList<Player>();

        // try/catch for opening the file and parsing out the data into usable objects
        try
        {
            input = Files.newInputStream(playerList);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            line = reader.readLine();
            while(line != null)
            {
                fields = line.split(DELIMITER);
                name = fields[0];
                sex = Gender.parseSex(fields[1]);           // Parses the sex string into a Gender enum
                wins = Integer.parseInt(fields[2]);         // Parses the wins string into an integer
                losses = Integer.parseInt(fields[3]);       // Parses the losses string into an integer

                // If the gender is AI, it creates it as a Dealer class
                if(sex.equals(Gender.AI)){chooseList.add(new Dealer(name, sex, wins, losses));}
                // Else, it creates it as a Player class
                else{chooseList.add(new Player(name, sex, wins, losses));}
                line = reader.readLine();                   // Reads the next line
            }
            reader.close();                                 // Closes the reader
        }
        catch(IOException e)                                // Catches any IO Exceptions
        {
            System.out.println("Error: An IO Exception has occurred.");
            e.getStackTrace();
        }
        catch(NumberFormatException e)              // Catches any number formatting errors which can happen if the file isn't formatted right.
        {
            System.out.println("There was an error while trying to convert a String into an integer.  Please check the GoFishPlayers.txt" + NL +
                "to make sure it is formatted currently (name,gender,wins,losses).");
        }
        catch(Exception e)                          // Catches anything I missed.
        {
            System.out.println("An unknown exception occured.  Please send Shadow Foxtail the following stacktrace message:");
            System.out.println(e.getStackTrace().toString());
        }

        return chooseList;                          // Returns the list to be used by the game
    }
    /** Play Game method called by the TestGoFish.java, or this class's "main" function */
    public void playGame()
    {
        // Local Variable Init
        int numPlayers = 0;     
        boolean correct = true;
        String pause;
        input = new Scanner(System.in);

        // Show Instructions
        showInstructions();

        // Pause for effect
        System.out.print("Type any key to continue... ");
        pause = input.nextLine();

        // Just to clear the "Value of the variable is never used" message for pause, since it is only used to
        // pause the spam between turns.
        if(pause.isBlank()){}

        // Get PlayerList
        savedPlayers = getPlayerList();

        // Show Players to Choose From or create their own
        System.out.println(NL + "Now displaying current players saved to disk...");
        showSavedPlayers();

        // Get number of players
        do
        {
            try
            {
                correct = true;
                System.out.print(NL + "How many human players are playing in this game? ");
                numPlayers = input.nextInt();
            }
            catch(InputMismatchException e)                 // Catches if an int wasn't entered.
            {
                System.out.println("You must enter a number.");
                correct = false;
            }
            catch(Exception e)                              // Catches anything else that missed.
            {
                System.out.println("You entered an invalid answer.");
                correct = false;
            }
            // Finally, it eats the return character so any further inputs get read correctly
            finally
            {
                input.nextLine();
            }
            if(numPlayers == 0)                             // Checks to see if you think the computer needs to play by itself (it can't)
            {
                System.out.println("You need at least one human player.");
                correct = false;
            }
            else if(numPlayers > 8)                         // Had to place a limit of 7 players on this game or else things might've gotten
            {                                               // a little unpredictable.  To be fair, I only tested this with 3 players (2 human, one AI)
                System.out.println("There's too many players to handle in this game.");
                correct = false;
            }
        }
        while(correct == false);                            // for..while to keep going until it gets a valid answer.
        


        // Choose who's playing
        selectPlayers(numPlayers);
        
        // Add computer player
        addComputerPlayer();
        
        // Display players in game
        System.out.println(NL + "Starting game with the following players...");
        for(Player p : players)
        {
            System.out.println(p.display());
        }


        // Deal Cards
        dealCards(numPlayers);
        

        System.out.println(NL + "Let's play Go Fish!!" + NL + NL + "-----------------------------");

        // Play turns until there's a winner
        boolean endOfGame = false;
        while(endOfGame == false)
        {
            for(Player p : players)
            {
                System.out.println();
                p.takeTurn();
                p.checkForBooks();
                endOfGame = checkforEOG();
                if(p.hand.getHand().size() == 0)
                {
                    System.out.println("Player " + p.getPlayerName() + " has no more cards left.  House rules state that the game will end.");
                    endOfGame = true;
                }
                if(endOfGame){break;}
                // Pause for effect (and spam prevention)
                System.out.print("End of turn.  Type any key to continue: ");
                pause = input.nextLine();
            }
        }

        // Finds the winner
        int highscore = 0;
        String highPlayer = "";
        for(Player p : players)                     // Runs through the players and records who has the highest score
        {
            if(p.getScore() > highscore)
            {
                // That player gets instantiated to be read and recorded
                highscore = p.getScore();
                highPlayer = p.getPlayerName();
            }
        }

        // Reports the winner
        System.out.println("At the end of the game, with " + highscore + " points, " + highPlayer + " is our winner!");

        // For loop that updates the winner and loser(s) win/loss records
        for(Player p : players)
        {
            if(highPlayer.equals(p.getPlayerName())){p.wins++;} // The winner gets a point on their win
            else{p.losses++;}                                   // The loser(s) get(s) a point on their losses
        }

        // Saves the player data
        savePlayers();

        // End Message
        System.out.println(NL + NL + "The win and loss(es) have been successfully recorded!  Thank you for playing!");
    }
    /** Method to save the players in the GoFishPlayers.txt file for future games. */
    public void savePlayers()
    {
        // Output Init
        OutputStream output = null;

        // Local Variable Init
        String entry = "";

        // Saves the players in previous game to array of all players
        for(Player p: players)
        {
            for(int i = 0; i < savedPlayers.size(); i++)
            {
                if(p.getPlayerName() == savedPlayers.get(i).getPlayerName())
                {
                    savedPlayers.get(i).wins = p.wins;
                    savedPlayers.get(i).losses = p.losses;
                }
            }
        }

        // Deletes previous file
        try
        {
            if(Files.exists(playerList)){Files.delete(playerList);}
        }
        catch(IOException e)            // Catches any IO Exceptions
        {
            System.out.println("Error: " + e.getMessage() + " returned an IO Exception.");
            e.getStackTrace().toString();
        }

        // Saves the players to GoFishPlayers.txt
        try
        {
            output = new BufferedOutputStream(Files.newOutputStream(playerList, CREATE));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

            // for loop that iterates over all the players in the savedPlayers ArrayList and formats them for transfer to the file
            for(int i = 0; i < savedPlayers.size(); i++)
            {
                entry = savedPlayers.get(i).getPlayerName() + DELIMITER + savedPlayers.get(i).getPlayerGender().letter +
                    DELIMITER + savedPlayers.get(i).getWins() + DELIMITER + savedPlayers.get(i).getLosses();
                
                writer.write(entry, 0, entry.length()); // Writes the data to the file
                writer.newLine();       // Moves to the next line
            }

            writer.flush();             // Flushes the stream
            writer.close();             // Closes the steam
        }
        catch(IOException e)            // Catches any IO Exceptions
        {
            System.out.println("Error: " + e.getMessage() + " returned an IO Execption.");
            e.getStackTrace().toString();
        }
        catch(Exception e)              // Catches anything I missed.
        {
            System.out.println("An unexpected exception occured.  Please send the programmer this stack trace:");
            e.getStackTrace().toString();
        }
    }
    /** Method to select a player from the list given or an option to add a new player. */
    public void selectPlayers(int numPlayers)
    {
        // Scanner Init
        input = new Scanner(System.in);

        // Local Variable Init
        String response;

        System.out.println("Please type your name from the list above or type 'Add' to add a new player.");
        // for loop that uses numPlayers (# of human players) to add/create players
        for(int i = 0; i < numPlayers; i++)
        {
            System.out.print("Player " + (i + 1) + ": ");
            response = input.nextLine();
            boolean nameChoosen = false;        // Initializes sentinel boolean
            while(nameChoosen == false)         // while loop that checks the response for validity
            {
                if(response.equalsIgnoreCase("Computer"))   // Prevents the player from being the computer.  We are not the Borg.
                {
                    System.out.print("You can't choose the computer player. Please try again:");
                    response = input.nextLine();
                }
                else if(response.equalsIgnoreCase("Add"))   // If they type 'add', moves to the addPlayer method.
                {
                    System.out.println("Entering new player: ");
                    addPlayer();
                    nameChoosen = true;                     // Once out of the addPlayer method, sets the sentinel to let the user out
                }
                else                                        // else gate that attempts to add the player from the saved list
                {
                    // Iterates through the savedPlayers ArrayList, looking for a match
                    for(int j = 0; j < savedPlayers.size(); j++)
                    {
                        if(response.equalsIgnoreCase(savedPlayers.get(j).playerName))   // If a match is found, adds them to the game
                        {
                            players.add(savedPlayers.get(j));
                            System.out.println("Player " + savedPlayers.get(j).playerName + " has been added to the game!");
                            nameChoosen = true;             // Lets the player out of the loop
                        }
                    }
                }
                if(nameChoosen == false)                    // If, after all that, a player wasn't chosen, send them to the top to try again.
                {
                    System.out.print("Invalid name. Please try again: ");
                    response = input.nextLine();
                }
            }
        } 
    }
    /** Shows Instructions for the Game.*/
    public void showInstructions()
    {
        System.out.println("          ----------------" + "  Instructions" + "----------------" + NL + NL +
            "1) The rules of play follow offical Bicycle rules.  When a player asks for a card that their opponent has multiple of," + NL +
            "the opponent gives up all the cards.  For example, Player One has three Kings and Player Two asks for Kings.  Player One" + NL +
            "gives Player Two all three Kings.  To score, you must have four of a kind.  Play continues until all 'books' have been played" + NL +
            "or until one player has no more cards in their hand by the end of their turn." + NL +
            "Player with the most scored books wins." + NL +
            "2) When asking for a card, it is important to note the following: a) You need to type in the full face value (i.e. 'King' or" + NL +
            "'Three'.  b) As of this version, case sensitivity is not important, however, there is a known bug that may require you to spell" + NL +
            "the face either all lowercase, or CamelCase ('king' or 'King' respectively).  The programmer is still hard at work debugging this" + NL +
            "issue and thanks you for your patience in this matter.  For now, should this error occur, try switching the case on the spelling." + NL +
            "3) Finally, I thank you for trying this game and if you any comments, questions or concerns, you may reach Shadow Foxtail (that's me!)" + NL +
            "at s.foxtail@gmail.com.  Finally, have fun!" + NL + NL +
            "          ----------------------------------------------" + NL + NL);
    }
    /** Prints out the list of saved players, pulled from GoFishPlayers.txt */
    public void showSavedPlayers()
    {
        System.out.println("Player List" + NL + "-----------");
        // Iterates through the savedPlayers ArrayList and displays each member in turn.
        for(int i = 0; i < savedPlayers.size(); i ++)
        {
            System.out.println(savedPlayers.get(i).display());
        }
    }
}
// Longest code that I have EVER written!!