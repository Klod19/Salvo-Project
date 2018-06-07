package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
//    public List <Game> getAll() { THIS RETURNS ALL THE GAMES
//        List<Game> allGames =  repo.findAll();
//         return  allGames;
//    }


//     try to get IDS with a shorter function; the following works
    public List<Object> getIds() {
        List <Game> allGames = repo.findAll();
        return allGames.stream()

                .map(g -> makeGames(g))

                .collect(toList());
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

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> getGameByGp(@PathVariable Long gamePlayerId) {
        //@PathVariable UPDATES the gamePlayerId with the value given by the URL; then i use
        //the gamePlayerId in the method

        //we'll return the following:
        Map<String, Object> gameInfo = new LinkedHashMap<>();

        // 1) either find all the gamePlayers, and then loop through them
//        List <GamePlayer> allGamePlayers = gp_repo.findAll();
//        for (int i =0; i < allGamePlayers.size(); i++){
//            if(allGamePlayers.get(i).getId() == gamePlayerId) {
//                Game current_game = allGamePlayers.get(i).getGame();
//                Set<GamePlayer> gps = current_game.getGamePlayers();
//                List<GamePlayer> gps_list = new ArrayList<>(gps);
//                gameInfo.put("id", current_game.getId());
//                gameInfo.put("created", current_game.getDate());
//                gameInfo.put("gamePlayer", gps_list.stream()
//
//                        .map(gp -> gpInfo(gp))
//
//                        .collect(toList()));
//            }
//        }

        // 2) or find one gamePlayer by id with .findOne(id), and then work with it
        GamePlayer current_gp = gp_repo.findOne(gamePlayerId);
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

        return gameInfo;

      //!! METHOD END !!!!
    }


}


//the following works, but returns a JSON with the salvo only for the current game player
//    List<Map> salvoesInfo = new LinkedList<>(); //this one is probably useless
//    Map <String, Object> one_salvo = new LinkedHashMap<>();
//    Set<Salvo> salvoes_set = current_gp.getSalvoes();
//        gameInfo.put("salvoes", salvoes_set.stream()
//
//                .map(sl -> getSalvoInfo(sl))
//
//                .collect(toList()));