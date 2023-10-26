package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT) // JSON형태로 변환할 떄 값이 기본값이 아닌 변수만 Json 형태로 변환하는 어노테이션
public class MsgDTO {

    private int result; // 성공 : 1 / 실패 : 그 외
    private String msg; // 메시지
}
