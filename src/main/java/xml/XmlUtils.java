package xml;

import model.Person;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class XmlUtils {
    private XmlUtils() {
    }

    public static Map<Integer, Person> readAllDataFrom(String fileName) {
        try {
            Map<Integer, Person> persons = new HashMap<>();

            Document doc = parseDocument(fileName);
            Element root = doc.getDocumentElement();
            NodeList nodes = root.getChildNodes();

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node instanceof Element e) {
                    Person person = getPersonFromElement(e);
                    persons.put(person.getServiceNumber(), person);
                }
            }
            return persons;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

    }

    public static void addRecordTo(String fileName, Person person) {
        try {
            Document doc = parseDocument(fileName);
            Element root = doc.getDocumentElement();
            root.appendChild(getElementFromPerson(doc, person));
            writeToFile(doc, fileName);
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateRecord(String fileName, Person person) {
        try {
            Document doc = parseDocument(fileName);
            Element root = doc.getDocumentElement();
            Element newVersion = getElementFromPerson(doc, person);
            root.replaceChild(newVersion, findOldVersion(root, person.getServiceNumber()));
            writeToFile(doc, fileName);
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document parseDocument(String fileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = f.newDocumentBuilder();
        return b.parse(new File(fileName));
    }

    public static Document createNewDocument(String filePath) throws ParserConfigurationException, TransformerException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = document.createElement("persons");
        document.appendChild(root);
        writeToFile(document, filePath);
        return document;
    }

    public static void writeToFile(Document document, String fileName) throws TransformerException {
        DOMSource source = new DOMSource(document);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        StreamResult result = new StreamResult(fileName);
        transformer.transform(source, result);
    }


    public static Element getElementFromPerson(Document doc, Person person) {
        Element personRoot = doc.createElement("person");
        personRoot.setAttribute("serviceNumber", person.getServiceNumber().toString());

        Element surname = doc.createElement("surname");
        surname.appendChild(doc.createTextNode(person.getSurname()));
        personRoot.appendChild(surname);

        Element name = doc.createElement("name");
        name.appendChild(doc.createTextNode(person.getName()));
        personRoot.appendChild(name);

        Element patronymic = doc.createElement("patronymic");
        patronymic.appendChild(doc.createTextNode(person.getPatronymic()));
        personRoot.appendChild(patronymic);

        Element position = doc.createElement("position");
        position.appendChild(doc.createTextNode(person.getPosition()));
        personRoot.appendChild(position);

        Element organization = doc.createElement("organization");
        organization.appendChild(doc.createTextNode(person.getOrganization()));
        personRoot.appendChild(organization);

        Element email = doc.createElement("email");
        email.appendChild(doc.createTextNode(person.getEmail()));
        personRoot.appendChild(email);

        Element phoneNumbers = doc.createElement("phoneNumbers");
        phoneNumbers.appendChild(doc.createTextNode(String.join(", ", person.getPhoneNumbers())));
        personRoot.appendChild(phoneNumbers);

        return personRoot;

    }

    private static Element findOldVersion(Element root, Integer serviceNumber) {
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child instanceof Element e && e.getAttribute("serviceNumber").equals(serviceNumber.toString())) {
                return e;
            }
        }
        throw new IllegalArgumentException();
    }

    private static Person getPersonFromElement(Element element) {

        Person.PersonBuilder builder = Person.builder();
        Integer serviceNumber = Integer.parseInt(element.getAttribute("serviceNumber"));
        builder.serviceNumber(serviceNumber);

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child instanceof Element e) {
                switch (e.getTagName()) {
                    case "name" -> builder.name(getTextFromTextNode((Text) e.getFirstChild()));
                    case "surname" -> builder.surname(getTextFromTextNode((Text) e.getFirstChild()));
                    case "patronymic" -> builder.patronymic(getTextFromTextNode((Text) e.getFirstChild()));
                    case "position" -> builder.position(getTextFromTextNode((Text) e.getFirstChild()));
                    case "organization" -> builder.organization(getTextFromTextNode((Text) e.getFirstChild()));
                    case "email" -> builder.email(getTextFromTextNode((Text) e.getFirstChild()));
                    case "phoneNumbers" -> builder.phoneNumbers(getListFromTextNode((Text) e.getFirstChild()));
                    default -> System.out.println("Unknown tag " + e.getTagName());
                }
            }
        }
        return builder.build();
    }

    private static String getTextFromTextNode(Text node) {
        return node.getData().trim();
    }

    private static List<String> getListFromTextNode(Text node) {
        return Arrays.stream(node.getData().trim().split(", ")).toList();
    }

}
