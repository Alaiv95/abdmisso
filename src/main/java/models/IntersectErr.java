package models;

import lombok.Getter;

import java.util.List;

@Getter
public class IntersectErr {
    private final String message;
    private final String roadCode;
    private final String km;
    private final String m;

    public IntersectErr(List<String> errorData) {
        if(errorData.size() != 4)
            throw new IllegalArgumentException("Not a intersect error");

        int MSG_INDEX = 0;
        int ROAD_CODE_INDEX = 1;
        int KM_INDEX = 2;
        int M_INDEX = 3;

        message = errorData.get(MSG_INDEX);
        roadCode = errorData.get(ROAD_CODE_INDEX);
        km = errorData.get(KM_INDEX);
        m = errorData.get(M_INDEX);
    }
}
