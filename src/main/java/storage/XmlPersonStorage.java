package storage;

import model.Person;
import model.SearchParameters;
import xml.XmlUtils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class XmlPersonStorage implements PersonStorage {

    private final String fileName;
    private final InMemoryPersonStorage personStorage;

    public XmlPersonStorage(String fileName) {
        this.fileName = fileName;
        try {
            Path filePath = Path.of(fileName);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                XmlUtils.createNewDocument(fileName);
                this.personStorage = new InMemoryPersonStorage();
            } else {
                Map<Integer, Person> personsFromFile = XmlUtils.readAllDataFrom(fileName);
                this.personStorage = new InMemoryPersonStorage(personsFromFile);
            }
        } catch (IOException | ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person add(Person person) {
        Person added = personStorage.add(person);
        XmlUtils.addRecordTo(fileName, added);
        return added;
    }

    @Override
    public Person edit(int id, Person person) {
        Person edited = personStorage.edit(id, person);
        XmlUtils.updateRecord(fileName, edited);
        return edited;
    }

    @Override
    public List<Person> findAll() {
        return personStorage.findAll();
    }

    @Override
    public Optional<Person> findOne(SearchParameters parameters) {
        return personStorage.findOne(parameters);
    }


}
