package storage;

import model.Person;
import model.SearchParameters;

import java.util.List;
import java.util.Optional;

public interface PersonStorage {
    Person add(Person person);

    Person edit(int id, Person person);

    List<Person> findAll();

    Optional<Person> findOne(SearchParameters parameters);
}
