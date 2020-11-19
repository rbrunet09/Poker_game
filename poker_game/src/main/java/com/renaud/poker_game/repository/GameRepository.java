package com.renaud.poker_game.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renaud.poker_game.model.Game;

public interface GameRepository extends JpaRepository <Game, Long>
{
	
}
