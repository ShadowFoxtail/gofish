// Name: Anthony Yingling
// File: Deck.java
// Date: 2/13/2020
// Midterm/Final Project
// Desc: The deck class that will work with the TestDeck.javs

package Final;

import java.util.ArrayList;
import Final.CardTypes.*;
import java.util.Random;

public class Deck
{
    // Global Variable Init
    private ArrayList<Card> deck = new ArrayList<Card>();           // ArrayList that holds the deck of cards.
    
    public Deck()                                                   // Default constructor for a new deck.
    {
        getCards();                                                 // Gets the cards using the getCards method
    }
    private void getCards()                                         // Method to repopulate a deck, either for the first time, or when
    {       
        for(Suit s : Suit.values())                                 // for loop that goes through each suit
        {
            for(Face f : Face.values())                             // for loop that goes through each face value
            {
                deck.add(new Card(f, s));                           // Adds the card to the deck
            }
        }                                                           // The two loops combined ensures that I get all 52 cards.
    }
    public void shuffle(boolean reset)                              // Shuffles the deck based on the boolean variable
        {
        ArrayList<Card> shuffledDeck = new ArrayList<Card>();       // ArrayList that holds the deck of shuffled cards.
        Card card;                                                  // Object to store a single card
        int randint;                                                // Random int for shuffling cards.
        Random rand = new Random();                                 // Creates the random number generator

        if(reset == true)                                           // True = resets the deck to 52 cards before shuffling
        {
            deck.clear();                                           // Clears the remaining cards.
            getCards();                                             // Gets a new deck.
        }                                                           
        while(deck.size() > 0)                                      // while loop that will stop when the unshuffled deck runs out of cards
        {
            randint = rand.nextInt(deck.size());                    // Gets a random number from the current size of the deck
            card = deck.get(randint);                               // Pulls a copy of a random card out of the unshuffled deck.
            deck.remove(randint);                                   // Removes the original card from the unshuffled deck.
            shuffledDeck.add(card);                                 // Adds the copy of the card to the shuffled deck.
        }
        deck = shuffledDeck;                                        // Replaces the old deck with the new shuffled deck.
        
        // Prints a message confirming the shuffle has been completed and how many cards were in the deck.
        // Commented this out so that it didn't display in the GoFish game.
        // System.out.println("The deck of " + deck.size() + " cards has been shuffled.");
    }
    public Card deal()                                              // Deals a single card.
    {
        Card card;                                                  // Object to store a single card
        if(deck.size() - 1 < 0)                                     // If the number of cards when one is taken out would be negative
        {                                                           // Print error and
            System.out.println("ERROR: You cannot draw a card from the deck as there none left.\n");
            return card = null;                                     // return a null card to show that nothing can be returned.
        }
        else                                                        // Else, if a card can be drawn,
        {
            card = deck.get(0);                                     // create a copy of the top card (at index 0)
            deck.remove(0);                                         // and remove the original from the deck.
        }
        return card;                                                // Return the card drawn to the user.
    }
    public Card[] deal(int count)                                   // Deal a number of cards equal to 'count'.
    {
        Card[] cards;                                               // Array that stores multiple cards for use when dealing cards.
        Card card;                                                  // Object to store a single card

        if(deck.size() - count < 0)                                 // Again, if the number of cards drawn would bring the deck to a negative value,
        {                                                           // print error message and show how many cards are left vs how many to be drawn.
            System.out.println("ERROR: You cannot draw " + count + " cards as there are only " + deck.size() + " cards left in the deck.\n");
            cards = new Card[0];                                    // Null out the cards array
            return cards;                                           // Return the null array
        }
        else                                                        // Else, if the cards can be removed successfully,
        {
            cards = new Card[count];                                // Create a new cards array to hold the cards, set to the amount to be drawn.
            for(int i = 0; i < count; i++)                          // for loop to get each card
            {
                card = deck.get(0);                                 // Always pulling from the top (index 0), create a copy of the card
                deck.remove(0);                                     // remove the original from the deck,
                cards[i] = card;                                    // Add the copy to the array at the iterable index (starting at 0)
            }                                                       // Process continues until all cards have been dealt.
        }
        return cards;                                               // Returns the card array to the user.
        
    }
    public Card peek()                                              // Peeks at the top card and returns its value.
    {
        Card card;                                                  // Object to store a single card
        card = deck.get(0);                                         // Creates a copy of the top card
        return card;                                                // and returns it to the user.
    }
    public int remainingCards()                                     // Shows how many cards are left in the deck.
    {
        return deck.size();                                         // Returns the value of numCards.
    }
    public void display()                                           // Displays the current deck.
    {
        Card card;                                                  // Object to store a single card
        System.out.println("\nDisplaying the current deck:");
        int line = 0;                                               // Integer init for formatting the string of cards on a line.
                                                                    // If the number of cards is zero, print that the deck is empty.
        if(deck.size() == 0){System.out.println("The deck is empty.");}
        else                                                        // If there are cards in the deck, continue.
        {
            for(int i = 0; i < deck.size(); i++)                    // for loop that runs through every card in the deck.
            {
                card = deck.get(i);                                 // Creates a copy of the card
            
                System.out.print(card.display());                   // Uses the display function on the card to show it.
                if (i < deck.size() - 1){System.out.print(", ");}   // If there are more cards to show, add a comma,
                else{System.out.print(".");}                        // if not, add a period.
                if(line == 4)                                       // If it adds the fifth card to a line,
                {
                    System.out.println();                           // Push a return character to add a new line
                    line = 0;                                       // Resets the line counter
                }
                else{line++;}                                       // If there aren't five cards on the line, increase the counter.
            }
            
        }
        System.out.println("\n");                                   // At the very end of all this, add a new line for formatting purposes.
    }

}