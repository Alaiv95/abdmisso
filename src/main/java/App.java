import pojo.IssoData;

import java.util.List;
import java.util.Set;

public class App {
    public static void main(String[] args) throws Exception {
        Set<Integer> validIssoTypes = Set.of(10, 20, 30, 40, 50, 65, 80, 100, 110, 120, 130, 140, 150, 160, 180, 220, 230);
        IssoProvider issoProvider = new IssoProvider();

        List<Integer> filteredShortIssoCodes = issoProvider.getFilteredIssoCodes(validIssoTypes).subList(0, 5);
        List<IssoData> issoData = issoProvider.convertIssoCodesToIssoData(filteredShortIssoCodes);
        issoProvider.createExcelFileBasedOnData(issoData, "issoDataResult");
    }
}
