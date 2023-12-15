package models;

import lombok.Getter;

import java.util.List;

@Getter
public class NoValuesErr {
    private final String message;
    private final String issoCode;

    public NoValuesErr(List<String> errorData) {
        if(errorData.size() != 2)
            throw new IllegalArgumentException("Not a values error");

        int MSG_INDEX = 0;
        int ISSO_CODE_INDEX = 1;

        message = errorData.get(MSG_INDEX);
        issoCode = errorData.get(ISSO_CODE_INDEX);
    }
}
