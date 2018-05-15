package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Id;
import java.util.*;

import static java.util.stream.Collectors.toList;

//THIS IS TO SHOW THE DATA I CREATED AND I WANT

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository repo;
    @Autowired
    private GamePlayerRepository gp_repo;

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
        dto.put("player", playerInfo);
        return dto;
    }

    //BUT THE FOLLOWING 3 LINES DON'T WORK
//    public List<Long> getIds(List<Game> games) {
//        List <Game> allGames = repo.findAll();
//        return games.stream().map(Game::getId).collect(toList());
//    }

    // now add additional information: a date; store the data as a Map(Object in JS): "id": long; "created": date;
    // let's try the long method,  with a for loop
//    private List<Map> getGameInfo(){
////        List <GamePlayer> allGamePlayers = game_player_repo.findAll();
//        List <Game> allGames = repo.findAll();
//
//        List <Map>  infos = new ArrayList<>();
//        for(int i=0; i < allGames.size(); i++){
//                Map<String, Object> element = new LinkedHashMap<>();
//                long id = allGames.get(i).getId();
//                Date date = allGames.get(i).getDate();
//                element.put("id", id);
//                element.put("created", date);
//                List<Map> game_player = new ArrayList<>();
//                Set<GamePlayer> gamePlayers = allGames.get(i).getGamePlayers();
//                for (int k = 0 ; k < gamePlayers.size(); k++){
//
//                }
//                element.put("gamePlayers", game_player);
//                infos.add(element);
//        }
//        return infos;
//    }
//    //try a shorter version; i don't get how to make it work
//    private List<Map> getGameInfo() {
//        List <Game> allGames = repo.findAll();
//        List <Map> infos = new ArrayList<>();
//        Map<String, Map> element = new LinkedHashMap<>();
//        Map newElement = element.put("id", allGames.stream().map(g -> g.getId()));
//        allGames.stream().
//        element.put("creation", allGames.stream().map(g -> g.getDate()));
//
//    }

    //following: getShipsInfo, called in the method getGamesbyGp, returns infos on the ships
    public Map getShipsInfo (Ship s){
        Map <String, Object> single_ship = new LinkedHashMap<>();
        // first add the ship's Type to the Map
        single_ship.put("type", s.getType());
        // then add the location, which is a List
        single_ship.put("location", s.getLocations());
        return single_ship;
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
        Game current_game= current_gp.getGame();
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
        gameInfo.put("gamePlayer", gps_list.stream()

                                   .map(gp -> gpInfo(gp))

                                   .collect(toList()));

        //this method needs ships too
        List <Map> shipsInfo = new LinkedList<>();
        Set<Ship> ships_set = current_gp.getShips();
        gameInfo.put("ships", ships_set.stream()

                              .map(s -> getShipsInfo(s))

                              .collect(toList()));
        return gameInfo;

      //!! METHOD END !!!!
    }


}






