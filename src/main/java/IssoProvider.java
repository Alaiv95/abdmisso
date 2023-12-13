import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pojo.IssoData;
import services.DataBase;
import workers.ExcelWriter;
import workers.Serializer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class IssoProvider {

    protected final List<Integer> existingObjectsIsso;
    protected final Map<Integer, List<String>> idMapping;
    protected final Serializer serializer;
    private final ExcelWriter excelWriter;


    public IssoProvider(Map<Integer, List<String>> mapping) {
        existingObjectsIsso = DataBase.getAllExistingObjectsIsso();
        idMapping = mapping;
        serializer = new Serializer();
        excelWriter = new ExcelWriter(new XSSFWorkbook());
    }

    public abstract List<IssoData> getIssoDataWithGivenTypes(Set<Integer> validIssoTypes);

    public void createExcelFileBasedOnData(List<IssoData> issoData, String fileName) throws IOException {
        issoData.forEach(dataObject -> excelWriter.addRow(dataObject, dataObject.isEmpty()));
        excelWriter.saveExcelFile(fileName);
    }

    protected IssoData buildIssoData(Map<Object, Object> map) throws Exception {
        Map<?, ?> location = convertObjectToMap(map.get("location"));
        Map<?, ?> fku = convertObjectToMap(map.get("fku"));

        String km = location.get("km").toString();
        String m = location.get("m").toString();
        String fkuName = fku.get("orgName").toString();
        String dorCode = map.get("dorCode").toString();
        String roadName = map.get("dorName").toString();
        String issoCode = map.get("issoCode").toString();
        String issoTypeCode = map.get("issoTypeCode").toString();
        String abddIds = getAbddRoadIds(Integer.parseInt(dorCode));

        return IssoData.builder()
                .cIsso(Integer.parseInt(issoCode))
                .dorName(roadName)
                .km(Integer.valueOf(km))
                .m(Integer.valueOf(m))
                .orgName(fkuName)
                .dorCode(dorCode)
                .issoTypeCode(issoTypeCode)
                .abddIds(abddIds)
                .isEmpty(false)
                .build();
    }

    protected Map<?, ?> convertObjectToMap(Object object) throws Exception {
        if (object instanceof Map<?, ?>)
            return (Map<?, ?>) object;
        else
            throw new Exception("Объект не является объектом map");
    }

    protected IssoData getEmptyData(int issoCode, int roadCode, int issoType) {
        String abddIds = getAbddRoadIds(roadCode);

        return IssoData.builder()
                .cIsso(issoCode)
                .dorName("Пустое тело ответа")
                .km(0)
                .m(0)
                .orgName("")
                .dorCode(String.valueOf(roadCode))
                .abddIds(abddIds)
                .issoTypeCode(String.valueOf(issoType))
                .isEmpty(true)
                .build();
    }

    protected String getAbddRoadIds(int roadCode) {
        String abddIds = "Нет замапленных дорог";

        if (idMapping.containsKey(roadCode))
            abddIds = String.join(";\n", idMapping.get(roadCode));

        return abddIds;
    }
}
