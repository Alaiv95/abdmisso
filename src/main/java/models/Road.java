package models;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Road {
    String roadId;
    int start;
    int end;
}
