package com.github.gustavoflor.sbho.service;

import com.github.gustavoflor.sbho.client.brasilapi.BrasilAPIClient;
import com.github.gustavoflor.sbho.client.viacep.ViaCEPClient;
import com.github.gustavoflor.sbho.model.Address;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.github.gustavoflor.sbho.util.CircuitBreakerNames.FIND_ADDRESS_BY_CEP;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final ViaCEPClient viaCEPClient;
    private final BrasilAPIClient brasilAPIClient;

    @CircuitBreaker(name = FIND_ADDRESS_BY_CEP, fallbackMethod = "findByCEPWithBrasilAPI")
    public Optional<Address> findByCEP(final String cep) {
        try {
            return Optional.of(viaCEPClient.findByCEP(cep).address());
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

    protected Optional<Address> findByCEPWithBrasilAPI(final String cep, final CallNotPermittedException exception) {
        try {
            return Optional.of(brasilAPIClient.findByCEP(cep).address());
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

}
