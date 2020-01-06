package br.com.sicredi.limites.business;

import br.com.sicredi.limites.domain.LimiteDiario;
import br.com.sicredi.limites.domain.LimiteDiarioRepository;
import br.com.sicredi.limites.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class LimiteBusiness {

    private LimiteDiarioRepository limiteDiarioRepository;
    private KafkaSender kafkaSender;

    public LimiteBusiness(final LimiteDiarioRepository limiteDiarioRepository, final KafkaSender kafkaSender) {
        this.limiteDiarioRepository = limiteDiarioRepository;
        this.kafkaSender = kafkaSender;
    }

    @Value("${limite.valorTotal}")
    private BigDecimal valorTotal;

    @NewSpan
    public void limiteDiario(final TransactionDTO transactionDTO) {
        var limiteDiario = limiteDiarioRepository.findByAgenciaAndContaAndData(
                transactionDTO.getConta().getCodigoAgencia(),
                transactionDTO.getConta().getCodigoConta(), LocalDate.now());
        if (Objects.isNull(limiteDiario)) {
            limiteDiario = new LimiteDiario();
            limiteDiario.setAgencia(transactionDTO.getConta().getCodigoAgencia());
            limiteDiario.setConta(transactionDTO.getConta().getCodigoConta());
            limiteDiario.setValor(valorTotal);
            limiteDiario.setData(LocalDate.now());
            limiteDiario = limiteDiarioRepository.save(limiteDiario);
        }

        if (limiteDiario.getValor().compareTo(transactionDTO.getValor()) < 0) {

            log.info("Transação excede valor diario.: " + transactionDTO);
            kafkaSender.send(transactionDTO);

        } else {
                limiteDiario.setValor(limiteDiario.getValor().subtract(transactionDTO.getValor()));
                limiteDiarioRepository.save(limiteDiario);
        }
    }
}
