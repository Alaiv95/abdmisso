import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.IssoData;
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
        Logger logger = LoggerFactory.getLogger(App.class);


        try {
            Set<Integer> excludedTypes = Set.of(70, 90);
            Set<Integer> validIssoTypes = IssoCodesTypes.getFitleredIssoTypes(excludedTypes);
            Map<Integer, List<String>> mappingIds = excelReader.getMapOfIdsFromExcelMappingFile(MyConfig.MAPPING_FILE, sheetName);

//            IssoProvider shortIssoProvider = new ShortIssoProvider(mappingIds);
            IssoProvider fullIssoProvider = new FullIssoProvider(mappingIds);

//            List<IssoData> issoDataFromShort = shortIssoProvider.getIssoDataWithGivenTypes(validIssoTypes);
            List<IssoData> issoDataFromFull = fullIssoProvider.getIssoDataWithGivenTypes(validIssoTypes);

//            shortIssoProvider.createExcelFileBasedOnData(issoDataFromShort, "issoDataResultShort");
            fullIssoProvider.createExcelFileBasedOnData(issoDataFromFull, "issoDataResultFull");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }
}
