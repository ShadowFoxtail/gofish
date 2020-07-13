// Name: Anthony Yingling
// Date: 3/3/2020
// File: Dealer.java
// Final Project
// Desc: Dealer subclass of Player that plays the Computer AI of the game Go Fish

package Final;

import java.util.ArrayList;
import java.util.Random;
import Final.CardTypes.Face;

public class Dealer extends Player
{
    // Data fields
    Random rng;

    public Dealer()
    {
        super();
        this.playerName = "Computer";
        this.gender = Gender.AI;
    }
    public Dealer(String name, Gender gender, int wins, int losses)
    {
        super(name, gender, wins, losses);
    }
    /** AI Method for randomly choosing a Face value that it has in its hand. */
    public String aiChoice()
    {
        // Local Variable Init
        int faceChosen;
        boolean valid = false;

        // Random Number Generator Init
        rng = new Random();

        // do..while loop that gets a random value from those in its hand
        do
        {
            faceChosen = rng.nextInt(13);       // gets a random number from 0 to 12
            for(Card c : this.hand.getHand())   // for loop that checks all the cards in its hand
            {
                if(c.getFace().value == (faceChosen + 1)){valid = true;}    // if the value on the card matches the number chosen, continue
            }                                                               // Had to add the + 1 so that it matched the enum values
        }                                                                   // This is how it was 'cheating' when we talked on Teams
        while(valid == false);                  // sentinel that checks the validity of the number

        // switch to parse the number into a string to pass to the checkForCatch method.
        switch(faceChosen)
        {
            case 0:
                return "Ace";
            case 1:
                return "Two";
            case 2:
                return "Three";
            case 3:
                return "Four";
            case 4:
                return "Five";
            case 5:
                return "Six";
            case 6:
                return "Seven";
            case 7:
                return "Eight";
            case 8:
                return "Nine";
            case 9:
                return "Ten";
            case 10:
                return "Jack";
            case 11:
                return "Queen";
            case 12:
                return "King";
            default:
                System.out.println("Unknown error with AI's choice of card.");
        }
        return null;            // Redundant null return to appease the Java gods (it would error otherwise)
    }
     // Overriding the superclass checkforCatch method
     @Override
     public boolean checkForCatch(String response, Player player)
     {
         // Local Variable Init
         Face choice = null;
         boolean cardGiven;
 
         // Parses the response into a Face enum
         for(Face f : Face.values())
         {
             if(response.equals(f.toString()))
             {
                 choice = f;
             }
         }
         
         // Initializing the sentinel
         cardGiven = false;
 
         // Set up a temporary hand to iterate through, creating a parallel array
         ArrayList<Card> tempHand = player.hand.getHand();
 
         // For loop to iterate through the temporary hand
         for(int i = 0; i < tempHand.size();)
         {
             // Instanciates the next card from the temp hand
             Card c = tempHand.get(i);       // Uses the parallel array for iteration but adds and removes cards
             if(choice.equals(c.getFace()))  // from the actual hands to avoid a ConcurrentModifcationException
             {
                 cardGiven = true;
                 this.hand.getHand().add(c);
                 player.hand.getHand().remove(c);
                 System.out.println(this.playerName + " receives the " + c.display() + " from " + player.playerName + ".");
             }
             else{i++;}                      // If that's not the card we were looking for, go to the next
         }
         return cardGiven;                   // returns the boolean to tell the AI that it can go again
     }
    /** AI Method for randomly chosing a player to take cards from */
    public Player choosePlayer()
    {
        // Local Variable Init
        int playerChosen;
        boolean sentinel = false;
        
        // Random Number Generator Init
        rng = new Random();

        // do..while loop for choosing a random player
        do
        {
            playerChosen = rng.nextInt(GoFish.players.size());  // Gets a random number based on the number of players
            if(GoFish.players.get(playerChosen) == this)        // if gate that checks if the number it chose was itself
            {
                playerChosen = rng.nextInt(GoFish.players.size());  // If so, roll again
                sentinel = false;                                   // And run the loop again
            }
            else{sentinel = true;}                              // If the number is not the AI, then the loop ends
        }
        while(sentinel == false);
        return GoFish.players.get(playerChosen);                // Returns the Player object of the player chosen
    }
    // Overriding the goFish method for AI use
    @Override
    public boolean goFish(Face choice)
    {
        // Local Variable Init
        Card cardDrawn = null;

        // Checks to see if there are any cards left in the deck
        if(GoFish.d.remainingCards() == 0){System.out.println("The deck is empty and thus, you can fish from it no longer.");}
        else
        {
            cardDrawn = GoFish.d.deal();
            System.out.println("Player " + this.playerName + " draws a card.");     // Draws a card but doesn't report to the player what it is
            this.hand.getHand().add(cardDrawn);
            if(cardDrawn.getFace() == choice){return true;}                         // Checks to see if it caught the card it was looking for
            else{return false;}                                                     // if not, returns false
        }
        return false;       // Another redundant return statement to give to the Java gods in return for a complilable program
    }
    // Turn Method (@overrides the superclass)
    @Override
    public void takeTurn()      // Special AI takeTurn method.
    {
        // Local Variable Init
        Face choice = null;
        boolean goAgain = false;

        System.out.println("It is " + this.playerName + ", the AI's turn!");
        // do loop for everything the AI needs to do.
        do
        {
            // Uses a Random Number Generator to choose which player and which Face value it'll ask for from that player
            Player chosenPlayer = choosePlayer();
            String chosenCard = aiChoice();

            // Parses the string taken from aiChoice into a Face enum
            for(Face f : Face.values())
            {
                if(chosenCard.equals(f.toString()))
                {
                    choice = f;
                }
            }
            // This line is to separate turns to make them look more pleasing to the eye.
            // Following that, the AI asks the player if they have a card they've chosen.
            System.out.println("-----------------------------" + NL +
            this.playerName + " says, \"Player " + chosenPlayer.playerName + ", do you have any " + chosenCard + "s?\"");
            // The computer catches for catches
            goAgain = checkForCatch(chosenCard, chosenPlayer);
            
            // If they got a catch, they go again.
            if(goAgain)
            {
                System.out.println("The AI, " + this.playerName + " got a catch and may play again!");
                checkForBooks();                    // Checks to see if they scored any books.
            }
            else                                    // If they didn't catch anything....
            {
                System.out.println(chosenPlayer.getPlayerName() + " says, \"Nope!  Go fish!\"");
                goFish(choice);                     // Go fish!! :)
            }
            // Reports to the player(s) how many cards it has left.
            System.out.println(playerName + " has " + this.hand.getHand().size() + " cards left.");
        }
        while(goAgain == true && this.hand.getHand().size() != 0);  // Loop continues if they caught something and they have cards left
                                                                    // if they scored any books.
    }
}