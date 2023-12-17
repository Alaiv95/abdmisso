import models.NoIntersectErr;
import models.NoValuesErr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import workers.AbdmErrorsLog;

import java.util.List;


public class AbdmErrorsLogTest {
    private final String intersectLogErrPath = "src/test/resources/NoIntersectErr.log";
    private final String valueLogErrPath = "src/test/resources/NoValuesErr.log";
    private final String logDirPath = "src/test/resources/testLogs";
    private final String logDirDupesPath = "src/test/resources/testLogsWithDups";

    @Test
    public void noIntersectErrorsTestFromFile() throws Exception {
        AbdmErrorsLog abdmErrorsLog = new AbdmErrorsLog(intersectLogErrPath);
        var expectedIntersectErr = new NoIntersectErr(List.of(AbdmErrorsLog.NO_INTERSECT_ERR, "6660", "201", "233"));

        var intersectErrors = abdmErrorsLog.getNoIntersectErrors();
        var valErrors = abdmErrorsLog.getNoValueErrors();


        Assertions.assertEquals(1, intersectErrors.size());
        Assertions.assertEquals(0, valErrors.size());
        Assertions.assertEquals(expectedIntersectErr, intersectErrors.get(0));
    }

    @Test
    public void noValueErrorsTestFromFile() throws Exception {
        AbdmErrorsLog abdmErrorsLog = new AbdmErrorsLog(valueLogErrPath);
        var expectedValErr = new NoValuesErr(List.of(AbdmErrorsLog.NO_VALUES_ERR, "3300523"));

        var valErrors = abdmErrorsLog.getNoValueErrors();
        var intersectErrors = abdmErrorsLog.getNoIntersectErrors();

        Assertions.assertEquals(1, valErrors.size());
        Assertions.assertEquals(0, intersectErrors.size());
        Assertions.assertEquals(expectedValErr, valErrors.get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {logDirPath, logDirDupesPath})
    public void errorsTestFromDir(String path) throws Exception {
        AbdmErrorsLog abdmErrorsLog = new AbdmErrorsLog(path);
        var expectedValErr = new NoValuesErr(List.of(AbdmErrorsLog.NO_VALUES_ERR, "3300523"));
        var expectedIntersectErr = new NoIntersectErr(List.of(AbdmErrorsLog.NO_INTERSECT_ERR, "6660", "201", "233"));

        var valErrors = abdmErrorsLog.getNoValueErrors();
        var intersectErrors = abdmErrorsLog.getNoIntersectErrors();

        Assertions.assertEquals(1, valErrors.size());
        Assertions.assertEquals(expectedValErr, valErrors.get(0));

        Assertions.assertEquals(1, intersectErrors.size());
        Assertions.assertEquals(expectedIntersectErr, intersectErrors.get(0));
    }

}
