package xml;

import model.Person;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class XmlUtilsTest {
    private static final String FILE_PATH = "./src/test/resources/Storage.xml";
    private final Person testPerson = Person.builder()
            .serviceNumber(1)
            .surname("Иванов")
            .name("Иван")
            .patronymic("Иванович")
            .position("Разработчик")
            .organization("Рога и Копыта")
            .email("ivanov@email")
            .phoneNumbers(List.of("+7123456789", "+7000000000"))
            .build();

    @BeforeEach
    public void init() throws TransformerException, ParserConfigurationException, IOException, SAXException {
        Files.createFile(Path.of(FILE_PATH));
        Document document = XmlUtils.createNewDocument(FILE_PATH);
        Element root = document.getDocumentElement();
        Element personAsElement = XmlUtils.getElementFromPerson(document, testPerson);
        root.appendChild(personAsElement);
        XmlUtils.writeToFile(document, FILE_PATH);
    }

    @AfterEach
    public void clearFile() throws IOException {
        Files.delete(Path.of(FILE_PATH));
    }

    @Test
    public void shouldReadData() {

        Map<Integer, Person> read = XmlUtils.readAllDataFrom(FILE_PATH);

        assertThat(read).asInstanceOf(InstanceOfAssertFactories.map(Integer.class, Person.class))
                .containsKey(testPerson.getServiceNumber())
                .containsValue(testPerson);
    }

    @Test
    public void shouldWriteData() {
        Person expected = Person.builder()
                .serviceNumber(2)
                .surname("Петров")
                .name("Петр")
                .patronymic("Петрович")
                .position("Разработчик")
                .organization("Рога и Копыта")
                .email("petrov@email")
                .phoneNumbers(List.of("+7876543210", "+7000000000"))
                .build();

        XmlUtils.addRecordTo(FILE_PATH, expected);

        Map<Integer, Person> read = XmlUtils.readAllDataFrom(FILE_PATH);

        assertThat(read).asInstanceOf(InstanceOfAssertFactories.map(Integer.class, Person.class))
                .hasSize(2)
                .containsKey(expected.getServiceNumber())
                .containsValue(expected);
    }

    @Test
    public void shouldUpdateExistingData() {

        Person expected = Person.builder()
                .serviceNumber(testPerson.getServiceNumber())
                .surname(testPerson.getSurname())
                .name(testPerson.getName())
                .patronymic(testPerson.getPatronymic())
                .position("Старший разработчик")
                .organization(testPerson.getOrganization())
                .email(testPerson.getEmail())
                .phoneNumbers(testPerson.getPhoneNumbers())
                .build();

        Map<Integer, Person> beforeUpdate = XmlUtils.readAllDataFrom(FILE_PATH);

        assertThat(beforeUpdate).asInstanceOf(InstanceOfAssertFactories.map(Integer.class, Person.class))
                .containsKey(expected.getServiceNumber())
                .doesNotContainValue(expected);

        XmlUtils.updateRecord(FILE_PATH, expected);

        Map<Integer, Person> afterUpdate = XmlUtils.readAllDataFrom(FILE_PATH);
        assertThat(afterUpdate).asInstanceOf(InstanceOfAssertFactories.map(Integer.class, Person.class))
                .containsKey(expected.getServiceNumber())
                .containsValue(expected);

    }


}