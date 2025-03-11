package org.example.social_network.domain.validators;

import org.example.social_network.domain.Message;

public class MessageValidator implements Validator<Message> {
    public void validate(Message entity) throws ValidationException {
        if(entity.getText().isEmpty())
            throw new ValidationException("Mesajul nu este valid (text gol)");
        if(entity.getToIdList().isEmpty()){
            throw new ValidationException("Mesajul nu este valid (nu are destinatar)");
        }

    }
}
