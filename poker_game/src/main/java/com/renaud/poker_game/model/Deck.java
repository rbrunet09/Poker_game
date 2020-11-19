package com.renaud.poker_game.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;


@Embeddable
public class Deck {
	
	@ElementCollection
	private List<Card> cardsList;
	
	public Deck()
	{
		cardsList = new ArrayList<Card>();
		for (Card.Suit suit : Card.Suit.values()) 
		{ 
			for (Card.Value number : Card.Value.values())
		    {
				if(number == Card.Value.DUMMY)
					continue;
		    	Card card = new Card(suit, number);
		    	cardsList.add(card);
		    }
		}
	}
	
	public List<Card> getCardsStack()
	{
		return cardsList;
	}
	
	public void setCardsStack( List<Card> cardsList )
	{
		this.cardsList = cardsList;
	}
	
	public String toString()
	{
		return "Deck has this list of cards: " + cardsList;
	}
}
