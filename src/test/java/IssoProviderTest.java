import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import pojo.IssoData;
import properties.IssoCodesTypes;
import workers.ExcelReader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IssoProviderTest {
    IssoProvider shortIssoProvider;
    IssoProvider fullIssoProvider;


    @BeforeAll
    public void init() throws IOException {
        String filePath = "src/main/resources/abdmMapping.xlsx";
        ExcelReader excelReader = new ExcelReader();

        Map<Integer, List<String>> mappingIds = excelReader.getMapOfIdsFromExcelMappingFile(filePath, "Лист 1");
        shortIssoProvider = new ShortIssoProvider(mappingIds);
        fullIssoProvider = new FullIssoProvider(mappingIds);
    }


    @Test
    public void getIssoDataFromShort() {
        Set<Integer> includedIssoTypes = Set.of(20);
        List<IssoData> issoData = shortIssoProvider.getIssoDataWithGivenTypes(includedIssoTypes);

        Assertions.assertFalse(issoData.isEmpty());
        Assertions.assertNotNull(issoData.get(0));
        Assertions.assertEquals("20", issoData.get(0).getIssoTypeCode());
    }

    @Test
    public void createExcelFileFromShortIssos() throws IOException {
        Set<Integer> validIssoTypes = Set.of(20);
        List<IssoData> issoData = shortIssoProvider.getIssoDataWithGivenTypes(validIssoTypes);

        String fileName = "testFile2";
        shortIssoProvider.createExcelFileBasedOnData(issoData, fileName);

        File file = new File(fileName + ".xlsx");

        Assertions.assertTrue(file.exists());
        Assertions.assertEquals(fileName + ".xlsx", file.getName());
    }

    @Test
    public void getFullIssoData()  {
        Set<Integer> includedIssoTypes = Set.of(20);
        List<IssoData> issoData = fullIssoProvider.getIssoDataWithGivenTypes(includedIssoTypes).subList(0, 1);

        Assertions.assertFalse(issoData.isEmpty());
        Assertions.assertNotNull(issoData.get(0));
        Assertions.assertEquals("20", issoData.get(0).getIssoTypeCode());
    }

    @Test
    public void createExcelFileFromFullIssos() throws IOException {
        Set<Integer> validIssoTypes = Set.of(20);
        List<IssoData> issoData = fullIssoProvider.getIssoDataWithGivenTypes(validIssoTypes).subList(0, 1);

        String fileName = "testFile";
        fullIssoProvider.createExcelFileBasedOnData(issoData, fileName);

        File file = new File(fileName + ".xlsx");

        Assertions.assertTrue(file.exists());
        Assertions.assertEquals(fileName + ".xlsx", file.getName());
    }

    @Test
    public void bothMethodsGiveSameResult() {
        Set<Integer> excludedIssoTypes = Set.of(20);
        List<IssoData> issoData1 = shortIssoProvider.getIssoDataWithGivenTypes(excludedIssoTypes);
        List<IssoData> issoData2 = fullIssoProvider.getIssoDataWithGivenTypes(excludedIssoTypes);


        Assertions.assertEquals(issoData1.size(), issoData2.size());
        Assertions.assertTrue(issoData1.containsAll(issoData2) && issoData2.containsAll(issoData1));
    }

    @Test
    public void getTypes() {
        Set<Integer> excludedTypes = Set.of(70, 90);
        Set<Integer> validIssoTypes = IssoCodesTypes.getFitleredIssoTypes(excludedTypes);

        Assertions.assertFalse(validIssoTypes.containsAll(excludedTypes));
    }
}
