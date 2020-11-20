package com.renaud.poker_game.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.renaud.poker_game.model.Game;
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
	public ResponseEntity<Game> createGame() 
	{
		return gameService.createGame();
	}
	
	@DeleteMapping( "/games/{gameId}/delete" )
	public ResponseEntity<String> deleteGame( @PathVariable Long gameId ) 
	{
		return gameService.deleteGame( gameId );
	}
	
	@GetMapping("/games/{gameId}")
	public ResponseEntity<?> getGame( @PathVariable Long gameId ) 
	{
		return gameService.getGame( gameId );
	}
	
	@PostMapping( "/games/{gameId}/addPlayer" )
	public ResponseEntity<?> addPlayer( @PathVariable Long gameId, @RequestBody Player player ) 
	{
		return gameService.addPlayer( gameId, player );
	}
	
	@DeleteMapping( "/games/{gameId}/players/{playerId}/delete" )
	public ResponseEntity<?> deletePlayer( @PathVariable Long gameId,  @PathVariable Long playerId ) 
	{
		return gameService.deletePlayer( gameId, playerId );
	}
	
	@GetMapping("/games/{gameId}/players/{playerId}")
	public ResponseEntity<?> getPlayer( @PathVariable Long gameId,  @PathVariable Long playerId ) 
	{
		return gameService.getPlayer( gameId, playerId );
	}
	
	@PutMapping( "/games/{gameId}/createDeck" )
	public ResponseEntity<?> createDeck( @PathVariable Long gameId )
	{
		return gameService.createDeck ( gameId );
	}

	@PutMapping( "/games/{gameId}/addDeck" )
	public ResponseEntity<?> addDeck(@PathVariable Long gameId)
	{
		return gameService.addDeck(gameId);
	}
	
	@PutMapping( "/games/{gameId}/dealCard" )
	public ResponseEntity<?> dealCard( @PathVariable Long gameId, @RequestBody Integer numberOfCard )
	{
		return gameService.dealCard( gameId, numberOfCard );
	}
	
	@GetMapping( "/games/{gameId}/getPlayersList" )
	public ResponseEntity<?> getPlayersList( @PathVariable Long gameId ) 
	{
		return gameService.getPlayersListWithScore( gameId );
	}
	
	@GetMapping( "/games/{gameId}/players/{playerId}/getCardsListForPlayer" )
	public ResponseEntity<?> getCardsListForPlayer( @PathVariable Long gameId, @PathVariable Long playerId ) 
	{
		return gameService.getCardsListForPlayer( gameId, playerId );
	}
		
	@GetMapping( "/games/{gameId}/getCardsLeftPerSuit" )
	public ResponseEntity<?> getCardsLeftPerSuit( @PathVariable Long gameId ) 
	{
		return gameService.getCardsLeftPerSuit( gameId );
	}
	
	@GetMapping( "/games/{gameId}/getCardsLeftSorted" )
	public ResponseEntity<?> getCardsLeftSorted( @PathVariable Long gameId ) 
	{
		return gameService.getCardsLeftSorted( gameId );
	}
	
	@PutMapping( "/games/{gameId}/shuffleDeck" )
	public void shuffleGameDeck( @PathVariable Long gameId ) 
	{
		gameService.shuffleGameDeck( gameId );
	}
		
}
