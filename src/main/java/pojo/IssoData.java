package pojo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IssoData {
    private String dorName;
    private String orgName;
    private String dorCode;
    private Integer km;
    private Integer m;
    private int cIsso;
    private boolean isEmpty;

    @Override
    public String toString() {
        return "ResultIssoData{" +
                "dorName='" + dorName + '\'' +
                ", orgName='" + orgName + '\'' +
                ", km=" + km +
                ", m=" + m +
                ", cIsso=" + cIsso +
                ", isEmpty=" + isEmpty +
                '}';
    }
}
