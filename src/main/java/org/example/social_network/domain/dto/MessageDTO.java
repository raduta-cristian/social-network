package org.example.social_network.domain.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class MessageDTO {
    private Long id;
    private String text;
    private LocalDateTime dateTime;
    private String replyingTo;
    private String sentByOther;

    public MessageDTO(Long id, String text, LocalDateTime dateTime, String replyingTo, String sentByOther) {
        this.id = id;
        this.text = text;
        this.dateTime = dateTime;
        this.replyingTo = replyingTo;
        this.sentByOther = sentByOther;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getReplyingTo() {
        return replyingTo;
    }

    public void setReplyingTo(String replyingTo) {
        this.replyingTo = replyingTo;
    }

    public String isSentByOther() {
        return sentByOther;
    }

    public void setSentByOther(String sentByOther) {
        this.sentByOther = sentByOther;
    }

    public String getSentByOther() {
        return sentByOther;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDTO that = (MessageDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(text, that.text) && Objects.equals(dateTime, that.dateTime) && Objects.equals(replyingTo, that.replyingTo) && Objects.equals(sentByOther, that.sentByOther);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, dateTime, replyingTo, sentByOther);
    }
}
