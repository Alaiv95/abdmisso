package models;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class NoIntersectErr {
    private final String message;
    private final String roadCode;
    private final String km;
    private final String m;

    @Override
    public int hashCode() {
        return Objects.hash(message, roadCode, km, m);
    }

    public NoIntersectErr(List<String> errorData) throws Exception {
        if(errorData.size() != 4)
            throw new Exception("Not a intersect error");

        int MSG_INDEX = 0;
        int ROAD_CODE_INDEX = 1;
        int KM_INDEX = 2;
        int M_INDEX = 3;

        message = errorData.get(MSG_INDEX);
        roadCode = errorData.get(ROAD_CODE_INDEX);
        km = errorData.get(KM_INDEX);
        m = errorData.get(M_INDEX);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoIntersectErr that = (NoIntersectErr) o;
        return Objects.equals(message, that.message) && Objects.equals(roadCode, that.roadCode) &&
                Objects.equals(km, that.km) && Objects.equals(m, that.m);
    }
}
