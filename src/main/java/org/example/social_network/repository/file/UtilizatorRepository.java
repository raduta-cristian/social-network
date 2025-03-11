package org.example.social_network.repository.file;

import org.example.social_network.domain.Entity;
import org.example.social_network.domain.Utilizator;
import org.example.social_network.domain.validators.Validator;

import java.util.Optional;


public class UtilizatorRepository extends AbstractFileRepository<Long, Utilizator>{
    public UtilizatorRepository(Validator<Utilizator> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Utilizator createEntity(String line) {
        String[] split = line.split(";");
        Utilizator u = new Utilizator(split[1], split[2], split[3], split[4]);
        u.setId(Long.parseLong(split[0]));
        return u;
    }

    @Override
    public String saveEntity(Utilizator entity) {
       return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName()+ entity.getEmail()+ entity.getPassword();
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        entity.setId(getAvailableId());
        Optional<Utilizator> e = super.save(entity);
        if (e.isPresent())
            super.writeToFile();
        return e;
    }

    private Long getAvailableId(){
        return entities.values().stream().map(Entity::getId).max(Long::compareTo).orElse(0L) + 1L;
    }
}
