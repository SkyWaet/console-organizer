package storage;

import model.Person;
import model.SearchParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryPersonStorage implements PersonStorage {

    private final Map<Integer, Person> storage;
    private Integer maxServiceNumber;

    public InMemoryPersonStorage(Map<Integer, Person> storage) {
        this.storage = storage;
        this.maxServiceNumber = storage.keySet().stream().max(Integer::compareTo).orElse(0);
    }

    public InMemoryPersonStorage() {
        this(new HashMap<>());
    }

    @Override
    public Person add(Person person) {
        person.setServiceNumber(++maxServiceNumber);
        storage.put(person.getServiceNumber(), person);
        return person;
    }

    @Override
    public Person edit(int id, Person person) {
        if (storage.containsKey(id)) {
            person.setServiceNumber(id);
            storage.put(id, person);
            return person;
        }
        throw new RuntimeException();
    }

    @Override
    public List<Person> findAll() {
        return storage.values().stream().toList();
    }

    @Override
    public Optional<Person> findOne(SearchParameters parameters) {
        return storage.values().stream()
                .filter(parameters.toPredicate())
                .findAny();
    }

}
