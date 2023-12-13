import pojo.IssoData;
import pojo.ShortIsso;
import properties.IssoCodesTypes;
import properties.MyConfig;
import workers.ExcelReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        String sheetName = "Лист 1";
        ExcelReader excelReader = new ExcelReader();

        try {
            Set<Integer> excludedTypes = Set.of(70, 90);
            Set<Integer> validIssoTypes = IssoCodesTypes.getFitleredIssoTypes(excludedTypes);
            Map<Integer, List<String>> mappingIds = excelReader.getMapOfIdsFromExcelMappingFile(MyConfig.MAPPING_FILE, sheetName);

            IssoProvider issoProvider = new IssoProvider(mappingIds);

            List<ShortIsso> filteredShortIsso = issoProvider.getShortIssoWithGivenTypes(validIssoTypes);
            List<IssoData> issoDataFromShort = issoProvider.convertShortIssoToIssoData(filteredShortIsso);
            List<IssoData> issoDataFromFull = issoProvider.getIssoDataWithGivenTypes(validIssoTypes);

            issoProvider.createExcelFileBasedOnData(issoDataFromShort, "issoDataResultShort");
            issoProvider.createExcelFileBasedOnData(issoDataFromFull, "issoDataResultFull");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
