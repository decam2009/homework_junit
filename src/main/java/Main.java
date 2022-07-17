import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
    private static final String jsonFileName = "data.csv";
    private static final String xmlFileName = "data.xml";

    public static List<Employee> parseCSV(String[] mapping, String fileName) {
        List<Employee> list = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(mapping[0], mapping[1], mapping[2], mapping[3], mapping[4]);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader).withMappingStrategy(strategy).build();
            list = csv.parse();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public static String listToJson(List<Employee> employeeList) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(employeeList, listType);
    }

    public static void writeString(String inputString, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(inputString);
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<Employee> read(Node node) {
        NodeList nodeList = node.getChildNodes();
        List<Employee> employeeList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node dataNode = nodeList.item(i);
            if (Node.ELEMENT_NODE == dataNode.getNodeType()) {
                Element employee = (Element) dataNode;

                long id = Long.parseLong(employee.getElementsByTagName(columnMapping[0]).item(0).getTextContent());
                String firstname = employee.getElementsByTagName(columnMapping[1]).item(0).getTextContent();
                String lastName = employee.getElementsByTagName(columnMapping[2]).item(0).getTextContent();
                String country = employee.getElementsByTagName(columnMapping[3]).item(0).getTextContent();
                int age = Integer.parseInt(employee.getElementsByTagName(columnMapping[4]).item(0).getTextContent());
                employeeList.add(new Employee(id, firstname, lastName, country, age));
            }
        }
        return employeeList;
    }

    public static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));

        Node root = doc.getDocumentElement();
        return read(root);
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> listFromCSV = parseCSV(columnMapping, jsonFileName);
        List<Employee> listFromXML = parseXML(xmlFileName);
        writeString(listToJson(listFromCSV), "JSONFromCSV.json");
        writeString(listToJson(listFromXML), "JSONFromXML.json");
        System.out.println("Конвертация данных завершена!");
    }
}

