package com.renaud.poker_game.service;

import com.renaud.poker_game.model.Player;

public interface GameService {

	String createGame();

	String deleteGame(Long gameId);

	String addPlayer(Long gameId, Player player);

	String deletePlayer(Long gameId, Long playerId);

	String createDeck(Long gameId);

	String addDeck(Long gameId);

	String dealCard(Long gameId, Integer numberOfCard);

	String getPlayersListWithScore(Long gameId);

	String getCardsListForPlayer(Long gameId, Long playerId);

	String getCardsLeftPerSuit(Long gameId);

	String getCardsLeftSorted(Long gameId);

	void shuffleGameDeck(Long gameId);

}