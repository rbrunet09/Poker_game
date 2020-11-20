package com.renaud.poker_game.service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpStatus;

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
	public ResponseEntity<Game> createGame() 
	{
		Game game = new Game();
		
		Game savedGame = gameRepository.save( game );
		
		return new ResponseEntity<Game>(savedGame, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<String> deleteGame(Long gameId) {
		
		gameRepository.deleteById( gameId );
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@Override
	public @ResponseBody ResponseEntity<?> getGame(Long gameId) {
		
		Optional<Game> optionalGame = gameRepository.findById(gameId);
		if(optionalGame.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The game was not found.");
		
		Game game = optionalGame.get();
		
		return new ResponseEntity<Game>(game, HttpStatus.OK);
	}

	@Override
	public @ResponseBody ResponseEntity<?> addPlayer(Long gameId, Player player) {
		
		Optional<Game> optionalGame = gameRepository.findById(gameId);
		if(optionalGame.isEmpty())
			return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "The game was not found." );
		
		Game game = optionalGame.get();
		List<Player> playerList = game.getPlayersList();
		playerList.add(player);
		game.setPlayersList(playerList);
		
		Game savedGame = gameRepository.save(game);
		
		return new ResponseEntity<Game>( savedGame, HttpStatus.OK );
	}
	
	@Override
	public @ResponseBody ResponseEntity<?> deletePlayer( Long gameId, Long playerId )
	{
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The game was not found.");
		
		Game game = optionalGame.get();
		List<Player> playerList = game.getPlayersList();
		playerList.removeIf( player -> player.getId() == playerId );
		game.setPlayersList(playerList);
		
		Game savedGame = gameRepository.save(game);
		
		return new ResponseEntity<Game>( savedGame, HttpStatus.OK );
	}
	
	@Override
	public @ResponseBody ResponseEntity<?> getPlayer(Long gameId, Long playerId) {

		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The game was not found.");
		
		Game game = optionalGame.get();
		List<Player> playerList = game.getPlayersList();
		
		Optional<Player> optionalPlayer = playerList.stream().filter( player -> player.getId() == playerId ).findFirst();
		
		if(optionalPlayer.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The player was not found.");

		
		return new ResponseEntity<Player>( optionalPlayer.get(), HttpStatus.OK );
		
	}

	@Override
	public @ResponseBody ResponseEntity<?> createDeck(Long gameId) {
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The game was not found.");
		
		Game game = optionalGame.get();
		if( game.getDeck() != null && !game.getDeck().getCardsStack().isEmpty() )
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "The game deck is not empty, you can only add a deck.");
		}
		
		Deck deck = new Deck();
		game.setDeck(deck);
		
		Game savedGame = gameRepository.save(game);
		
		return new ResponseEntity<Game>( savedGame, HttpStatus.OK );
	}

	@Override
	public @ResponseBody ResponseEntity<?> addDeck(Long gameId) {
		
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The game was not found.");
		
		Game game = optionalGame.get();
		Deck gameDeck = game.getDeck();

		Deck newDeck = new Deck();
		
		List<Card> newStack = newDeck.getCardsStack();
		newStack.addAll( gameDeck.getCardsStack() );
		
		gameDeck.setCardsStack( newStack );
		game.setDeck(gameDeck);
		
		Game savedGame = gameRepository.save(game);
		
		return new ResponseEntity<Game>( savedGame, HttpStatus.OK );
	}

	@Override
	public @ResponseBody ResponseEntity<?> dealCard(Long gameId, Integer numberOfCard) {
		
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The game was not found.");
		
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
		
		Game savedGame = gameRepository.save( game );
		
		return new ResponseEntity<Game>( savedGame, HttpStatus.OK );
	}

	@Override
	public ResponseEntity<?> getPlayersListWithScore(Long gameId) {
		
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The game was not found.");
		
		Game game = optionalGame.get();
		List<Player> playerList = game.getPlayersList();
		
		Collections.sort( playerList );
		Collections.reverse( playerList );
		
		List < AbstractMap.SimpleEntry<String, Integer> > playersScordList = new ArrayList< AbstractMap.SimpleEntry<String, Integer> >(); 
		
		for ( Player player : playerList )
		{
			List<Card> hand = player.getHand();
			int score = 0;
			for(Card card: hand)
			{
				score += card.getValue().ordinal();
			}
			
			AbstractMap.SimpleEntry<String, Integer> playerScore = new AbstractMap.SimpleEntry<String, Integer>( player.getUsername(), score );
			
			playersScordList.add(playerScore);
		}
				
		return new ResponseEntity< List < AbstractMap.SimpleEntry<String, Integer> > >( playersScordList, HttpStatus.OK );
	}

	@Override
	public @ResponseBody ResponseEntity<?> getCardsListForPlayer(Long gameId, Long playerId) {
		
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The game was not found.");
		
		Game game = optionalGame.get();
		List<Player> playerList = game.getPlayersList();
		
		Optional<Player> optionalPlayer = playerList.stream().filter(player -> player.getId() == playerId).findFirst();
		
		if( optionalPlayer.isEmpty() )
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The player was not found in the game.");
		
		Player player = optionalPlayer.get();
		List<Card> hand = player.getHand();
		
		
		return new ResponseEntity< List<Card> >( hand , HttpStatus.OK );
	}

	@Override
	public @ResponseBody ResponseEntity<?> getCardsLeftPerSuit(Long gameId) {
		
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The game was not found.");
		
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
		
		return new ResponseEntity< HashMap<String, Integer> >( numberOfcardPerSuit , HttpStatus.OK );
	}

	@Override
	public @ResponseBody ResponseEntity<?> getCardsLeftSorted(Long gameId) {
		
		Optional<Game> optionalGame = gameRepository.findById( gameId );
		if(optionalGame.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The game was not found.");
		
		Game game = optionalGame.get();
		List<Card> cardStack = game.getDeck().getCardsStack();
		
		HashMap<Card, Integer> numberOfcardLeft = new HashMap<Card, Integer>(); 
		

		for (Card.Suit suit : Card.Suit.values()) 
		{
			for (Card.Value number : Card.Value.values())
		    {
				if(number == Card.Value.DUMMY)
					continue;
				Card card = new Card( suit, number);
				numberOfcardLeft.put(card, 0);
		    }
		}

		for(Card card: cardStack)
		{
			Integer count = numberOfcardLeft.get( card );
			numberOfcardLeft.replace( card, (count + 1) );
		}
		
		return new ResponseEntity< HashMap<Card, Integer> >( numberOfcardLeft , HttpStatus.OK );
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
