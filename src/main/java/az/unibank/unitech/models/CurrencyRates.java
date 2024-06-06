package az.unibank.unitech.models;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRates implements Serializable {

    private LocalDateTime dateTime;

    private Map<String, Float> rates;
}
