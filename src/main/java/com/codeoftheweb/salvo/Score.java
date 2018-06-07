package com.codeoftheweb.salvo;

import com.google.common.base.Splitter;
import javax.persistence.*;
import java.util.*;


@Entity
public class Score {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    //establish relationship between "Game" and "Score"
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_Id")
    private Game game;

    //establish relation between "Player" and "Score"
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_Id")
    private Player player;

    //add a finish date
    private Date finish_date = new Date();

    double score_amount = 0;

    public Score(){};
    // ADD PLAYER AND GAME!!!!!!!!!!
    public Score(Game game, Player player, double amount){
        this.player = player;
        this.game = game;
        this.score_amount = amount;
    }

    // ID getter
    public long getId() {
        return id;
    }

    //getter of "game"
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    //getter of "player
    public Player getPlayer() {
        return player;
    }
    //this is called in method "addScore()" of class "Player);
    public void setPlayer(Player player) {
        this.player = player;
    }

    //getter of date
    public Date getDate() {
        return finish_date;
    }

    public double getScore_amount() {
        return score_amount;
    }
}

