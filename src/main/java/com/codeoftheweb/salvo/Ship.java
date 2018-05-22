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
    // not a class this is why a normal array wouldn't be enough
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
    //IMPORTANT!!!! adding a single string and then splitting is overly complicated; add ann array
    // of strings instead
    public void addLocation(List<String> coordinates){
//        locations.add(coordinates);
//        List<String> locations = Arrays.asList(new String[]{"John", "Mary", "Bill"});
        locations = coordinates;
        //IMPORTANT! THE FOLLOWING IS OBSOLETE( was to resize the string according to its length
        //split the string "coordinates" using a regular expression;
        //String[] tool = coordinates.split("(?<=\\G.{2})");
        // OR split it using guava Splitter class
//        if(coordinates.length()%2 == 0 ){
//            for (String substring : Splitter.fixedLength(2).split(coordinates)) {
//                locations.add(substring);
//            }
//        }
        // IF the length of the string coordinates is an odd number, i.e. is has 10 :
//        if(coordinates.length()%2 != 0) {
//            //the following returns the a substring made of the last 3 digits of the string
//            String substring_3 = coordinates.substring(coordinates.length() - 3);
//            locations.add(substring_3);
//            //the following makes a string out of the old one MINUS the last three characters
//            String new_string = coordinates.substring(0, coordinates.length()-3);
//            for (String substring : Splitter.fixedLength(2).split(new_string)) {
//                locations.add(substring);
//            }
//        }
    }


    //getter of locations
    public List<String> getLocations() {
        System.out.println(locations);
        return locations;
    }

}

