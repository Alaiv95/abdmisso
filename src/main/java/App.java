import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.IssoData;
import properties.IssoCodesTypes;
import properties.MyConfig;
import services.DataBase;
import workers.ExcelReader;
import workers.IntersectionVerifier;

import java.io.IOException;
import java.util.*;

public class App {
    public static void main(String[] args) {
        String sheetName = "Лист 1";
        ExcelReader excelReader = new ExcelReader();
        Logger logger = LoggerFactory.getLogger(App.class);

        try {
            Set<Integer> excludedTypes = Set.of(70, 90);
            Set<Integer> validIssoTypes = IssoCodesTypes.getFitleredIssoTypes(excludedTypes);
            Map<Integer, List<String>> mappingIds = excelReader.getMapOfIdsFromExcelMappingFile(MyConfig.MAPPING_FILE, sheetName);
            IssoProvider fullIssoProvider = new FullIssoProvider(mappingIds);

            List<IssoData> issoDataFromFull = fullIssoProvider.getIssoDataWithGivenTypes(validIssoTypes);
            IntersectionVerifier.setIntersectedRoadIds(issoDataFromFull, mappingIds);

            fullIssoProvider.createExcelFileBasedOnData(issoDataFromFull, "issoDataResultFull");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
