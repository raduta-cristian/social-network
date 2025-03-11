package org.example.social_network.domain.validators;

import org.example.social_network.domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie> {
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(entity.getId() == null || entity.getId().getLeft().equals(entity.getId().getRight()))
            throw new ValidationException("Prietenia e invalida");

    }
}
