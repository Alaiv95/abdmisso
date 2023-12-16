import database.IssoDataTable;
import database.NoIntersectErrorsTable;
import database.NoValueErrorsTable;
import models.IssoData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import properties.IssoCodesTypes;
import properties.MyConfig;
import workers.AbdmErrorsLog;
import workers.ExcelReader;
import workers.IntersectionVerifier;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class App {
    private static final NoIntersectErrorsTable noIntersectErrorsTable = new NoIntersectErrorsTable();
    private static final NoValueErrorsTable noValueErrorsTable = new NoValueErrorsTable();
    private static final IssoDataTable issoDataTable = new IssoDataTable();

    public static void main(String[] args) {
        String sheetName = "Лист 1";
        ExcelReader excelReader = new ExcelReader();
        Logger logger = LoggerFactory.getLogger(App.class);
        AbdmErrorsLog abdmErrorsLog = new AbdmErrorsLog(MyConfig.LOG_DIR_PATH);
        initDatabaseTables();


        try {
            Set<Integer> excludedTypes = Set.of(70, 90);
            Set<Integer> validIssoTypes = IssoCodesTypes.getFitleredIssoTypes(excludedTypes);
            Map<Integer, List<String>> mappingIds = excelReader.getMapOfIdsFromExcelMappingFile(MyConfig.MAPPING_FILE, sheetName);
            IssoProvider fullIssoProvider = new FullIssoProvider(mappingIds);

            List<IssoData> issoDataFromFull = fullIssoProvider.getIssoDataWithGivenTypes(validIssoTypes);
            IntersectionVerifier.setIntersectedRoadIds(issoDataFromFull, mappingIds);

            abdmErrorsLog.getNoIntersectErrors().forEach(noIntersectErrorsTable::addRow);
            abdmErrorsLog.getNoValueErrors().forEach(noValueErrorsTable::addRow);
            issoDataFromFull.forEach(issoDataTable::addRow);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void initDatabaseTables() {
        noValueErrorsTable.createEmptyNoValErrTable();
        noIntersectErrorsTable.createEmptyNoIntersectErrTable();
        issoDataTable.createEmptyIssoDataTable();
    }

}
