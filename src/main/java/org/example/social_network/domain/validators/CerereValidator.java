package org.example.social_network.domain.validators;

import org.example.social_network.domain.Cerere;

import java.util.Arrays;
import java.util.List;

public class CerereValidator implements Validator<Cerere> {

    private List<String> statuses = Arrays.asList("Pending", "Accepted", "Rejected");

    @Override
    public void validate(Cerere entity) throws ValidationException {
        if(entity.getId() == null || entity.getId().getLeft().equals(entity.getId().getRight()))
            throw new ValidationException("Cerere invalida");
        if(!statuses.contains(entity.getStatus()))
            throw new ValidationException("Status cerere invalid");
    }
}
