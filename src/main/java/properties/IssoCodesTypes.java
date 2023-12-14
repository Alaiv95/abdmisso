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

    public static boolean isTube(int type) {
        return type == 70 || type == 80 || type == 90 ||
                type == 100 || type == 110 || type == 120 ||
                type == 130 || type == 150;
    }

    public static boolean isBridge(int type) {
        return type == 10 || type == 20 || type == 30 ||
                type == 40 || type == 50 || type == 60 ||
                type == 65 || type == 190 || type == 220;
    }

    public static boolean isTunnel(int type) {
        return type == 140 || type == 160;
    }

    public static boolean isGallery(int type) {
        return type == 180;
    }
}
