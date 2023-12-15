import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workers.ExcelReader;
import workers.AbdmLogReader;

public class App {
    public static void main(String[] args) {
        String sheetName = "Лист 1";
        ExcelReader excelReader = new ExcelReader();
        Logger logger = LoggerFactory.getLogger(App.class);
        AbdmLogReader abdmLogReader = new AbdmLogReader();
        System.out.println(abdmLogReader
                .readLogTextFile("src/main/resources/abdd-app03-app-log-2023-12-11.log")
                .getSpecificErrorsFromLog(AbdmLogReader.NO_INTERSECT_ERR)
        );

        int workerTypeIndex = 6;


//        try {
//            DataBaseWriter.dropIssoTable();
//            DataBaseWriter.createIssoDataTable();
//
//            Set<Integer> excludedTypes = Set.of(70, 90);
//            Set<Integer> validIssoTypes = IssoCodesTypes.getFitleredIssoTypes(excludedTypes);
//            Map<Integer, List<String>> mappingIds = excelReader.getMapOfIdsFromExcelMappingFile(MyConfig.MAPPING_FILE, sheetName);
//            IssoProvider fullIssoProvider = new FullIssoProvider(mappingIds);
//
//            List<IssoData> issoDataFromFull = fullIssoProvider.getIssoDataWithGivenTypes(validIssoTypes);
//            IntersectionVerifier.setIntersectedRoadIds(issoDataFromFull, mappingIds);
//
//            issoDataFromFull.forEach(DataBaseWriter::addRowToIssoDataTable);
//
//           fullIssoProvider.createExcelFileBasedOnData(issoDataFromFull, "issoDataResultFull");
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
    }

}
