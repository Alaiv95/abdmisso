package models;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class NoValuesErr {
    private final String message;
    private final String issoCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoValuesErr that = (NoValuesErr) o;
        return Objects.equals(message, that.message) && Objects.equals(issoCode, that.issoCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, issoCode);
    }

    public NoValuesErr(List<String> errorData) throws Exception {
        if(errorData.size() != 2)
            throw new Exception("Not a values error");

        int MSG_INDEX = 0;
        int ISSO_CODE_INDEX = 1;

        message = errorData.get(MSG_INDEX);
        issoCode = errorData.get(ISSO_CODE_INDEX);
    }
}
