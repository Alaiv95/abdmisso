package workers;

import pojo.IssoData;
import pojo.Road;
import services.DataBase;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IntersectionVerifier {
    public static void setIntersectedRoadIds(List<IssoData> issoData, Map<Integer, List<String>> mappingIds) {
        Map<String, Road> roads = getRoadsData(mappingIds);

        issoData.forEach(data -> {
            int location = Integer.parseInt(data.getKm().toString() + data.getM().toString());

            String roadIds = Arrays.stream(data.getAbddIds().split(";\n"))
                    .filter(id -> !id.equals("Нет замапленных дорог"))
                    .filter(id -> isLocationInRoadRange(location, id, roads))
                    .collect(Collectors.joining(";\n"));

            roadIds = roadIds.isEmpty() ? "Пересечений не найдено" : roadIds;
            data.setRoadsWithMatchingLen(roadIds);
        });
    }

    private static Map<String, Road> getRoadsData(Map<Integer, List<String>> mappingIds) {
        List<String> ids = mappingIds.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet())
                .stream()
                .map(v -> String.format("%s", v))
                .collect(Collectors.toList());

        return DataBase.getRoadData(ids);
    }

    private static boolean isLocationInRoadRange(int location, String id, Map<String, Road> roads) {
        Road road = roads.get(id);
        return location <= road.getEnd() && location >= road.getStart();
    }
}
