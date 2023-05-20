package model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

@Data
@Builder
public class SearchParameters {
    private Integer serviceNumber;
    private String name;
    private String surname;
    private String patronymic;
    private String position;
    private String organization;
    private String email;
    private List<String> phoneNumbers;

    public Predicate<Person> toPredicate() {
        List<Predicate<Person>> predicates = new ArrayList<>();
        if (serviceNumber != null && serviceNumber > 0) {
            predicates.add(person -> person.getServiceNumber().equals(serviceNumber));
        }
        if (name != null && !name.isBlank()) {
            predicates.add(person -> person.getName().equals(name));
        }
        if (surname != null && !surname.isBlank()) {
            predicates.add(person -> person.getSurname().equals(surname));
        }
        if (patronymic != null && !patronymic.isBlank()) {
            predicates.add(person -> person.getPatronymic().equals(patronymic));
        }
        if (position != null && !position.isBlank()) {
            predicates.add(person -> person.getPosition().equals(position));
        }
        if (organization != null && !organization.isBlank()) {
            predicates.add(person -> person.getOrganization().equals(organization));
        }
        if (email != null && !email.isBlank()) {
            predicates.add(person -> person.getEmail().equals(email));
        }
        if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
            predicates.add(person -> new HashSet<>(person.getPhoneNumbers()).containsAll(phoneNumbers));
        }

        return predicates.stream().reduce(Predicate::and).orElseThrow(IllegalAccessError::new);
    }

}
