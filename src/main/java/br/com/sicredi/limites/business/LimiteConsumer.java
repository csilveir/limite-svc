package br.com.sicredi.limites.business;

import br.com.sicredi.limites.dto.TransactionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class LimiteConsumer {

    private ObjectMapper objectMapper;
    private LimiteBusiness limiteBusiness;


    public LimiteConsumer(final ObjectMapper objectMapper, final LimiteBusiness limiteBusiness) {
        this.objectMapper = objectMapper;
        this.limiteBusiness = limiteBusiness;
    }


    @KafkaListener(topics = "${app.topic}")
    public void onConsume(final String message) {
        try {
            TransactionDTO transaction = getTransaction(message);
            limiteBusiness.limiteDiario(transaction);

        } catch (IOException ioexception) {
            log.error(ioexception.getMessage());
        }
    }

    private TransactionDTO getTransaction(String message) throws IOException {
        return objectMapper.readValue(message, TransactionDTO.class);
    }
}
