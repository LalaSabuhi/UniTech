package az.unibank.unitech.dto.response;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.sql.Timestamp;

import static az.unibank.unitech.enums.ResponseEnum.SUCCESS;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonResponse implements Serializable {

    private Integer code;

    private String message;

    private Object data;

    private Long timestamp;

    public static CommonResponse success() {
        return CommonResponse.builder()
                .code(SUCCESS.getCode())
                .message(SUCCESS.getMessage())
                .data(null)
                .timestamp(new Timestamp(System.currentTimeMillis()).getTime())
                .build();
    }

    public static CommonResponse success(Object data) {
        return CommonResponse.builder()
                .code(SUCCESS.getCode())
                .message(SUCCESS.getMessage())
                .data(data)
                .timestamp(new Timestamp(System.currentTimeMillis()).getTime())
                .build();
    }
}
