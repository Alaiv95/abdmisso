package models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShortIsso {
    private int cIsso;
    private int god;
    private int cTypisso;
    private String nTypisso;
    private String descr;
    private int cDor;
}
