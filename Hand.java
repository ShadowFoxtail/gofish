// Name: Anthony Yingling
// File: Hand.java
// Date: 3/2/2020
// Final Project
// Desc: The class for a hand of cards in a game of Go Fish for the final project

package Final;

import java.util.ArrayList;

public class Hand
{
    // Data Fields
    private ArrayList<Card> hand;

    // Constructor
    public Hand()
    {
        hand = new ArrayList<Card>();
    }

    // Get Method
    public void showHand()
    {
        int line = 0;
        String cards = "";

        for(Card c : hand)
        {
            cards += c.display() + "   ";
            if(line == 3)
            {
                cards += "\n";
                line = 0;
            }
            else{line++;}
        }
        System.out.println(cards);
    }
    public ArrayList<Card> getHand()
    {
        return hand;
    }
    
    // Add card method
    public void addCard(Card c)
    {
        this.hand.add(c);
    }

    // Remove card method
    public void removeCard(Card c)
    {
        this.hand.remove(c);
    }

    // Add Cards Method(?)
}