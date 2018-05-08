package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.util.ArrayList;
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
    @ElementCollection
    @Column(name="locations")
    private List<String> locations = new ArrayList<>();

    //the ship type (patrol boat etc)
    public String type = new String();



    public Ship() {

    }
    //getter of Id
    public long getId() {
        return id;
    }

    //getter of GamePlayer
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    //getter of type
    public String getType() {
        return type;
    }

    //setter of type
    public void setType(String type) {
        this.type = type;
    }

    //getter of locations
    private List<String> getLocations() {
        return locations;
    }

}

