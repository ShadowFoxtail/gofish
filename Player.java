// Name: Anthony Yingling
// File: Player.java
// Date: 3/2/2020
// Final Project
// Desc: A Player class for my GoFish Final project.

package Final;

import java.util.Scanner;
import Final.CardTypes.Face;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Player
{
    // Declaring nextLine variable
    final protected static String NL = Foxlib.nextLine;

    // Scanner init
    static Scanner input;

    // I had intended to use this for personal pronoun usage in the game but in the end, I decided against it, so it's here for fluff
    // and to identify the AI character

    /**
     * Gender type is the gender of the player (Male, Female, Neutral, AI (Computer))
     * @letter - data field : shortened version of the gender
     */
    public enum Gender
    {
        Male('M'), Female('F'), Neutral('N'), AI('C');
        char letter;
        Gender(char l)
        {
            letter = l;
        }
        // Enum method that can return a Gender enum for a passed String using a switch
        public static Gender parseSex(String sex)
        {
                switch(sex)
            {
                case "M":
                    return Gender.Male;
                case "F":
                    return Gender.Female;
                case "N":
                    return Gender.Neutral;
                case "C":
                    return Gender.AI;
                default:
                    System.out.println("Invalid response, defaulting to Neutral/Other");
                    return Gender.Neutral;
            }
        }
    }

    // Data Fields
    protected String playerName;
    protected Gender gender;
    protected int wins;
    protected int losses;
    protected int score;
    protected Hand hand;
    protected Face choice;
    

    // Constructors
    public Player()
    {
        this.playerName = "Anonymous";
        this.gender = Gender.Neutral;
        wins = 0;
        losses = 0;
        hand = new Hand();
    }
    public Player(String name, Gender gender)
    {
        this.playerName = name;
        this.gender = gender;
        wins = 0;
        losses = 0;
        hand = new Hand();
    }
    public Player(String name,Gender gender, int wins, int losses)
    {
        this.playerName = name;
        this.gender = gender;
        this.wins = wins;
        this.losses = losses;
        hand = new Hand();
    }

    // Get Methods
    /** Returns the name of the player */
    public String getPlayerName()
    {
        return playerName;
    }
    /** Returns the gender of the player */
    public Gender getPlayerGender()
    {
        return gender;
    }
    /** Returns the player's wins */
    public int getWins()
    {
        return wins;
    }
    /** Returns the player's losses */
    public int getLosses()
    {
        return losses;
    }
    /** Returns how many books the player has scored this game */
    public int getScore()
    {
        return score;
    }

    // Set Methods

    /** Sets the Player's Name */
    public void setPlayerName(String name)
    {
        playerName = name;
    }
    /** Sets the player's gender */
    public void setPlayerGender(Gender gender)
    {
        this.gender = gender;
    }
    /** Sets the player's wins */
    public void setPlayerWins(int wins)
    {
        this.wins = wins;
    }
    /** Sets the player's losses */
    public void setPlayerLosses(int losses)
    {
        this.losses = losses;
    }

    // Display Methods
    
    /** Displays the player, their gender, and their win/loss ratio */
    public String display()
    {
        String display = "Name: " + playerName + "\t\tSex: " + gender.letter + "\t\tWins/Losses: " + wins + "/" +
            losses;
        return display;
    }
    /** Displays the current players' scores */
    public void displayScores()
    {
        System.out.println("Players in game" + NL + "---------------" + NL + "--> " + this.playerName + "\t\tScore: " + this.score);
        for(Player p : GoFish.players)
        {
            if(p.getPlayerName() != this.playerName)
            {
                System.out.println("--- " + p.getPlayerName() + "\t\tScore: " + p.getScore());
            }
        }
    }

    /** Method to check to make sure the card the player chooses is valid */
    public void cardCheck(String response)
    {
        boolean sentinel = false;
        boolean cardFound = false;
        choice = null;

        // do..while loop that checks both if the String is a valid face value and if they have the face value already in their hand
        do
        {
            for(Face f : Face.values())
            {
                if(response.equalsIgnoreCase(f.toString()))     // Checks the response for a valid enum and assigns it a Face value
                {
                    choice = f;
                    sentinel = true;                            // Sets the sentinel to true to exit the loop so long as the following gate
                }                                               // is passed.
            }
            if(sentinel == false)
            {                                                   // If fails, reinputs and loops again
                System.out.print("Invalid choice, please try again: ");
                response = input.nextLine();
            }
            else                                                // Else, if succeeds, goes into the logic gate to check if you have the card
            {

                for(Card c : this.hand.getHand())               // For loop that looks through your hand and returns true if the chosen
                {                                               // Face value matches one of your own cards
                    if(choice.equals(c.getFace()))
                    {
                        cardFound = true;
                    }
                }
                if(cardFound == false)                          // If false, then report that to the player and have them reinput.
                {
                    System.out.println("You can't ask for a card value that you don't already have.");
                    System.err.print("Please try again: ");
                    response = input.nextLine();
                    sentinel = false;                           // Resets the sentinel to false to re-run the loop.
                }
            }
        }
        while(sentinel == false);
    }
    /** Method that is checked at the end of the turn and after every card is drawn to see if a book of 4 cards has been formed */
    public void checkForBooks()
    {
        int cardCount;
        // After initializing the count variable, start the for loop that iterates through every face value and checks if there are four
        // of that valule in your hand.
        for(Face f : Face.values())
        {
            cardCount = 0;                                      // At the start of every face value, reset the counter to zero.
            for(Card c : this.hand.getHand())
            {
                if(c.getFace() == f){cardCount++;}              // Counts every matching card in the hand.
            }
            if(cardCount == 4)                                  // If there are four cards in the hand of a matching face value...
            {
                this.score++;                                   // Add one point to your score,
                System.out.println(this.playerName + " has 4 " + f.toString() + "s, places them on the table and scores 1 point!");
                Face faceWon = f;
                ArrayList<Card> tempHand = this.hand.getHand();
                for (int i = 0; i < tempHand.size();)           // And run a for loop that removes the cards from the game, placing them on the
                {                                               // table, never to be seen again.
                    Card cardsWon = tempHand.get(i);
                    if(cardsWon.getFace() == faceWon)
                    {
                        this.hand.removeCard(cardsWon);
                    } 
                    else{i++;}
                }
                cardCount = 0;                                  // Resets the count for the next check.  Unneeded redundancy?
            }
        }
    }
    /** Method to see whether or not the player has a 'catch' or not. */
    public boolean checkForCatch(String response, Player player)
    {
        // Local Variable Init
        boolean cardGiven;
        choice = null;

        // Local Scanner init
        input = new Scanner(System.in);

        // First, check to see if the card is valid
        cardCheck(response);
 
        // Initalize cardGiven
        cardGiven = false;

        // Set up a temporary hand to iterate through, creating a parallel array
        ArrayList<Card> tempHand = player.hand.getHand();
        
        // For loop to iterate through the temporary hand
        for(int i = 0; i < tempHand.size();)
        {
            // Instanciates the next card from the temp hand
            Card c = tempHand.get(i);
            if(choice.equals(c.getFace()))
            {
                cardGiven = true;
                this.hand.getHand().add(c);                 // Uses the parallel array for iteration but adds and removes cards
                player.hand.getHand().remove(c);            // from the actual hands to avoid a ConcurrentModifcationException
                System.out.println(player.playerName + " has a " + choice.toString() + " and gives you their " + c.display() + ".");
            }
            else{i++;}                                      // If that's not the card we were looking for, go to the next
        }
        if(cardGiven == false)                              // If a card wasn't found, report as such and go fish.
        {
            System.out.println(player.playerName + " does not have a " + choice.toString() + ".  Go Fish!");
            cardGiven = goFish(choice);                     // Go Fish, passing the face value to the method to see if they draw the card
        }                                                   // they had asked for.
        return cardGiven;                                   // Returns the boolean.  True, they go again, false, next turm.
    }
    /** The method in which the player draws a card from the deck, checking to see if the card drawn was one they had asked for. */
    public boolean goFish(Face choice)
    {
        // Local Variable Init
        Card cardDrawn = null;

        // First, see if you can draw a card
        if(GoFish.d.remainingCards() == 0){System.out.println("The deck is empty and thus, you can fish from it no longer.");}
        else                                                // If so, hey, draw a card.
        {
            cardDrawn = GoFish.d.deal();
            // Players get to see the card they draw as they draw it.
            System.out.println("Player " + this.playerName + " draws a card and gets a " + cardDrawn.display() + ".");
            this.hand.getHand().add(cardDrawn);             // Adds to their deck
            if(cardDrawn.getFace() == choice){return true;} // Checks to see if the card drawn was the one they asked for.  If so, they keep going.
            else{return false;}                             // If not, their turn ends.
        }
        return false;                                       // Redundant return statement, but apparently needed for it to run.
    }
    /** Shows the player's progress in forming books */
    public void showProgress()
    {
        // Local Variable Init
        int[] progress = new int[13];       // To get a count of how many of each face card the player has
        String message = "You have:" + NL;      // Starts the message
        Face[] faces = Face.values();       // Gets the faces in an arraylist
        int line = 0;

        for(Card c : this.hand.getHand())
        {
            int check = c.value() - 1;
            for(int i = 0; i < progress.length; i++)
            {
                if(check == i)
                {
                    progress[i]++;
                    break;
                }
            }
        }

        for(int i = 0; i < progress.length; i++)
        {
            if(progress[i] != 0)
            {
                message += progress[i] + " " + faces[i];
                if(progress[i] > 1){message += "s";}
                message += "   ";
                line++;
                if(line == 3)
                {
                    message += NL;
                    line = 0;
                }
            }
        }
        System.out.println(message);

    }
    /** Sorts the cards in the player's hand */
    public void sortHand()
    {
        Collections.sort(this.hand.getHand(), new Comparator<Card>() {
        @Override
        public int compare(Card card1, Card card2) 
            {
                if(card1.getFace().value == card2.getFace().value)
                {
                    return card1.getFace().compareTo(card2.getFace());
                } 
                else 
                {
                    return card1.getFace().value.compareTo(card2.getFace().value);
                }
            }
        });
    }
    /** Player's Turn Method */
    public void takeTurn()
    {
        // Local Variable Init
        String response;
        String turnName = "";
        Player player = null;
        boolean goAgain = false;
        boolean selfCheck = false;

        // Scanner Init
        input = new Scanner(System.in);

        // Do loop that runs through the actions needed to complete a turn.
        do
        {
            // This part displays the player's hand and the remaining cards in the deck/
            sortHand();
            System.out.println("Player " + this.playerName + ", it is your turn!" + NL);
            displayScores();
            System.out.println(NL + "Cards in your Hand (" + this.hand.getHand().size() + ")" + NL + "------------------");
            hand.showHand();
            System.out.println();
            showProgress();
            System.out.println(NL + "Cards remaining in deck: " + GoFish.d.remainingCards() + NL);

            // If there only two players, why bother asking who you're asking for a card from?  It's obviously the only other player in the
            // game, which in this case, is the computer player.
            if(GoFish.players.size() != 2)
            {
                // If three or more players, then ask....
                System.out.print(NL + "Who would you like to ask for a card from? ");
                response = input.nextLine();

                boolean sentinel = false;
                while(sentinel == false)
                {
                    // Logic gate to see if the player asked is a player in this game.
                    for(Player p : GoFish.players)
                    {
                        if(response.equalsIgnoreCase(p.getPlayerName()))
                        {
                            turnName = p.playerName;
                            player = p;
                            sentinel = true;

                            // Checks to see if the player chose themself
                            if(response.equalsIgnoreCase(this.getPlayerName()))
                            {
                                System.out.print("You cannot choose yourself.  Please try again: ");
                                response = input.nextLine();
                                selfCheck = true;
                                sentinel = false;
                                break;
                            }
                            break;
                        }
                        
                    }
                    if(sentinel == false && selfCheck == false)
                    {
                    System.out.print(response + " is not a player in this game.  Please try again: ");
                    response = input.nextLine();
                    }
                }
            }
            else
            {
                turnName = "Computer";
                player = GoFish.players.get(1);
            }
            // This line is to separate turns to make them look more pleasing to the eye.
            System.out.println("-----------------------------");
            System.out.print("You ask, \"Player " + turnName + ", do you have a: ");
            response = input.nextLine();
            cardCheck(response);
            goAgain = checkForCatch(response, player);
            if(goAgain)
            {
                System.out.println("You got a catch and may play again!");
                checkForBooks();
            }
        }
        while(goAgain == true && this.hand.getHand().size() != 0);      // If they can go again and they still have cards in their hand, continue.
    }
}