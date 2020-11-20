package com.renaud.poker_game.service;

import org.springframework.http.ResponseEntity;

import com.renaud.poker_game.model.Game;
import com.renaud.poker_game.model.Player;

public interface GameService {

	ResponseEntity<Game> createGame();
	
	ResponseEntity<String> deleteGame(Long gameId);
	
	ResponseEntity<?> getGame(Long gameId);

	ResponseEntity<?> addPlayer(Long gameId, Player player);

	ResponseEntity<?> deletePlayer(Long gameId, Long playerId);
	
	ResponseEntity<?> getPlayer(Long gameId, Long playerId);

	ResponseEntity<?> createDeck(Long gameId);

	ResponseEntity<?> addDeck(Long gameId);

	ResponseEntity<?> dealCard(Long gameId, Integer numberOfCard);

	ResponseEntity<?> getPlayersListWithScore(Long gameId);

	ResponseEntity<?> getCardsListForPlayer(Long gameId, Long playerId);

	ResponseEntity<?> getCardsLeftPerSuit(Long gameId);

	ResponseEntity<?> getCardsLeftSorted(Long gameId);

	void shuffleGameDeck(Long gameId);

}