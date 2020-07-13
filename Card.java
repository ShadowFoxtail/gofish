// Name: Anthony Yingling
// File: Card.java
// Date: 2/13/2020
// Midterm/Final Project
// Desc: The Card class that will define a card and its suits and numbers

package Final;

import Final.CardTypes.*;

public class Card
{
    // Variable Init
    private Face cardFace;              // Every card has a face value
    private Suit cardSuit;              // and a suit.
    
    public Card(Face face, Suit suit)   // Default constructor, takes a face value and a suit.
    {
        this.cardFace = face;           // Sets the face
        this.cardSuit = suit;           // Sets the suit
    }

    // Get Methods
    public Face getFace()
    {
        return this.cardFace;
    }
    public Suit getSuit()
    {
        return this.cardSuit;
    }
    public int value()
    {
        return this.cardFace.value;
    }

    // Set Methods
    public void setFace(Face face)
    {
        this.cardFace = face;
    }
    public void setSuit(Suit suit)
    {
        this.cardSuit = suit;
    }
    
    // Display Methods
    public String display()
    {
        return this.cardFace + " of " + this.cardSuit;
    }
    public String displayShort()
    {
        return this.cardFace.icon + "" + this.cardSuit.symbol;
    }
}