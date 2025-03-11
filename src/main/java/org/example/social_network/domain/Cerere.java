package org.example.social_network.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cerere extends Entity<Tuple<Long, Long>> {
    private LocalDateTime timeSent;
    private String status;

    public Cerere(LocalDateTime timeSent, String status) {
        this.timeSent = timeSent;
        this.status = status;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "from: " + getId().getLeft() + " - to:" + getId().getRight() + " - " + getTimeSent().format(formatter) + " - " + status;
    }
}
