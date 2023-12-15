package workers;

import java.io.*;
import java.util.*;

public class AbdmLogReader {
    public static final String NO_INTERSECT_ERR = "Не найдено пересечений с дорогами АБДД";
    public static final String NO_VALUES_ERR = "Отсутствуют необходимые поля в ведомости";
    private final int ERR_PART_START_INDEX = 12;
    private final int ERR_LOG_MIN_SIZE = 20;
    private final int FILTERED_ERR_MSG_INDEX = 0;

    private final Map<String, List<List<String>>> logValues = new HashMap<>(){{
        put(NO_INTERSECT_ERR, new ArrayList<>());
        put(NO_VALUES_ERR, new ArrayList<>());
    }};

    public List<List<String>> getSpecificErrorsFromLog(String message) {
        return logValues.get(message);
    }

    public AbdmLogReader readLogTextFile(String path) {
        try (var reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> fullError = Arrays.stream(line.split("[.:|,]"))
                        .map(String::trim)
                        .filter(v -> !v.isEmpty())
                        .toList();

                if (isAbdmErrLog(fullError)) {
                    List<String> logData = fullError.subList(ERR_PART_START_INDEX, fullError.size())
                            .stream()
                            .filter(value -> isDigit(value) || isErrorMessage(value))
                            .toList();

                    logValues.get(logData.get(FILTERED_ERR_MSG_INDEX)).add(logData);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    private boolean isAbdmErrLog(List<String> fullError) {
        return fullError.size() >= ERR_LOG_MIN_SIZE && isErrorMessage(fullError.get(ERR_PART_START_INDEX));
    }

    private boolean isErrorMessage(String value) {
        return value.equals(NO_INTERSECT_ERR) || value.equals(NO_VALUES_ERR);

    }

    private boolean isDigit(String value) {
        return value.matches("\\d*");
    }
}
