package az.unibank.unitech.dto.response;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

import static az.unibank.unitech.enums.ResponseEnum.SUCCESS;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse implements Serializable {
    private String token;
}
