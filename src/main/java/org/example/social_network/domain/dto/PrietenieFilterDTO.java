package org.example.social_network.domain.dto;

import java.util.Optional;

public class PrietenieFilterDTO implements FilterDTO{

    private Optional<Long> id = Optional.empty();

    public Optional<Long> getId() {
        return id;
    }

    public void setId(Optional<Long> id) {
        this.id = id;
    }
}
