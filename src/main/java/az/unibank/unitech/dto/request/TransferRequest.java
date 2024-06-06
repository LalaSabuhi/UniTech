package az.unibank.unitech.dto.request;

import lombok.*;
import org.springframework.validation.annotation.Validated;

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
@Validated
public class TransferRequest implements Serializable {
    @NotNull
    @Size(min = 7)
    private String fromIdentifier;
    @NotNull
    @Size(min = 7)
    private String toIdentifier;
    @NotNull
    private BigDecimal amount;
}
