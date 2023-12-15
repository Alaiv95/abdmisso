import models.NoIntersectErr;
import models.NoValuesErr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import workers.AbdmLogReader;

import java.util.List;


public class AbdmLogReaderTest {
    private final String intersectLogErrPath = "src/main/resources/NoIntersectErr.log";
    private final String valueLogErrPath = "src/main/resources/NoValuesErr.log";

    @Test
    public void noIntersectErrorsTest() throws Exception {
        AbdmLogReader abdmLogReader = new AbdmLogReader(intersectLogErrPath);
        var expectedIntersectErr = new NoIntersectErr(List.of(AbdmLogReader.NO_INTERSECT_ERR, "6660", "201", "233"));

        var intersectErrors = abdmLogReader.getNoIntersectErrors();
        var valErrors = abdmLogReader.getNoValueErrors();


        Assertions.assertEquals(1, intersectErrors.size());
        Assertions.assertEquals(0, valErrors.size());
        Assertions.assertEquals(expectedIntersectErr, intersectErrors.get(0));
    }

    @Test
    public void noValueErrorsTest() throws Exception {
        AbdmLogReader abdmLogReader = new AbdmLogReader(valueLogErrPath);
        var expectedValErr = new NoValuesErr(List.of(AbdmLogReader.NO_VALUES_ERR, "3300523"));

        var valErrors = abdmLogReader.getNoValueErrors();
        var intersectErrors = abdmLogReader.getNoIntersectErrors();

        Assertions.assertEquals(1, valErrors.size());
        Assertions.assertEquals(0, intersectErrors.size());
        Assertions.assertEquals(expectedValErr, valErrors.get(0));
    }
}
