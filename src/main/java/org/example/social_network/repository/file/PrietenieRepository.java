package org.example.social_network.repository.file;

import org.example.social_network.domain.Prietenie;
import org.example.social_network.domain.Tuple;
import org.example.social_network.domain.validators.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class PrietenieRepository extends AbstractFileRepository<Tuple<Long,Long>, Prietenie> {

    Supplier<DateTimeFormatter> formatterSupplier = () -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public PrietenieRepository(Validator<Prietenie> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Prietenie createEntity(String line) {
        String[] split = line.split(";");

        Prietenie p = new Prietenie(LocalDateTime.parse(split[2], formatterSupplier.get()));
        p.setId(new Tuple<Long, Long>(Long.parseLong(split[0]), Long.parseLong(split[1])));
        return p;
    }

    @Override
    public String saveEntity(Prietenie entity) {
        return entity.getId().getLeft() + ";" + entity.getId().getRight() + ";" + entity.getFriendsFrom().format(formatterSupplier.get());
    }


}
