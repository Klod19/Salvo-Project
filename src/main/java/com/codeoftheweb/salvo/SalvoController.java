package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

//THIS IS TO SHOW THE DATA I CREATED AND I WANT

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository repo;
    @Autowired
    private GamePlayerRepository gp_repo;
    @Autowired
    private PlayerRepository p_repo;

    @RequestMapping("/games")
    // make a method to show current player + games in a JSON object
    public Map <String, Object> build (Authentication authentication){
        Map<String, Object> currentP_and_Games = new LinkedHashMap<>();
        currentP_and_Games.put("games", getGames());
        //the following adds player information ONLY when that player is logged in
        if (! (authentication == null || authentication instanceof AnonymousAuthenticationToken)){
            currentP_and_Games.put("player", cp_infos(authentication));

        }
        return currentP_and_Games;
    }

    // current player, based on instances of Authentication class (from Spring)
    public Player currentPlayer (Authentication authentication) {
        Player current_p = new Player();
        if (! (authentication == null || authentication instanceof AnonymousAuthenticationToken)){
            current_p = p_repo.findByUserName(authentication.getName());
        }
        return current_p;
    }

    // the following is a Map containing current player ID and current player userName
    //!! the Class "Authentication" is automatically given by Spring!
    public Map <String, Object> cp_infos (Authentication authentication) {
        Map<String, Object> cpInfos = new LinkedHashMap<>();
        cpInfos.put("id", currentPlayer(authentication).getId() );
        cpInfos.put("userName", currentPlayer(authentication).getUserName());
        cpInfos.put("aut.getName()", authentication.getName()); //it gets the username! is it the authorization name?
        return cpInfos;
    }

    public List<Object> getGames() {
        List <Game> allGames = repo.findAll();
           List gamesList  =      allGames.stream()

                .map(g -> makeGames(g))

                .collect(toList());
           return gamesList;
    }

    public Map<String, Object> makeGames(Game game){

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getDate());
        dto.put("gamePlayers", game.getGamePlayers().stream()

                                                    .map(gp -> gpInfo(gp))

                                                    .collect(toList())
        );

        return dto;
    }

    public Map<String, Object> gpInfo(GamePlayer gp){
        //following: the key and values "id"-GamePlayer id AND "player" - playerInfo
        Map<String, Object> dto = new LinkedHashMap<>();
        //following: playerInfo, i.e. the value of key "player"
        Map<String, Object> playerInfo = new LinkedHashMap<>();
        //first let's make "playerInfo", with fields from class "Player"
        playerInfo.put("id", gp.getPlayer().getId());
        playerInfo.put("userName", gp.getPlayer().getUserName());
        playerInfo.put("gpId", gp.getId());
        //then let's make "dto", i.e. the fields of "GamePlayer"
        dto.put("id", gp.getId());
        if (gp.getScore() != null) {
            dto.put("score", gp.getScore().getScore_amount());
        }
        dto.put("player", playerInfo);
        return dto;
    }

    //following: getShipsInfo, called in the method getGamebyGp, returns infos on the ships
    public Map getShipsInfo (Ship s){
        Map <String, Object> single_ship = new LinkedHashMap<>();
        // first add the ship's Type to the Map
        single_ship.put("type", s.getType());
        // then add the location, which is a List
        single_ship.put("location", s.getLocations());
        return single_ship;
    }

    //following: getSalvoInfo, called in method getGameByPg, returns inofs on salvos
    public Map getSalvoInfo (Salvo sl){
        Map <String, Object> single_salvo = new LinkedHashMap<>();
        // first add the salvo's gamePlayer
        single_salvo.put("gamePlayer_id", sl.getGamePlayer().getId());
        //add the salvo's turn
        single_salvo.put("turn", sl.getTurn());
        //add the salvo's player's id
        single_salvo.put("player_id", sl.getGamePlayer().getPlayer().getId());
        //add the salvo's player's username
        single_salvo.put("player_userName", sl.getGamePlayer().getPlayer().getUserName());
        // then add the location, which is a List
        single_salvo.put("locations", sl.getLocations());
        return single_salvo;
    }

//    @RequestMapping("/test")
//    public List<Object> test(){
//        List<GamePlayer> allGamePlayers = gp_repo.findAll();
//        return allGamePlayers.stream().map(gp -> gp.getScore().getScore_amount())
//                .collect(toList());
//    }

    //method to create a new game (post)
    @RequestMapping(path ="/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createNewGame (Authentication authentication) {
        if ( authentication == null){
            return new ResponseEntity<>(makeMap("error", "NO PLAYER LOGGED IN!"), HttpStatus.UNAUTHORIZED);
        }
        else{
            //save new game and new gameplayer in the matching repositories; return a response
            Game newGame = new Game();
            repo.save(newGame);
            GamePlayer newGamePlayer = new GamePlayer(currentPlayer(authentication), newGame);
            gp_repo.save(newGamePlayer);
            return new ResponseEntity<>(makeMap("gpId", newGamePlayer.getId()), HttpStatus.CREATED);
        }
    }

    @RequestMapping("/scores_list")
    public Map<String, Object> getPoints(){
        Map<String, Object> allPlayers = new HashMap<>();

        List<GamePlayer> allGamePlayers = gp_repo.findAll();
        for (int i = 0; i < allGamePlayers.size(); i++ ) {
            String userName = allGamePlayers.get(i).getPlayer().getUserName();
            if (! allPlayers.containsKey(userName)) {

                Map<String, Object> playerScore = new HashMap<>();

                //the following 3 lines make a count of the respective values
                playerScore.put("wins", allGamePlayers.get(i).getPlayer().getScores().stream().filter(score -> score.getScore_amount() == 1).count());
                playerScore.put("losses", allGamePlayers.get(i).getPlayer().getScores().stream().filter(score -> score.getScore_amount() == 0).count());
                playerScore.put("ties", allGamePlayers.get(i).getPlayer().getScores().stream().filter(score -> score.getScore_amount() == 0.5).count());
                //make the "count" into a string; parse this string to a double, so "total" stays double (it stays double also with int, but whatever))
                double total_wins = Double.parseDouble(playerScore.get("wins").toString());
                double total_losses = Double.parseDouble(playerScore.get("losses").toString());
                double total_ties = Double.parseDouble(playerScore.get("ties").toString());
                double total = (total_wins* 1) + (total_ties * 0.5);
                playerScore.put("total", total);
                allPlayers.put(userName, playerScore);

            }
            // WHY THIS DOESN'T WORK? IS THERE A WAY TO CREATE A FUNCTION OUT OF THIS??
//            public long countScores (List list_gp, int i){
//                list_gp.get(i).getPlayer().getScores().stream().filter(score -> score.getScore_amount() == 1).count();
//            }
        }

        return allPlayers;
    }

    @RequestMapping(path = "/game_view/{gamePlayerId}",  method = RequestMethod.GET)
    //@PathVariable UPDATES the gamePlayerId with the value given by the URL; then I use
    //the gamePlayerId in the method
    public ResponseEntity <Map<String,Object >> getGameByGp(@PathVariable Long gamePlayerId, Authentication authentication) {
        // we need to check if the authenticated user may access the page:
        //so check if current player userName == to the current gamePlayer, got through its id
        GamePlayer current_gp = gp_repo.findOne(gamePlayerId);
        if (!(current_gp.getPlayer().getUserName()).equals(currentPlayer(authentication).getUserName())){
            return new ResponseEntity<>(makeMap("error", "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
        }
        else{
            //we'll return the following:
            Map<String, Object> gameInfo = new LinkedHashMap<>();
            // Find one gamePlayer by id with .findOne(id), and then work with it
            Game current_game = current_gp.getGame();
            gameInfo.put("id", current_game.getId());
            gameInfo.put("created", current_game.getId());
            //I want the current gamePlayer id and the current player userName
            gameInfo.put("current gamePlayer Id", current_gp.getId());
            gameInfo.put("current Player userName", current_gp.getPlayer().getUserName());

            //now get the 2 gamePlayers from the current game, then add their data to gameInfo
            //I get the gamePlayers of a Game in a Set collection
            Set<GamePlayer> gps = current_game.getGamePlayers();
            //I need to turn this Set into a List
            List <GamePlayer> gps_list = new ArrayList<>(gps);
            //now I have to add the gamePlayers, filtering the data I need (their Id and the Map "player")
            gameInfo.put("gamePlayers", gps_list.stream()

                    .map(gp -> gpInfo(gp))

                    .collect(toList()));

            //this method needs ships too
            List <Map> shipsInfo = new LinkedList<>(); //this one is probably useless
            Set<Ship> ships_set = current_gp.getShips();
            gameInfo.put("ships", ships_set.stream()

                    .map(s -> getShipsInfo(s))

                    .collect(toList()));

            //we need to add salvoes too!
            Set <GamePlayer> two_current_gp = current_game.getGamePlayers();

            two_current_gp.stream().map(cgp -> cgp.getSalvoes());
            Map <String, Object> one_salvo = new LinkedHashMap<>();

            //alternative 1, doesn't work
            Set<Set<Salvo>> salvoes_set = two_current_gp.stream().map(cgp -> cgp.getSalvoes())
                    .collect(Collectors.toSet());


            gameInfo.put("salvoes",  salvoes_set.stream()

                    .map(sl -> sl.stream()
                            .map(salvo1 -> getSalvoInfo(salvo1))
                            .collect(toList()))

                    .collect(toList()));

            return new ResponseEntity<>(makeMap("games",gameInfo), HttpStatus.ACCEPTED);
        }

      //!! METHOD END !!!!
    }

    // METHOD TO CREATE A NEW USER!!!!
    @RequestMapping(path = "/players", method = RequestMethod.POST)
    //@RequestParam annotation used for accessing the query parameter values from the request
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String userName, String password) {
        //if no name is given:
        if (userName.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No name given!"), HttpStatus.FORBIDDEN);
        }
        //if the given name is already in use:
        Player player = p_repo.findByUserName(userName);
        if (player != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.CONFLICT);
        }
        //if everything is ok, save the new player into the repository
        Player newPlayer = p_repo.save(new Player(userName, password));
        return new ResponseEntity<>(makeMap(userName, newPlayer.getId()) , HttpStatus.CREATED);
    }

    //METHOD TO JOIN A GAME
    @RequestMapping(path = "/game/{gameId}/players",  method = RequestMethod.POST)
    // I request a path variable to obtain the placeholder from the URL; it binds the URL request template
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gameId, Authentication authentication) {
        //GET CURRENT USER
        Player current_player = currentPlayer(authentication);
        //NO CURRENT USER?
        if (current_player == null) {
            return new ResponseEntity<>(makeMap("ERROR", "NO LOGGED-IN USER"), HttpStatus.UNAUTHORIZED);
        }
        //GET THE GAME WITH THE ID; look for it in the game repository, using repository.findOne(Long id);
        Game current_game = repo.findOne(gameId);
        //NO SUCH A GAME?
        if(current_game == null){
            return new ResponseEntity<>(makeMap("ERROR", "THERE IS NO SUCH A GAME"), HttpStatus.FORBIDDEN );
        }
        //CHECK THAT THIS GAME HAS ONLY 1 PLAYER
        if (current_game.getGamePlayers().stream().count() == 2){
            return new ResponseEntity<>(makeMap("ERROR","GAME ALREADY FULL"), HttpStatus.FORBIDDEN);
        }
        //EVERYTHING OK? MAKE A NEW GAMEPLAYER, WITH CURRENT PLAYER + CURRENT GAME, AND SAVE IT
        GamePlayer newGamePlayer = new GamePlayer(current_player, current_game);
        gp_repo.save(newGamePlayer);
        return new ResponseEntity<>(makeMap("gpId", newGamePlayer.getId()), HttpStatus.CREATED);
    }


    // a method to visualize a return Map
    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

}


