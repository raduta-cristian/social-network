package org.example.social_network.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    Long fromId;
    List<Long> toIdList;
    String text;
    LocalDateTime dateTime;
    Long replyTo;

    public Message(Long fromId, List<Long> toIdList, String text, LocalDateTime dateTime, Long replyTo) {
        this.fromId = fromId;
        this.toIdList = toIdList;
        this.text = text;
        this.dateTime = dateTime;
        this.replyTo = replyTo;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public List<Long> getToIdList() {
        return toIdList;
    }

    public void setToIdList(List<Long> toIdList) {
        this.toIdList = toIdList;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getReplyTo() {
        return replyTo;
    }
    public void setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
    }
}
