package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.util.Date;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository,
									  GamePlayerRepository gamePlayerRepository, ShipRepository
									  shipRepository) {
		return (args) -> {
			//make some players
			Player player1 = new Player("j.bauer@ctu.gov ");
			Player player2 = new Player("c.obrian@ctu.gov");
			Player player3 = new Player("kim_bauer@gmail.com");
			Player player4 = new Player("t.almeida@ctu.gov");
			Player player5 = new Player("lol.wut@gmail.com");
            //save some players
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			playerRepository.save(player5);
			// make some games
			Game game1 = new Game();
			Game game2 = new Game();
			Game game3 = new Game();
			Game game4 = new Game();
			//change the creation date of the games
			game2.setDate(1);
			game3.setDate(2);
			game4.setDate(3);
			//control if the change worked
			System.out.println(game1.getDate());
			System.out.println(game2.getDate());
			System.out.println(game3.getDate());
			//save the games
			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
//			GamePlayer gamePlayer1 = new GamePlayer(player1, game1);
//			System.out.println(gamePlayer1);
			GamePlayer gamePlayer1 = new GamePlayer(player1, game1);
			GamePlayer gamePlayer2 = new GamePlayer(player2, game1);
			GamePlayer gamePlayer3 = new GamePlayer(player3, game2);
			GamePlayer gamePlayer4 = new GamePlayer(player4, game2);
			GamePlayer gamePlayer5 = new GamePlayer(player1, game3);
			GamePlayer gamePlayer6 = new GamePlayer(player5, game3);
			GamePlayer gamePlayer7 = new GamePlayer(player5, game4);

			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			gamePlayerRepository.save(gamePlayer5);
			gamePlayerRepository.save(gamePlayer6);
			gamePlayerRepository.save(gamePlayer7);

//			//make some ships
			Ship ship1 = new Ship();
			Ship ship2 = new Ship();
//
//			//save the ships
			shipRepository.save(ship1);
			shipRepository.save(ship2);

		};
	}

}


