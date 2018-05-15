package com.codeoftheweb.salvo;

import com.google.common.base.Splitter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    //this assigns many locations to a single ship; one-to-many, but "to many" are pieces of data,
    // not a class
    @ElementCollection // i make this to store this data on the database cell!
    @Column(name="locations")
    private List<String> locations = new ArrayList<>();

    //the ship type (patrol boat etc)
    public String type = new String();


    public Ship() {}; //Empty constructor for JACKSON

    public Ship(String type) {
        this.type = type;
    }
    //getter of Id
    public long getId() {
        return id;
    }

    // setter of GamePlayer; we MUST set the GamePlayer for the ship; I do it in the addShip() method
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    //getter of GamePlayer
    public GamePlayer getGamePlayer() {
        System.out.println(this.gamePlayer);
        return this.gamePlayer;
    }

    //getter of type
    public String getType() {
        System.out.println(this.type);
        return this.type;
    }

    //setter of type
    public void setType(String type) {
        this.type = type;
    }

    //method to add the location; see:
    // https://stackoverflow.com/questions/3760152/split-string-to-equal-length-substrings-in-java
    public void addLocation(String coordinates){
        //split the string "coordinates" using a regular expression;
        //String[] tool = coordinates.split("(?<=\\G.{2})");

        // OR split it using guava Splitter class
        for (String substring : Splitter.fixedLength(2).split(coordinates)) {
            locations.add(substring);
        }

    }

    //getter of locations
    public List<String> getLocations() {
        System.out.println(locations);
        return locations;
    }

}

