package com.renaud.poker_game.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.renaud.poker_game.model.Player;
import com.renaud.poker_game.service.GameService;

@RestController
public class GameController {

	private GameService gameService;
	
	public GameController( GameService gameService)
	{
		this.gameService = gameService;
	}
	
	@PostMapping("/games/create")
	public String createGame() 
	{
		return gameService.createGame();
	}
	
	@DeleteMapping( "/games/{gameId}/delete" )
	public String deleteGame( @PathVariable Long gameId ) 
	{
		return gameService.deleteGame( gameId );
	}
	
	@PostMapping( "/games/{gameId}/addPlayer" )
	public String addPlayer( @PathVariable Long gameId, @RequestBody Player player ) 
	{
		return gameService.addPlayer( gameId, player );
	}
	
	@DeleteMapping( "/games/{gameId}/players/{playerId}/delete/" )
	public String deletePlayer( @PathVariable Long gameId,  @PathVariable Long playerId ) 
	{
		return gameService.deletePlayer( gameId, playerId );
	}
	
	@PostMapping( "/games/{gameId}/createDeck" )
	public String createDeck( @PathVariable Long gameId )
	{
		return gameService.createDeck ( gameId );
	}

	@PostMapping( "/games/{gameId}/addDeck" )
	public String addDeck(@PathVariable Long gameId)
	{
		return gameService.addDeck(gameId);
	}
	
	@PutMapping( "/games/{gameId}/dealCard" )
	public String dealCard( @PathVariable Long gameId, @RequestBody Integer numberOfCard )
	{
		return gameService.dealCard( gameId, numberOfCard );
	}
	
	@GetMapping( "/games/{gameId}/getPlayersList" )
	public String getPlayersList( @PathVariable Long gameId ) 
	{
		return gameService.getPlayersListWithScore( gameId );
	}
	
	@GetMapping( "/games/{gameId}/players/{playerId}/getCardsList" )
	public String getCardsListForPlayer( @PathVariable Long gameId, @PathVariable Long playerId ) 
	{
		return gameService.getCardsListForPlayer( gameId, playerId );
	}
		
	@GetMapping( "/games/{gameId}/getCardsLeft" )
	public String getCardsCountLeft( @PathVariable Long gameId ) 
	{
		return gameService.getCardsLeftPerSuit( gameId );
	}
	
	@GetMapping( "/games/{gameId}/getCardsLeftSorted" )
	public String getCardsLeftSorted( @PathVariable Long gameId ) 
	{
		return gameService.getCardsLeftSorted( gameId );
	}
	
	@PutMapping( "/games/{gameId}/shuffleDeck" )
	public void shuffleGameDeck( @PathVariable Long gameId ) 
	{
		gameService.shuffleGameDeck( gameId );
	}
		
}
