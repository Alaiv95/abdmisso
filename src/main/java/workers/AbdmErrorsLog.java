package workers;

import models.NoIntersectErr;
import models.NoValuesErr;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class AbdmErrorsLog {
    public static final String NO_INTERSECT_ERR = "Не найдено пересечений с дорогами АБДД";
    public static final String NO_VALUES_ERR = "Отсутствуют необходимые поля в ведомости";
    private final int ERR_PART_START_INDEX = 12;

    public AbdmErrorsLog(String path) {
        File file = new File(path);

        if (file.isFile())
            readLogTextFile(file);
        else if (file.isDirectory())
            readLogFilesFromDirectory(path);
    }

    private final Map<String, List<List<String>>> logValues = new HashMap<>() {{
        put(NO_INTERSECT_ERR, new ArrayList<>());
        put(NO_VALUES_ERR, new ArrayList<>());
    }};

    public List<NoIntersectErr> getNoIntersectErrors() {
        return logValues.get(NO_INTERSECT_ERR)
                .stream()
                .map(err -> {
                    try {
                        return new NoIntersectErr(err);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .distinct().toList();
    }

    public List<NoValuesErr> getNoValueErrors() {
        return logValues.get(NO_VALUES_ERR)
                .stream()
                .map(err -> {
                    try {
                        return new NoValuesErr(err);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .distinct().toList();
    }

    private void readLogFilesFromDirectory(String filePath) {
        try (Stream<Path> paths = Files.walk(Paths.get(filePath))) {
            paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(this::readLogTextFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readLogTextFile(File path) {
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

                    int FILTERED_ERR_MSG_INDEX = 0;
                    logValues.get(logData.get(FILTERED_ERR_MSG_INDEX)).add(logData);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAbdmErrLog(List<String> fullError) {
        int ERR_LOG_MIN_SIZE = 20;
        return fullError.size() >= ERR_LOG_MIN_SIZE && isErrorMessage(fullError.get(ERR_PART_START_INDEX));
    }

    private boolean isErrorMessage(String value) {
        return value.equals(NO_INTERSECT_ERR) || value.equals(NO_VALUES_ERR);

    }

    private boolean isDigit(String value) {
        return value.matches("\\d*");
    }
}
