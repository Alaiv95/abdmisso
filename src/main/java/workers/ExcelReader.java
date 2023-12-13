package workers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReader {
    private final Map<Integer, List<String>> idMapping = new HashMap<>();

    public Map<Integer, List<String>> getMapOfIdsFromExcelMappingFile(String file, String sheet) throws IOException {
        XSSFWorkbook hssfWorkbook = new XSSFWorkbook(new FileInputStream(file));
        XSSFSheet hssfSheet = hssfWorkbook.getSheet(sheet);
        int rowAmount = hssfSheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowAmount; i++) {
            XSSFRow row = hssfSheet.getRow(i);
            String abddId = row.getCell(0).getStringCellValue();
            Cell otherSystemRoadId = row.getCell(1);

            if (otherSystemRoadId.getCellType() == CellType.STRING) {
                String[] ids = otherSystemRoadId.getStringCellValue().split(";");
                Arrays.stream(ids).forEach(id -> addValue(abddId, Integer.parseInt(id)));
            } else {
                int abdmMapid = (int) otherSystemRoadId.getNumericCellValue();
                addValue(abddId, abdmMapid);
            }
        }
        return idMapping;
    }

    private void addValue(String abddId, int abdmMapId) {
        if (idMapping.containsKey(abdmMapId)) {
            idMapping.get(abdmMapId).add(abddId);
        } else {
            List<String> abddIds = new ArrayList<>();
            abddIds.add(abddId);
            idMapping.put(abdmMapId, abddIds);
        }
    }
}
