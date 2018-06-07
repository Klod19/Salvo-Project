package com.codeoftheweb.salvo;

import com.google.common.base.Splitter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    //this assigns a location to a single salvo; locations are pieces of data,
    // not a class; this is why a normal array wouldn't be enough
    // Each salvo can have one location i.e. an array made of one element; why not a string?
    @ElementCollection // I make this to store this data on the database cell!
    @Column(name="locations")
    private List<String> location = new ArrayList<>();
    // the turn :
    public int turn = 0;

    public Salvo() {}; //Empty constructor for JACKSON

    // I'd create a new instance of "Salvo" with an int parameter "turn"
    public Salvo (int new_turn){
        this.turn = new_turn;
    }
    //the getter of "turn"
    public int getTurn() {
        return turn;
    }

    //getter of Id
    public long getId() {
        return id;
    }

    // setter of GamePlayer; we MUST set the GamePlayer for the salvo; I do it in the addSalvo() method
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    //getter of GamePlayer
    public GamePlayer getGamePlayer() {
        System.out.println(this.gamePlayer);
        return this.gamePlayer;
    }

    //method to add the location;
    public void addLocation(List<String> coordinates){
        location = coordinates;

    }

    //getter of locations
    public List<String> getLocations() {
        System.out.println(location);
        return location;
    }

}

