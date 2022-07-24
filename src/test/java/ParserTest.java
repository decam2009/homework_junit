import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ParserTest {

    //given
    private final List<Employee> employeeList = Arrays.asList(new Employee(1L, "John", "Smith", "USA", 25), new Employee(2L, "Ivan", "Petrov", "RU", 23));

    @Test
    public void testReturnEmployeeListFromCSV_validResult_objectsEquals() {
        //when
        final List<Employee> result = Main.parseCSV(new String[]{"id", "firstName", "lastName", "country", "age"}, "data.csv");
        //then
        Assertions.assertEquals(employeeList, result);
        Assertions.assertArrayEquals(new List[]{employeeList}, new List[]{result});
        Assertions.assertNotNull(result, "Test assert not null failed");
    }

    @Test
    public void testWriteString_validResult_LinesMatches() {
        //given
        final String expected = "Test passed";
        String result = "";
        //when
        Main.writeString("Test passed", "test.txt");
        int c;
        try (FileReader fr = new FileReader("test.txt")) {
            while ((c = fr.read()) != -1) {
                result += (char) c;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected, result, "Test 1 failed");
        Assertions.assertLinesMatch(Collections.singletonList(expected), Collections.singletonList(result), "Test 2 failed");
    }

    @Test
    public void testParsingListToJson_validResult_LinesMatches() {
        //given
        final String expected = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"Ivan\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";
        //when
        String result = Main.listToJson(employeeList);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected, result);
        Assertions.assertLinesMatch(Collections.singletonList(expected), Collections.singletonList(result));
    }

    @Test
    public void testParsingToXML_validResult_ObjectsEquals() throws ParserConfigurationException, IOException, SAXException {
        //when
        List<Employee> result = Main.parseXML("data.xml");
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(employeeList, result);
    }

}
