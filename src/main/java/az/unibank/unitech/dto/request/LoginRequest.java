package az.unibank.unitech.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest implements Serializable {
    @NotNull(message = "Pin required")
    private String pin;
    @NotNull(message = "Password required")
    private String password;
}
