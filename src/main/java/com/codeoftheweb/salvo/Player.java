package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    //establish relationship "Player"-"GamePlayer"
    @JsonIgnore
    @OneToMany(mappedBy="player", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new LinkedHashSet<>();

    //establish relationship "Player"-Score"
    @JsonIgnore
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER )
    Set<Score> scores = new LinkedHashSet<>();
// RESOURCES EXAMPLE
//    public void addSubscription(Subscription subscription) {
//        subscription.setSubscriber(this);
//        subscriptions.add(subscription);
//    }

//    public void addGame(Game game){
//        gamePlayers.setPlayer(this);
//        gamePlayers.add(game);
//    }
//
//    //should I add GamePlayer????
//    public void addGamePlayer(GamePlayer gamePlayer) {
//        gamePlayer.setgamePlayer(this);
//        players.add(gamePlayer);
//    }


    private String userName;
//    private String userMail;

    private String password;

    public Player() { } //EMPTY CONSTRUCTOR FOR JACKSON

    public Player(String userName/*, String userMail*/) {
        this.userName = userName;
//        this.userMail = userMail;
  
    }

    //password getter and setter


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ID getter
    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public String getUserMail() {
//        return userMail;
//    }
//
//    public void setUserMail(String userName) {
//        this.userMail = userMail;
//    }

    public String toString() {
        return userName;
    }

    //getter of gamePlayers
    @JsonIgnore
    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    //method to add a score: I don't actually use it, because when I make a "Score" object I already set a Player, from
    //its parameter
    public void addScore(Score s) {
        scores.add(s);
        s.setPlayer(this);
//        s.getPlayer().getGamePlayers();
    }

    public Set<Score> getScores() {
        return scores;
    }

    public Game getGame(){
        return this.getGame();
    }

    //basically I get a Score from: loop through scores, filter those scores whose "game" matches
    //the game in the parameter; i get the first satisfying this condition and, if I get none,
    // I return "null"; I'll use this method in method getScore OF Class "GamePlayer"
    @JsonIgnore
    public Score getScore(Game game){
            return scores
                            .stream()

                            .filter(s -> s.getGame().equals(game))

                            .findFirst()

                            .orElse(null);

    }
}

