package az.unibank.unitech.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRatesResponse implements Serializable {

    private String dateTime;

    private String currencies;

    private float rate;
}
