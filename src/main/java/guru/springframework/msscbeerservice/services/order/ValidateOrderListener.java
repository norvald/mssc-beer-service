package guru.springframework.msscbeerservice.services.order;

import guru.sfg.brewery.model.events.ValidateBeerOrderRequest;
import guru.sfg.brewery.model.events.ValidateBeerOrderResponse;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import guru.springframework.msscbeerservice.config.JmsConfig;
import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateOrderListener {
    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;

    @Transactional
    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void listen(ValidateBeerOrderRequest event) {
        BeerOrderDto beerOrderDto = event.getBeerOrderDto();
        boolean isValid = true;
        for (BeerOrderLineDto orderLine : beerOrderDto.getBeerOrderLines()) {
            Optional<Beer> beer = beerRepository.findOptionalByUpc(orderLine.getUpc());
            if (beer.isEmpty()) {
                isValid = false;
            }
        }

        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE, ValidateBeerOrderResponse.builder()
                .beerOrderId(beerOrderDto.getId().toString())
                .isValid(isValid)
                .build());
    }
}
