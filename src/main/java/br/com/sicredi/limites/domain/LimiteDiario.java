package br.com.sicredi.limites.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table
@Data
@EqualsAndHashCode(of = "id")
public class LimiteDiario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long agencia;
    private Long conta;
    private LocalDate data;
    private BigDecimal valor;
}
