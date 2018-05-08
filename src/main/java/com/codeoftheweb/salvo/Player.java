package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

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

    public Player() { }

    public Player(String userName/*, String userMail*/) {
        this.userName = userName;
//        this.userMail = userMail;
  
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
}

