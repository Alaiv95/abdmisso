import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pojo.IssoData;
import pojo.ShortIsso;
import workers.ExcelReader;
import workers.Serializer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IssoProvider {

    private final List<Integer> existingObjectsIsso;
    private final Serializer serializer;
    private final ExcelReader excelReader;
    private final List<Map<Object, Object>> allShortIsso;

    public IssoProvider() {
        allShortIsso = Service.getAllShortIsso();
        existingObjectsIsso = DataBase.getAllExistingObjectsIsso();
        serializer = new Serializer();
        Workbook workbook = new XSSFWorkbook();
        excelReader = new ExcelReader(workbook);
    }

    public List<Integer> getFilteredIssoCodes(Set<Integer> validIssoTypes) {
        return allShortIsso.stream()
                .map(serializer::convertMapToJson)
                .map(isso -> serializer.fromJson(isso, ShortIsso.class))
                .filter(isso -> validIssoTypes.contains(isso.getCTypisso()))
                .map(ShortIsso::getCIsso)
                .filter(code -> !existingObjectsIsso.contains(code))
                .toList();
    }

    public List<IssoData> convertIssoCodesToIssoData(List<Integer> issoCodes) {
        return issoCodes.stream()
                .map(issoCode -> {
                    Map<Object, Object> map = Service.getFullIsso(issoCode);
                    if (!map.isEmpty()) {
                        try {
                            return buildIssoData(issoCode, map);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else
                        return getEmptyData(issoCode);
                }).toList();
    }

    public void createExcelFileBasedOnData(List<IssoData> issoData, String fileName) throws IOException {
        issoData.forEach(dataObject -> excelReader.addRow(dataObject, dataObject.isEmpty()));
        excelReader.saveExcelFile(fileName);
    }

    private IssoData buildIssoData(int issoCode, Map<Object, Object> map) throws Exception {
        Map<?, ?> location = convertObjectToMap(map.get("location"));
        Map<?, ?> fku = convertObjectToMap(map.get("fku"));

        String km = location.get("km").toString();
        String m = location.get("m").toString();
        String fkuName = fku.get("orgName").toString();
        String dorCode = map.get("dorCode").toString();
        String roadName = map.get("dorName").toString();

        return IssoData.builder()
                .cIsso(issoCode)
                .dorName(roadName)
                .km(Integer.valueOf(km))
                .m(Integer.valueOf(m))
                .orgName(fkuName)
                .dorCode(dorCode)
                .isEmpty(false)
                .build();
    }

    private Map<?, ?> convertObjectToMap(Object object) throws Exception {
        if (object instanceof Map<?, ?>)
            return (Map<?, ?>) object;
        else
            throw new Exception("Объект не является объектом map");
    }

    private IssoData getEmptyData(int isso) {
        return IssoData.builder()
                .cIsso(isso)
                .dorName("Пустое тело ответа")
                .km(0)
                .m(0)
                .orgName("")
                .dorCode("")
                .isEmpty(true)
                .build();
    }
}
