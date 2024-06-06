package az.unibank.unitech.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddBalanceRequest implements Serializable {

    @NotEmpty(message = "Identifier must not be empty")
    @NotNull(message = "Identifier must not be null")
    private String identifier;

    @NotNull(message = "Amount must not be null")
    private BigDecimal amount;
}
