package com.codeoftheweb.salvo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)//generates id number auto, increasing
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")//arbitrary; name of column where player is saved
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    private Date date = new Date();

    public GamePlayer() { }

    public GamePlayer(Player player, Game game) {
        this.player = player;
        this.game = game;

    }
    //getter for ID
    public long getId() {
        return id;
    }

    //getter for Player
    public Player getPlayer() {
        return this.player;
    }
    //setter for Player
    public void setPlayer(Player player) {
        this.player = player;
    }

    //getter for Game
    public Game getGame() {
        return game;
    }
    //setter for Game
    public void setGame(Game game) {
        this.game = game;
    }

    //getter for Date
    public Date getDate() {
        return date;
    }
    //setter for Date
    public void setDate(int plus_hours) {
        plus_hours = plus_hours*3600;
        this.date = Date.from(date.toInstant().plusSeconds(plus_hours));
    }

}