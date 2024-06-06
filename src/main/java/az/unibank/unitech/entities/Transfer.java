package az.unibank.unitech.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "transfers")
public class Transfer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "from_account", referencedColumnName = "id")
    private Account from;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "to_account", referencedColumnName = "id")
    private Account to;

    private BigDecimal amount;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Transfer(Account from, Account to, BigDecimal amount, LocalDateTime createdAt) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.createdAt = createdAt;
    }
}
