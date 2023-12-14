package pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Builder
@ToString
public class IssoData {
    private String dorName;
    private String orgName;
    private String dorCode;
    private String issoTypeCode;
    private Integer km;
    private Integer m;
    private int cIsso;
    private boolean isEmpty;
    private String abddIds;
    private String length;
    @Setter
    private String roadsWithMatchingLen;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssoData issoData = (IssoData) o;
        return cIsso == issoData.cIsso && isEmpty == issoData.isEmpty && Objects.equals(dorName, issoData.dorName) &&
                Objects.equals(orgName, issoData.orgName) && Objects.equals(dorCode, issoData.dorCode) &&
                Objects.equals(issoTypeCode, issoData.issoTypeCode) && Objects.equals(km, issoData.km) &&
                Objects.equals(m, issoData.m) && Objects.equals(abddIds, issoData.abddIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dorName, orgName, dorCode, issoTypeCode, km, m, cIsso, isEmpty, abddIds);
    }
}
