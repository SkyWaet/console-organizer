package storage;

import model.Person;
import model.SearchParameters;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class InMemoryPersonStorageTest {

    private final PersonStorage storage = new InMemoryPersonStorage();

    @Test
    void shouldAdd() {
        Person person = Person.builder()
                .surname("Sidorov")
                .name("Ivan")
                .patronymic("Petrovich")
                .position("developer")
                .organization("someorg")
                .email("sidorov@some.org")
                .phoneNumbers(List.of("+7123456789"))
                .build();

        Person added = storage.add(person);
        assertThat(added).usingRecursiveComparison()
                .ignoringFields("serviceNumber")
                .isEqualTo(person);

        assertThat(added.getServiceNumber()).isGreaterThan(0);

    }

    @Test
    void shouldEdit() {
        Person oldPerson = Person.builder()
                .surname("Sidorov")
                .name("Ivan")
                .patronymic("Petrovich")
                .position("developer")
                .organization("someorg")
                .email("sidorov@some.org")
                .phoneNumbers(List.of("+7123456789"))
                .build();

        Person added = storage.add(oldPerson);
        Person dataToUpdate = Person.builder()
                .surname("Sidorov")
                .name("Ivan")
                .patronymic("Petrovich")
                .position("developer")
                .organization("someotherorg")
                .email("sidorov@some.other.org")
                .phoneNumbers(List.of("+7123456789", "+700000000"))
                .build();

        Person updated = storage.edit(added.getServiceNumber(), dataToUpdate);

        assertThat(updated).isNotEqualTo(added);
        assertThat(updated).usingRecursiveComparison()
                .ignoringFields("serviceNumber")
                .isEqualTo(dataToUpdate);
    }

    @Test
    void shouldFindAll() {
        Person oldPerson = Person.builder()
                .surname("Sidorov")
                .name("Ivan")
                .patronymic("Petrovich")
                .position("developer")
                .organization("someorg")
                .email("sidorov@some.org")
                .phoneNumbers(List.of("+7123456789"))
                .build();

        Person added = storage.add(oldPerson);
        List<Person> all = storage.findAll();

        assertThat(all).asList().containsExactly(added);
    }

    @Test
    void shouldFindOne() {
        Person first = storage.add(Person.builder()
                .surname("Sidorov")
                .name("Ivan")
                .patronymic("Petrovich")
                .position("developer")
                .organization("someorg")
                .email("sidorov@some.org")
                .phoneNumbers(List.of("+700000000"))
                .build());
        Person second = storage.add(Person.builder()
                .surname("Petrov")
                .name("Petr")
                .patronymic("Ivanovich")
                .position("QA")
                .organization("someorg")
                .email("petrov@some.org")
                .phoneNumbers(List.of("+7123456789"))
                .build());

        SearchParameters searchByName = SearchParameters.builder().name(first.getName()).build();
        assertThat(storage.findOne(searchByName))
                .asInstanceOf(InstanceOfAssertFactories.optional(Person.class))
                .contains(first);

        SearchParameters searchByOrganizationAndPosition = SearchParameters.builder()
                .organization(second.getOrganization())
                .position(second.getPosition())
                .build();

        assertThat(storage.findOne(searchByOrganizationAndPosition))
                .asInstanceOf(InstanceOfAssertFactories.optional(Person.class))
                .contains(second);

        SearchParameters searchByOrganizationOnly = SearchParameters.builder()
                .organization(second.getOrganization())
                .build();

        assertThat(storage.findOne(searchByOrganizationOnly))
                .asInstanceOf(InstanceOfAssertFactories.optional(Person.class))
                .get()
                .isIn(first, second);
    }
}