import lombok.extern.slf4j.Slf4j;
import pojo.IssoData;
import pojo.ShortIsso;
import services.IssoService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ShortIssoProvider extends IssoProvider {
    private final List<Map<Object, Object>> allShortIsso;

    public ShortIssoProvider(Map<Integer, List<String>> mapping) {
        super(mapping);
        allShortIsso = IssoService.getAllShortIsso();
    }

    public List<IssoData> getIssoDataWithGivenTypes(Set<Integer> validIssoTypes) {
        List<ShortIsso> shortIssos = getShortIssoWithGivenTypes(validIssoTypes);
        return convertShortIssoToIssoData(shortIssos);
    }

    private List<ShortIsso> getShortIssoWithGivenTypes(Set<Integer> validIssoTypes) {
        return allShortIsso.stream()
                .map(serializer::convertMapToJson)
                .map(isso -> serializer.fromJson(isso, ShortIsso.class))
                .filter(isso -> validIssoTypes.contains(isso.getCTypisso()))
                .filter(isso -> !existingObjectsIsso.contains(isso.getCIsso()))
                .toList();
    }

    private List<IssoData> convertShortIssoToIssoData(List<ShortIsso> issos) {
        return issos.stream()
                .map(shortIsso -> {
                    Map<Object, Object> map = IssoService.getFullIsso(shortIsso.getCIsso());

                    if (!map.isEmpty()) {
                        try {
                            return buildIssoData(map);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }

                    return getEmptyData(shortIsso.getCIsso(), shortIsso.getCDor(), shortIsso.getCTypisso());
                }).toList();
    }
}
