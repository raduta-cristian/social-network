package org.example.social_network.domain.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class CerereDTO {
    Long fromId;
    String fromFirstName;
    String fromLastName;
    Long toId;
    String toFirstName;
    String toLastName;
    LocalDateTime timeSent;
    String status;

    public CerereDTO(Long fromId, String fromFirstName, String fromLastName, Long toId, String toFirstName, String toLastName, LocalDateTime timeSent, String status) {
        this.fromId = fromId;
        this.fromFirstName = fromFirstName;
        this.fromLastName = fromLastName;
        this.toId = toId;
        this.toFirstName = toFirstName;
        this.toLastName = toLastName;
        this.timeSent = timeSent;
        this.status = status;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public String getFromFirstName() {
        return fromFirstName;
    }

    public void setFromFirstName(String fromFirstName) {
        this.fromFirstName = fromFirstName;
    }

    public String getFromLastName() {
        return fromLastName;
    }

    public void setFromLastName(String fromLastName) {
        this.fromLastName = fromLastName;
    }

    public String getToFirstName() {
        return toFirstName;
    }

    public void setToFirstName(String toFirstName) {
        this.toFirstName = toFirstName;
    }

    public String getToLastName() {
        return toLastName;
    }

    public void setToLastName(String toLastName) {
        this.toLastName = toLastName;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CerereDTO cerereDTO = (CerereDTO) o;
        return Objects.equals(fromId, cerereDTO.fromId) && Objects.equals(toId, cerereDTO.toId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId);
    }
}
