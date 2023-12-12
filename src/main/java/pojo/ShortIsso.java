package pojo;

import lombok.Builder;
import lombok.Getter;

@Builder
public class ShortIsso {
    @Getter
    public int cIsso;
    public int god;
    @Getter
    public int cTypisso;
    public String nTypisso;
    public String descr;
    public int cDor;
}
