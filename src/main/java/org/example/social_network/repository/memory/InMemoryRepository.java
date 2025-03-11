package org.example.social_network.repository.memory;


import org.example.social_network.domain.Entity;
import org.example.social_network.domain.validators.ValidationException;
import org.example.social_network.domain.validators.Validator;
import org.example.social_network.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private Validator<E> validator;
    protected Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        if(id == null){
            throw new IllegalArgumentException("ID invalid");
        }
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) throws ValidationException {

        /**
         *
         * @param entity
         *         entity must be not null
         * @return null- if the given entity is saved
         *         otherwise returns the entity (id already exists)
         * @throws ValidationException
         *            if the entity is not valid
         * @throws IllegalArgumentEx    ception
         *             if the given entity is null.     *
         */

        if(entity==null)
            throw new IllegalArgumentException("ENTITY CANNOT BE NULL");
        validator.validate(entity);

        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));


    }

    @Override
    public Optional<E> delete(ID id) {
        if(id == null){
            throw new IllegalArgumentException("ID invalid");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        if(entity == null){
            throw new IllegalArgumentException("Entity is null");
        }
        if(entities.containsKey(entity.getId())){
            validator.validate((entity));
            entities.put(entity.getId(), entity);
            return Optional.empty();
        }
        return Optional.of(entity);
    }
}
