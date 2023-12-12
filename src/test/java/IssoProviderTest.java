import io.restassured.internal.common.assertion.Assertion;
import org.junit.jupiter.api.*;
import pojo.IssoData;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IssoProviderTest {
    IssoProvider issoProvider;


    @BeforeAll
    public void init() {
        issoProvider = new IssoProvider();
    }

    @Test
    public void getFilteredDataTest() {
        Set<Integer> validIssoTypes = Set.of(10, 20, 30, 40, 50, 65, 70, 80, 100, 110, 120, 130, 140, 150, 160, 180, 220, 230);
        int includedIssoTypeCode = 3303327;
        int excludedIssoTypeCode = 2002119;

        List<Integer> codes = issoProvider.getFilteredIssoCodes(validIssoTypes);

        Assertions.assertTrue(codes.contains(includedIssoTypeCode));
        Assertions.assertFalse(codes.contains(excludedIssoTypeCode));
    }


    @Test
    public void convertIssoCodeToData() {
        Set<Integer> validIssoTypes = Set.of(10);
        List<Integer> codes = issoProvider.getFilteredIssoCodes(validIssoTypes).subList(0, 1);
        List<IssoData> issoData = issoProvider.convertIssoCodesToIssoData(codes);

        Assertions.assertFalse(issoData.isEmpty());
        Assertions.assertNotNull(issoData.get(0));
        Assertions.assertEquals((int) codes.get(0), issoData.get(0).getCIsso());
    }

    @Test
    public void createExcelFile() throws IOException {
        Set<Integer> validIssoTypes = Set.of(10);
        List<Integer> codes = issoProvider.getFilteredIssoCodes(validIssoTypes).subList(0, 1);
        List<IssoData> issoData = issoProvider.convertIssoCodesToIssoData(codes);

        String fileName = "testFile";
        issoProvider.createExcelFileBasedOnData(issoData, fileName);

        File file = new File(fileName + ".xlsx");

        Assertions.assertTrue(file.exists());
        Assertions.assertEquals(fileName + ".xlsx", file.getName());
    }
}
