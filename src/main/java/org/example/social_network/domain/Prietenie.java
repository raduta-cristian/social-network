package org.example.social_network.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Prietenie extends Entity<Tuple<Long, Long>>{
    private LocalDateTime friendsFrom;

    public Prietenie(LocalDateTime friendsFrom){
        this.friendsFrom = friendsFrom;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return getId().toString() + " - " + friendsFrom.format(formatter);
    }
}
