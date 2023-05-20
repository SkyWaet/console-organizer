package model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public final class Person {
    private Integer serviceNumber;
    private String name;
    private String surname;
    private String patronymic;
    private String position;
    private String organization;
    private String email;
    private List<String> phoneNumbers;

}
