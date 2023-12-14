import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import properties.IssoCodesTypes;

import java.util.Set;
import java.util.stream.Collectors;

public class TypesTest {
    @Test
    public void getTypes() {
        Set<Integer> excludedTypes = Set.of(70, 90);
        Set<Integer> validIssoTypes = IssoCodesTypes.getFitleredIssoTypes(excludedTypes);
        Assertions.assertFalse(validIssoTypes.containsAll(excludedTypes));
    }


    @Test
    public void getObjectTypes() {
        Set<Integer> bridges = Set.of(10, 20, 30, 40, 50, 65, 220);
        Set<Integer> tubes = Set.of(70, 80, 90, 100, 110, 120, 130, 150);
        Set<Integer> galery = Set.of(180);
        Set<Integer> tunnel = Set.of(140, 160);

        Set<Integer> filteredBridges = bridges.stream().filter(IssoCodesTypes::isBridge).collect(Collectors.toSet());
        Set<Integer> filteredtubes = tubes.stream().filter(IssoCodesTypes::isTube).collect(Collectors.toSet());
        Set<Integer> filteredgalery = galery.stream().filter(IssoCodesTypes::isGallery).collect(Collectors.toSet());
        Set<Integer> filteredtunnel = tunnel.stream().filter(IssoCodesTypes::isTunnel).collect(Collectors.toSet());

        Assertions.assertEquals(filteredBridges, bridges);
        Assertions.assertEquals(filteredtubes, tubes);
        Assertions.assertEquals(filteredgalery, galery);
        Assertions.assertEquals(filteredtunnel, tunnel);

    }
}
