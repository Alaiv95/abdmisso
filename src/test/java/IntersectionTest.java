import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import models.IssoData;
import workers.ExcelReader;
import workers.IntersectionVerifier;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntersectionTest {
    IssoProvider fullIssoProvider;
    Map<Integer, List<String>> mappingIds;
    @BeforeAll
    public void init() throws IOException {
        String filePath = "src/main/resources/abdmMappingTest.xlsx";
        ExcelReader excelReader = new ExcelReader();

         mappingIds = excelReader.getMapOfIdsFromExcelMappingFile(filePath, "Лист 1");
        fullIssoProvider = new FullIssoProvider(mappingIds);
    }


    @Test
    public void checkIntersection() {
        int cIsso1 = 2401917;
        String expectedRoadId1 = "5c4fdbfa-73f0-443b-9a5e-9c4ba5a3ea23";

        int cIsso2 = 2402121;
        String expectedRoadId2 = "Пересечений не найдено";

        Set<Integer> includedIssoTypes = Set.of(80);
        List<IssoData> issoData = fullIssoProvider.getIssoDataWithGivenTypes(includedIssoTypes);
        IntersectionVerifier.setIntersectedRoadIds(issoData, mappingIds);

        IssoData issoData1 = issoData.stream().filter(data -> data.getCIsso() == cIsso1).findFirst().orElseThrow();
        IssoData issoData2 = issoData.stream().filter(data -> data.getCIsso() == cIsso2).findFirst().orElseThrow();

        Assertions.assertEquals(expectedRoadId1, issoData1.getRoadsWithMatchingLen().trim());
        Assertions.assertEquals(expectedRoadId2, issoData2.getRoadsWithMatchingLen().trim());
    }
}
