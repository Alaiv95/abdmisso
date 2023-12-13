import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import pojo.IssoData;
import pojo.ShortIsso;
import properties.IssoCodesTypes;
import workers.ExcelReader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IssoProviderTest {
    IssoProvider issoProvider;


    @BeforeAll
    public void init() throws IOException {
        String filePath = "src/main/resources/abdmMapping.xlsx";
        ExcelReader excelReader = new ExcelReader();

        Map<Integer, List<String>> mappingIds = excelReader.getMapOfIdsFromExcelMappingFile(filePath, "Лист 1");
        issoProvider = new IssoProvider(mappingIds);
    }

    @Test
    public void getFilteredDataTest() {
        int validIssoType = 10;
        Set<Integer> validIssoTypes = Set.of(validIssoType);

        List<ShortIsso> issos = issoProvider.getShortIssoWithGivenTypes(validIssoTypes);
        List<ShortIsso> filteredIssos = issos.stream().filter(isso -> validIssoTypes.contains(isso.getCTypisso())).toList();

        Assertions.assertEquals(filteredIssos, issos);
        Assertions.assertEquals(validIssoType, issos.get(0).getCTypisso());
    }


    @Test
    public void convertIssoCodeToData() {
        Set<Integer> validIssoTypes = Set.of(10);
        List<ShortIsso> issos = issoProvider.getShortIssoWithGivenTypes(validIssoTypes).subList(0, 1);
        List<IssoData> issoData = issoProvider.convertShortIssoToIssoData(issos);

        Assertions.assertFalse(issoData.isEmpty());
        Assertions.assertNotNull(issoData.get(0));
        Assertions.assertEquals(issoData.get(0).getCIsso(), issos.get(0).getCIsso());
    }

    @Test
    public void createExcelFileFromShortIssos() throws IOException {
        Set<Integer> validIssoTypes = Set.of(20);
        List<ShortIsso> issos = issoProvider.getShortIssoWithGivenTypes(validIssoTypes).subList(0, 5);
        List<IssoData> issoData = issoProvider.convertShortIssoToIssoData(issos);

        String fileName = "testFile2";
        issoProvider.createExcelFileBasedOnData(issoData, fileName);

        File file = new File(fileName + ".xlsx");

        Assertions.assertTrue(file.exists());
        Assertions.assertEquals(fileName + ".xlsx", file.getName());
    }

    @Test
    public void getFullIssoData()  {
        Set<Integer> excludedIssoTypes = IssoCodesTypes.getFitleredIssoTypes(
                Set.of(10, 30, 40, 50, 65, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 180, 220, 230));
        List<IssoData> issoData = issoProvider.getIssoDataWithGivenTypes(excludedIssoTypes).subList(0, 1);

        Assertions.assertFalse(issoData.isEmpty());
        Assertions.assertNotNull(issoData.get(0));
        Assertions.assertEquals("20", issoData.get(0).getIssoTypeCode());
    }

    @Test
    public void createExcelFileFromFullIssos() throws IOException {
        Set<Integer> validIssoTypes = Set.of(20);
        List<IssoData> issoData = issoProvider.getIssoDataWithGivenTypes(validIssoTypes).subList(0, 1);

        String fileName = "testFile";
        issoProvider.createExcelFileBasedOnData(issoData, fileName);

        File file = new File(fileName + ".xlsx");

        Assertions.assertTrue(file.exists());
        Assertions.assertEquals(fileName + ".xlsx", file.getName());
    }

    @Test
    public void bothMethodsGiveSameResult() {
        Set<Integer> validIssoTypes = Set.of(20);
        List<ShortIsso> issos = issoProvider.getShortIssoWithGivenTypes(validIssoTypes);
        List<IssoData> issoData1 = issoProvider.convertShortIssoToIssoData(issos);
        List<IssoData> issoData2 = issoProvider.getIssoDataWithGivenTypes(validIssoTypes);


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
