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

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final ViaCEPClient viaCEPClient;
    private final BrasilAPIClient brasilAPIClient;

    @CircuitBreaker(name = "find-address-by-cep-circuit-breaker", fallbackMethod = "findByCEPFallback")
    public Optional<Address> findByCEP(final String cep) {
        try {
            return Optional.of(viaCEPClient.findByCEP(cep).address());
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

    public Optional<Address> findByCEPFallback(final String cep, final CallNotPermittedException exception) {
        try {
            return Optional.of(brasilAPIClient.findByCEP(cep).address());
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

}
