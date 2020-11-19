package com.renaud.poker_game.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Player implements Comparable<Object>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="PLAYER_ID")
	private Long id;
	private String username;
	
	@ElementCollection
	private List<Card> hand;
	
	public Player()
	{
	}
	
	public Player( String username )
	{
		this.username = username;
	}
	
	public String toString()
	{
		return "Player id: " + id + " Username: " + username + " Cards: " + hand;
	}
	
	public Long getId()
	{
		return id;
	}
	
	public void setId( Long id )
	{
		this.id = id;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername( String username )
	{
		this.username = username;
	}
	
	public List<Card> getHand()
	{
		return hand;
	}
	
	public void setHand( List<Card> hand )
	{
		this.hand = hand;
	}

	@Override
	public int compareTo(Object o) {
		Player player = (Player) o; 
		
		int scorePlayer1 = 0;
		int scorePlayer2 = 0;
		
		for(Card card: hand)
		{
			scorePlayer1 += card.getValue().ordinal();
		}
		
		for(Card card: player.getHand())
		{
			scorePlayer2 += card.getValue().ordinal();
		}

		return scorePlayer1 - scorePlayer2;
	}
}
