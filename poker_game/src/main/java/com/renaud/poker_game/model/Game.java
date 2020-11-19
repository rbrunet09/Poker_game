package com.renaud.poker_game.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Game {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="GAME_ID")
	private Long id;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Player> playersList;
	
	@Embedded
	private Deck deck;
	
	public Game()
	{
	}
		
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public List<Player> getPlayersList()
	{
		return playersList;
	}
	
	public void setPlayersList( List<Player> playersList )
	{
		this.playersList = playersList;
	}
	
	public Deck getDeck()
	{
		return deck;
	}
	
	public void setDeck( Deck deck )
	{
		this.deck = deck;
	}
	
	public String toString()
	{
		return "Game " + id + " with players " + playersList + " and with deck " + deck;
	}

	
}
