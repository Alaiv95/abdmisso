import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pojo.IssoData;
import pojo.ShortIsso;
import services.DataBase;
import services.IssoService;
import workers.ExcelWriter;
import workers.Serializer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IssoProvider {

    private final List<Integer> existingObjectsIsso;
    private final List<Map<Object, Object>> allShortIsso;
    private final List<Map<Object, Object>> allFullIsso;
    private final Map<Integer, List<String>> idMapping;
    private final Serializer serializer;
    private final ExcelWriter excelWriter;


    public IssoProvider(Map<Integer, List<String>> mapping) {
        allShortIsso = IssoService.getAllShortIsso();
        allFullIsso = IssoService.getAllFullIsso();
        existingObjectsIsso = DataBase.getAllExistingObjectsIsso();
        idMapping = mapping;
        serializer = new Serializer();
        excelWriter = new ExcelWriter(new XSSFWorkbook());
    }

    public List<ShortIsso> getShortIssoWithGivenTypes(Set<Integer> validIssoTypes) {
        return allShortIsso.stream()
                .map(serializer::convertMapToJson)
                .map(isso -> serializer.fromJson(isso, ShortIsso.class))
                .filter(isso -> validIssoTypes.contains(isso.getCTypisso()))
                .filter(isso -> !existingObjectsIsso.contains(isso.getCIsso()))
                .toList();
    }

    public List<IssoData> getIssoDataWithGivenTypes(Set<Integer> validIssoTypes) {
        return allFullIsso.stream()
                .filter(fullIsso -> validIssoTypes.contains((int) fullIsso.get("issoTypeCode")))
                .filter(fullIsso -> !existingObjectsIsso.contains((int) fullIsso.get("issoCode")))
                .map(fullIsso -> {
                    if (!fullIsso.isEmpty()) {
                        try {
                            return buildIssoData(fullIsso);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return getEmptyData(-1, -1, -1);
                }).toList();
    }

    public List<IssoData> convertShortIssoToIssoData(List<ShortIsso> issos) {
        return issos.stream()
                .map(shortIsso -> {
                    Map<Object, Object> map = IssoService.getFullIsso(shortIsso.getCIsso());

                    if (!map.isEmpty()) {
                        try {
                            return buildIssoData(map);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return getEmptyData(shortIsso.getCIsso(), shortIsso.getCDor(), shortIsso.getCTypisso());
                }).toList();
    }

    public void createExcelFileBasedOnData(List<IssoData> issoData, String fileName) throws IOException {
        issoData.forEach(dataObject -> excelWriter.addRow(dataObject, dataObject.isEmpty()));
        excelWriter.saveExcelFile(fileName);
    }

    private IssoData buildIssoData(Map<Object, Object> map) throws Exception {
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

    private Map<?, ?> convertObjectToMap(Object object) throws Exception {
        if (object instanceof Map<?, ?>)
            return (Map<?, ?>) object;
        else
            throw new Exception("Объект не является объектом map");
    }

    private IssoData getEmptyData(int issoCode, int roadCode, int issoType) {
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

    private String getAbddRoadIds(int roadCode) {
        String abddIds = "Нет замапленных дорог";

        if (idMapping.containsKey(roadCode))
            abddIds = String.join(";\n", idMapping.get(roadCode));

        return abddIds;
    }
}
