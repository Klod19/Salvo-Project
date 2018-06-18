package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository,
									  GamePlayerRepository gamePlayerRepository, ShipRepository
									  shipRepository, SalvoRepository salvoRepository,
									  ScoreRepository scoreRepository) {
		return (args) -> {
			//make some players
			Player player1 = new Player("j.bauer@ctu.gov", "24");
			Player player2 = new Player("c.obrian@ctu.gov", "27");
			Player player3 = new Player("kim_bauer@gmail.com", "kb");
			Player player4 = new Player("t.almeida@ctu.gov", "mole");
			Player player5 = new Player("lol.wut@gmail.com", "troll");

			// make some games
			Game game1 = new Game();
			Game game2 = new Game();
			Game game3 = new Game();
			Game game4 = new Game();
			Game game5 = new Game();
			Game game6 = new Game();
			//change the creation date of the games
			game1.getDate();
			//the following line returns the same date as the one above
			game2.setDate(1);
			game3.setDate(2);
			game4.setDate(3);
			//control if the change worked
			System.out.println(game1.getDate());
			System.out.println(game2.getDate());
			System.out.println(game3.getDate());

//			GamePlayer gamePlayer1 = new GamePlayer(player1, game1);
//			System.out.println(gamePlayer1);
			GamePlayer gamePlayer1 = new GamePlayer(player1, game1);
			GamePlayer gamePlayer2 = new GamePlayer(player2, game1);
			GamePlayer gamePlayer3 = new GamePlayer(player3, game2);
			GamePlayer gamePlayer4 = new GamePlayer(player4, game2);
			GamePlayer gamePlayer5 = new GamePlayer(player1, game3);
			GamePlayer gamePlayer6 = new GamePlayer(player5, game3);
			GamePlayer gamePlayer7 = new GamePlayer(player5, game4);
			GamePlayer gamePlayer8 = new GamePlayer(player2, game4);
			GamePlayer gamePlayer9 = new GamePlayer(player1, game5);
			GamePlayer gamePlayer10 = new GamePlayer(player2, game5);
			GamePlayer gamePlayer11 = new GamePlayer(player1, game6);
			GamePlayer gamePlayer12 = new GamePlayer(player5, game6);



//			//make some ships
			Ship ship1 = new Ship("Carrier");
			Ship ship2 = new Ship("Battle Ship");
			Ship ship3 = new Ship("Submarine");
			Ship ship4 = new Ship("Destroyer");
			Ship ship5 = new Ship("Patrol Boat");
			Ship ship6 = new Ship("Carrier");
			Ship ship7 = new Ship("Battle Ship");
			Ship ship8 = new Ship("Submarine");
			Ship ship9 = new Ship("Destroyer");
			Ship ship10 = new Ship("Patrol Boat");

			//add ships to gamePlayer1:
			gamePlayer1.addShip(ship1);
			gamePlayer1.addShip(ship2);
			gamePlayer1.addShip(ship3);
			gamePlayer1.addShip(ship4);
			gamePlayer1.addShip(ship5);
			gamePlayer1.getShips();
			//add ships to gamePlayer2
			gamePlayer2.addShip(ship6);
			gamePlayer2.addShip(ship7);
			gamePlayer2.addShip(ship8);
			gamePlayer2.addShip(ship9);
			gamePlayer2.addShip(ship10);
			gamePlayer2.getShips();
			//see if ships got their gamePlayer
			ship1.getGamePlayer();
			ship2.getGamePlayer();
			//control the type of the ships
			ship1.getType();
			ship2.getType();
			ship3.getType();
			ship4.getType();
			ship5.getType();
			//add and control the locations of the ships for gamePlayer1
			//THE FOLLOWING MAKES A LIST OUT OF THIS, IN ONE LINE
			ship1.addLocation( Arrays.asList("D10", "E10", "F10", "G10", "H10"));
			ship1.getLocations();
			ship2.addLocation(Arrays.asList("J7", "I7", "H7", "G7"));
			ship2.getLocations();
			ship3.addLocation(Arrays.asList("A8", "A9", "A10"));
			ship3.getLocations();
			ship4.addLocation(Arrays.asList("E5", "E6", "E7"));
			ship4.getLocations();
			ship5.addLocation(Arrays.asList("H3", "H4"));;
			ship5.getLocations();

			//add and control the locations of the ships for gameplayer2
			ship6.addLocation( Arrays.asList("D8", "E8", "F8", "G8", "H8"));
			ship6.getLocations();
			ship7.addLocation(Arrays.asList("J1", "I1", "H1", "G1"));
			ship7.getLocations();
			ship8.addLocation(Arrays.asList("C4", "C5", "C6"));
			ship8.getLocations();
			ship9.addLocation(Arrays.asList("A5", "A6", "A7"));
			ship9.getLocations();
			ship10.addLocation(Arrays.asList("J10", "J9"));;
			ship10.getLocations();


			//make some salvoes
			Salvo salvo1 = new Salvo(1);
			Salvo salvo2 = new Salvo(1);
			Salvo salvo3 = new Salvo(2);
			Salvo salvo4 = new Salvo(2);
			Salvo salvo5 = new Salvo(3);
			Salvo salvo6 = new Salvo(3);
			Salvo salvo7 = new Salvo(4);
			Salvo salvo8 = new Salvo(4);

			//give each salvo a location
			salvo1.addLocation(Arrays.asList("H1", "B4"));
			salvo2.addLocation(Arrays.asList("F4", "H9"));
			salvo3.addLocation(Arrays.asList("B10","C1"));
			salvo4.addLocation(Arrays.asList("E9", "J8"));
			salvo5.addLocation(Arrays.asList("J9", "F6"));
			salvo6.addLocation(Arrays.asList("D5", "A7"));
			salvo7.addLocation(Arrays.asList("I6", "G1"));
			salvo8.addLocation(Arrays.asList("A4", "E10"));

			// add some salvoes to the gameplayers
			gamePlayer1.addSalvo(salvo1);
			gamePlayer2.addSalvo(salvo2);
			gamePlayer1.addSalvo(salvo3);
			gamePlayer2.addSalvo(salvo4);
			gamePlayer1.addSalvo(salvo5);
			gamePlayer2.addSalvo(salvo6);
			gamePlayer1.addSalvo(salvo7);
			gamePlayer2.addSalvo(salvo8);



			//make some scores
			Score score1 = new Score(game1, player1, 1.0 );
			Score score2 = new Score(game1, player2, 0.0);
			Score score3 = new Score(game2, player3, 0.5);
			Score score4 = new Score(game2, player4, 0.5);
			Score score5 = new Score(game3, player1, 0.0);
			Score score6 = new Score(game3, player5, 1.0);
			Score score7 = new Score(game4, player5, 1.0);
			Score score8 = new Score(game4, player2, 0.0);
			Score score9 = new Score(game5, player1, 1.0);
			Score score10 = new Score(game5, player2, 0.0);
			Score score11 = new Score(game6, player1, 0.0);
			Score score12 = new Score(game6, player5, 1.0);


			//save some players
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			playerRepository.save(player5);

			//save the games
			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
			gameRepository.save(game5);
			gameRepository.save(game6);

			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			gamePlayerRepository.save(gamePlayer5);
			gamePlayerRepository.save(gamePlayer6);
			gamePlayerRepository.save(gamePlayer7);
			gamePlayerRepository.save(gamePlayer8);
			gamePlayerRepository.save(gamePlayer9);
			gamePlayerRepository.save(gamePlayer10);
			gamePlayerRepository.save(gamePlayer11);
			gamePlayerRepository.save(gamePlayer12);

			//save the ships
			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);
			shipRepository.save(ship6);
			shipRepository.save(ship7);
			shipRepository.save(ship8);
			shipRepository.save(ship9);
			shipRepository.save(ship10);

			//save the salvoes in the repository
			salvoRepository.save(salvo1);
			salvoRepository.save(salvo2);
			salvoRepository.save(salvo3);
			salvoRepository.save(salvo4);
			salvoRepository.save(salvo5);
			salvoRepository.save(salvo6);
			salvoRepository.save(salvo7);
			salvoRepository.save(salvo8);

			//save scores in repository
			scoreRepository.save(score1);
			scoreRepository.save(score2);
			scoreRepository.save(score3);
			scoreRepository.save(score4);
			scoreRepository.save(score5);
			scoreRepository.save(score6);
			scoreRepository.save(score7);
			scoreRepository.save(score8);
			scoreRepository.save(score9);
			scoreRepository.save(score10);
			scoreRepository.save(score11);
			scoreRepository.save(score12);


		};
	}

}

@Configuration //tells Spring to create an instance of this class automatically
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				// is "User" a Spring Boot class?
				//User class could also have multiple roles, setted doing this:
				//create a comma-separated list of roles, e.g., "INSTRUCTOR,STUDENT"
				// and pass it to AuthorityUtils.commaSeparatedStringToAuthorityList(...).
				//NOTICE: I didn't check if the user entered the correct password; this is
				//handled by the User Class internally
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}

}

@Configuration //tells Spring to create an instance of this class automatically
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter { //the first inherit from the second
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
//				.antMatchers("/**").permitAll()
				.antMatchers("/web/games.html").permitAll()
            	.antMatchers("/web/games.js").permitAll()
            	.antMatchers("/web/game_style.css").permitAll()
				.antMatchers("/gamesScreen.css").permitAll()
				.antMatchers("/api/games").permitAll()
				.antMatchers("/api/players").permitAll()
				.antMatchers("/img/*").permitAll()
				.antMatchers("/web/jQuery/jquery-3.3.1.min.js").permitAll()
				.antMatchers("/jquerycookie/*").permitAll()
				.antMatchers("/web/login_popup.html").permitAll()
				.antMatchers("/loginForm.css").permitAll()
				.antMatchers("/admin/**").hasAuthority("ADMIN")
				.antMatchers("/**").hasAuthority("USER")

				.and()
				.formLogin()
				.usernameParameter("userName")
				.passwordParameter("password")
				.loginPage("/api/login");

				http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens: these are additional keys that a server tells a browser to send with
		// authenticated users to prevent a form of a attack called Cross-Site Forgery Request. CSRF tokens are disabled because
		// supporting them requires a bit of work, and this kind of attack is more typical with regular web page browsing.
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}
	//following: a utility function,is defined to remove the flag Spring sets when an unauthenticated user
	// attempts to access some resource.
	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}

