package org.example.social_network.repository.file;

import org.example.social_network.domain.Entity;
import org.example.social_network.domain.validators.Validator;
import org.example.social_network.repository.RepositoryException;
import org.example.social_network.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E>{
    private final String filename;

    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        filename=fileName;
    }


    public abstract E createEntity(String line);
    public abstract String saveEntity(E entity);

    @Override
    public Optional<E> findOne(ID id) {
        loadData();
        return super.findOne(id);
    }

    @Override
    public Iterable<E> findAll() {
        loadData();
        return super.findAll();
    }

    @Override
    public Optional<E> save(E entity) {
        Optional<E> e = super.save(entity);
        if (e.isPresent())
            writeToFile();
        return e;
    }

    private void loadData(){
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine())  != null){
                E entity = createEntity(line);
                super.save(entity);
            }
        }
        catch (IOException e){
            throw new RepositoryException("Eroare la citire fisier");
        }
    }

    protected void writeToFile() {

        try  ( BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for (E entity: entities.values()) {
                String ent = saveEntity(entity);
                writer.write(ent);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RepositoryException("Eroare la scriere fisier");
        }

    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> deleted = super.delete(id);
        if(deleted.isPresent())
            writeToFile();
        return deleted;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> updated = super.update(entity);
        if(updated.isEmpty())
            writeToFile();
        return updated;
    }
}
