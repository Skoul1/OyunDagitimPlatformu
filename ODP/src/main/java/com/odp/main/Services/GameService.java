package com.odp.main.Services;

import com.odp.main.Models.Game;
import com.odp.main.Models.User;
import com.odp.main.Repositorys.GameRepository;
import com.odp.main.Repositorys.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GameService {

	private static final Logger logger = LoggerFactory.getLogger(GameService.class);

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private UserRepository userRepository;

	public Game getGameById(Long id) {
		return gameRepository.findById(id).orElse(null);
	}

	public boolean userOwnsGame(Long userId, Long gameId) {
		User user = userRepository.findById(userId).orElse(null);
		if (user != null) {
			return user.getLibrary().stream().anyMatch(game -> game.getId().equals(gameId));
		}
		return false;
	}

}
