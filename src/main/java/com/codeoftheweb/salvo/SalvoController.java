package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static java.util.stream.Collectors.toList;
//THIS IS TO SHOW THE DATA I CREATED AND I WANT

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository repo;

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
        //the let's make "dto", i.e. the fields of "GamePlayer"
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
}
