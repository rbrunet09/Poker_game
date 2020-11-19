package com.renaud.poker_game.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.renaud.poker_game.model.Card;
import com.renaud.poker_game.model.Deck;
import com.renaud.poker_game.model.Game;
import com.renaud.poker_game.model.Player;
import com.renaud.poker_game.repository.GameRepository;

@Service("GameService")
public class GameServiceImpl implements GameService {

	private GameRepository gameRepository;
	
	public GameServiceImpl( GameRepository gameRepository )
	{
		this.gameRepository = gameRepository;
	}

	@Override
	public String createGame() 
	{
		Game game = new Game();
		Game gameState = gameRepository.save( game );
		return gameState + " was created.";
	}

	@Override
	public String deleteGame(Long gameId) {
		gameRepository.deleteById( gameId );
		return "Game " + gameId + " was deleted.";
	}

	@Override
	public String addPlayer(Long gameId, Player player) {
		
		Optional<Game> optionalGame = gameRepository.findById(gameId);
		if(optionalGame.isEmpty())
			return "Game doesn't exist with id " + gameId;
		
		Game game = optionalGame.get();
		List<Player> playerList = game.getPlayersList();
		playerList.add(player);
		game.setPlayersList(playerList);
		
		Game gameState = gameRepository.save(game);
		
		return gameState.toString();
	}
	
	@Override
	public String deletePlayer( Long gameId, Long playerId )
	{
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return "Game doesn't exist";
		
		Game game = optionalGame.get();
		List<Player> playerList = game.getPlayersList();
		playerList.removeIf( player -> player.getId() == playerId );
		game.setPlayersList(playerList);
		
		Game gameState = gameRepository.save(game);
		
		return gameState.toString();
	}

	@Override
	public String createDeck(Long gameId) {
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return "Game doesn't exist";
		
		Game game = optionalGame.get();
		if( game.getDeck() != null && !game.getDeck().getCardsStack().isEmpty() )
		{
			return "Game " + gameId + " deck is not empty. You can only add deck.";
		}
		
		Deck deck = new Deck();
		game.setDeck(deck);
		
		Game gameState = gameRepository.save(game);
		
		return gameState.toString();
	}

	@Override
	public String addDeck(Long gameId) {
		
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return "Game doesn't exist";
		
		Game game = optionalGame.get();
		Deck gameDeck = game.getDeck();
		if( gameDeck == null )
		{
			Deck deck = new Deck();
			game.setDeck(deck);
			
			gameRepository.save(game);
			
			return "The game had no deck so a deck was created for game " + gameId;
		}
		
		Deck newDeck = new Deck();
		
		List<Card> newStack = newDeck.getCardsStack();
		newStack.addAll( gameDeck.getCardsStack() );
		
		gameDeck.setCardsStack( newStack );
		game.setDeck(gameDeck);
		
		gameRepository.save(game);
		
		return "A deck was added to game " + gameId + " the deck has now " + newStack.size() + " cards.";
	}

	@Override
	public String dealCard(Long gameId, Integer numberOfCard) {
		
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return "Game doesn't exist";
		
		Game game = optionalGame.get();
		Deck deck = game.getDeck();
			
		List<Card> cardsStack = deck.getCardsStack();
		List<Player> playerList = game.getPlayersList();
		
		for( int i = 0; i < numberOfCard; i++ )
		{
			for( Player player: playerList )
			{
				List<Card> hand = player.getHand();
				
				if( !cardsStack.isEmpty() )
				{
					hand.add( cardsStack.get(0) );
					cardsStack.remove(0);
				}

				player.setHand(hand);
			}
		}
		
		deck.setCardsStack( cardsStack );
		game.setDeck(deck);
		game.setPlayersList( playerList );
		
		Game gameState = gameRepository.save( game );
		
		return numberOfCard + " cards where dealt to each player in " + gameState;
	}

	@Override
	public String getPlayersListWithScore(Long gameId) {
		
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return "Game doesn't exist";
		
		Game game = optionalGame.get();
		List<Player> playerList = game.getPlayersList();
		
		Collections.sort( playerList );
		Collections.reverse( playerList );
		
		String response = " ";
		
		for ( Player player : playerList )
		{
			List<Card> hand = player.getHand();
			int score = 0;
			for(Card card: hand)
			{
				score += card.getValue().ordinal();
			}
			
			response += player.getUsername() + " score is " + score + " ";
		}
				
		return response;
	}

	@Override
	public String getCardsListForPlayer(Long gameId, Long playerId) {
		
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return "Game doesn't exist";
		
		Game game = optionalGame.get();
		List<Player> playerList = game.getPlayersList();
		
		Optional<Player> optionalPlayer = playerList.stream().filter(player -> player.getId() == playerId).findFirst();
		
		if( optionalPlayer.isEmpty() )
			return "Player " + playerId + " doesn't exist in game " + gameId;
		
		Player player = optionalPlayer.get();
		List<Card> hand = player.getHand();
		
		
		return hand.toString();
	}

	@Override
	public String getCardsLeftPerSuit(Long gameId) {
		
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return "Game doesn't exist";
		
		Game game = optionalGame.get();
		List<Card> cardStack = game.getDeck().getCardsStack();
		
		HashMap<String, Integer> numberOfcardPerSuit = new HashMap<String, Integer>(); 
		
		for (Card.Suit suit : Card.Suit.values()) 
		{ 
			numberOfcardPerSuit.put(suit.name(), 0);
		}
		
		for(Card card: cardStack)
		{
			Integer count = numberOfcardPerSuit.get( card.getSuit().name() );
			numberOfcardPerSuit.replace( card.getSuit().name(), (count + 1) );
		}
		
		return numberOfcardPerSuit.toString();
	}

	@Override
	public String getCardsLeftSorted(Long gameId) {
		
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return "Game doesn't exist";
		
		Game game = optionalGame.get();
		List<Card> cardStack = game.getDeck().getCardsStack();
		
		HashMap<Card, Integer> numberOfcardPerSuit = new HashMap<Card, Integer>(); 
		

		for (Card.Suit suit : Card.Suit.values()) 
		{
			for (Card.Value number : Card.Value.values())
		    {
				if(number == Card.Value.DUMMY)
					continue;
				Card card = new Card( suit, number);
				numberOfcardPerSuit.put(card, 0);
		    }
		}

		for(Card card: cardStack)
		{
			Integer count = numberOfcardPerSuit.get( card );
			numberOfcardPerSuit.replace( card, (count + 1) );
		}
		
		return numberOfcardPerSuit.toString();
	}

	@Override
	public void shuffleGameDeck(Long gameId) {
		
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return;
		
		Game game = optionalGame.get();
		Deck deck = game.getDeck();
		List<Card> cardStack = game.getDeck().getCardsStack();
		
		List<Card> tempCardList = new ArrayList<Card>();
		
		while( cardStack.size() > 0 )
		{
			Random rand = new Random();
			int index = rand.nextInt( cardStack.size() );
			
			tempCardList.add( cardStack.get(index) );
			cardStack.remove( index );
		}
		
		deck.setCardsStack( tempCardList );
		game.setDeck( deck );
		
		gameRepository.save( game );
	}
}
