package properties;

import java.util.Set;
import java.util.stream.Collectors;

public class IssoCodesTypes {
    private static final Set<Integer>  allIssoTypes
            = Set.of(10, 20, 30, 40, 50, 65, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 180, 220, 230);

    public static Set<Integer> getAllIssoTypes() {
        return allIssoTypes;
    }

    public static Set<Integer> getFitleredIssoTypes(Set<Integer> excludedTypes) {
        return allIssoTypes
                .stream()
                .filter(item -> !excludedTypes.contains(item))
                .collect(Collectors.toSet());
    }
}
