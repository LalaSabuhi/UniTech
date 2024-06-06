package az.unibank.unitech.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRateRequest implements Serializable {
    @NotEmpty
    @NotNull
    private String from;

    @NotEmpty
    @NotNull
    private String to;
}
