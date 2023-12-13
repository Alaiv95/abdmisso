import lombok.extern.slf4j.Slf4j;
import pojo.IssoData;
import services.IssoService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class FullIssoProvider extends IssoProvider {
    private final List<Map<Object, Object>> allFullIsso;

    public FullIssoProvider(Map<Integer, List<String>> mapping) {
        super(mapping);
        allFullIsso = IssoService.getAllFullIsso();
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
                            log.error(e.getMessage());
                        }
                    }

                    return getEmptyData(-1, -1, -1);
                }).toList();
    }
}
