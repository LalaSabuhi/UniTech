package az.unibank.unitech.dto.request;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest implements Serializable {

    @NotNull(message = "Pin required")
    @Size(min = 7, max = 7, message = "Pin must have 7 characters")
    private String pin;

    @NotNull
    @Size(min = 6, message = "Password must have minimum 6 characters")
    private String password;

    @NotNull
    @Size(min = 3, message = "Name must have minimum 3 characters")
    private String name;
}
