package org.example.social_network.domain.validators;


import org.example.social_network.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if(entity.getFirstName().isEmpty())
            throw new ValidationException("Utilizatorul nu este valid");
        if(entity.getLastName().isEmpty())
            throw new ValidationException("Utilizatorul nu este valid");
    }
}
