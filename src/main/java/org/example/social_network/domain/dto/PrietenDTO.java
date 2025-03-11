package org.example.social_network.domain.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class PrietenDTO {
    Long id1;
    Long id2;
    String firstName;
    String lastName;
    LocalDateTime friendsFrom;

    public PrietenDTO(Long id1, Long id2, String firstName, String lastName, LocalDateTime friendsFrom) {
        this.id1 = id1;
        this.id2 = id2;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friendsFrom = friendsFrom;
    }

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrietenDTO that = (PrietenDTO) o;
        return Objects.equals(id1, that.id1) && Objects.equals(id2, that.id2) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(friendsFrom, that.friendsFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2, firstName, lastName, friendsFrom);
    }
}
