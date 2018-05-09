package com.codeoftheweb.salvo;


import javax.persistence.*;
import java.util.Set;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

//    private LocalDate date = LocalDate.now(); this returns the date without hour
    private Date date = new Date(); //this returns date+hours BUT in JSON has on hour less (Greenwich Time)

    public Game() { } //EMPTY CONSTRUCTOR; FOR JACKSON AND TO CREATE A NEW GAME
    //getter of Id
    public long getId() {
        return id;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    // getter of Date
    public Date getDate() {
        return date;
    }
////    //setter for the class "Date" which has a JSON with "one hour earlier"
    public void setDate(int plus_hours) {
//////        date.parse("yyyy-MM-ddT03:hh:mm", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        plus_hours = plus_hours * 3600;
        this.date = Date.from(date.toInstant().plusSeconds(plus_hours));
    }
    // setter for LocalDateTime
//    public void setDate(long hours){
//        this.date = date.plusHours(hours);
//    }
}
