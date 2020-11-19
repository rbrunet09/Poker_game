package com.renaud.poker_game.model;

import javax.persistence.Embeddable;

@Embeddable
public class Card {
	
	public enum Suit 
	{
		HEARTS,
		SPADES,
		CLUBS,
		DIAMONDS
	}
	
	public enum Value 
	{
		DUMMY,
		ACE,
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		TEN,
		JACK,
		QUEEN,
		KING
	}
	
	private Suit suit;
	private Value value;
	
	public Card()
	{
		
	}

	public Card(Suit suit, Value value)
	{
		this.suit = suit;
		this.value = value;
	}
	
	public Suit getSuit()
	{
		return suit;
	}
	
	public void setSuit(Suit suit)
	{
		this.suit = suit;
	}
	
	public Value getValue()
	{
		return value;
	}
	
	public void setValue( Value value )
	{
		this.value = value;
	}
	
	public String toString()
	{
		return value.name() + " of " + suit.name();
	}
	
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * ( suit.ordinal() + 1 ) - value.ordinal();  
        return result;
    }
	
	 @Override
	 public boolean equals(Object obj) 
	 {
		 if (this == obj)
		     return true;
		 
		 if (obj == null)
		     return false;
		 
		 if (getClass() != obj.getClass())
		     return false;
		 
		 Card otherCard = (Card) obj;
		 
		 if ( value != otherCard.getValue() || suit != otherCard.getSuit() )
		     return false;
		 
		 return true;
	 }

}
