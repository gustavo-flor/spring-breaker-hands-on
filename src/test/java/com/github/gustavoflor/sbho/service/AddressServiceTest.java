package com.github.gustavoflor.sbho.service;

import com.github.gustavoflor.sbho.Application;
import com.github.gustavoflor.sbho.client.brasilapi.BrasilAPIClient;
import com.github.gustavoflor.sbho.client.viacep.ViaCEPClient;
import com.github.gustavoflor.sbho.util.Faker;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static com.github.gustavoflor.sbho.util.CircuitBreakerNames.FIND_ADDRESS_BY_CEP;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
class AddressServiceTest {

    private static final Faker FAKER = new Faker();

    @Autowired
    private AddressService addressService;

    @SpyBean
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    private ViaCEPClient viaCEPClient;

    @MockBean
    private BrasilAPIClient brasilAPIClient;

    @Test
    @DisplayName("Given an open state circuit breaker, When call find by cep, Then should call BrasilAPI client")
    void givenAnOpenStateCircuitBreakerWhenCallFindByCEPThenShouldCallBrasilAPIClient() {
        final var cep = FAKER.cep();
        final var brasilAPIAddressDTO = FAKER.brasilAPIAddressDTO();
        circuitBreakerRegistry.circuitBreaker(FIND_ADDRESS_BY_CEP).transitionToOpenState();
        doReturn(brasilAPIAddressDTO).when(brasilAPIClient).findByCEP(cep);

        addressService.findByCEP(cep);

        final var inOrder = inOrder(viaCEPClient, brasilAPIClient);
        inOrder.verify(viaCEPClient, never()).findByCEP(cep);
        inOrder.verify(brasilAPIClient).findByCEP(cep);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Given a closed state circuit breaker, When call find by cep, Then should call ViaCEP client")
    void givenAClosedStateCircuitBreakerWhenCallFindByCEPThenShouldCallViaCEPClient() {
        final var cep = FAKER.cep();
        final var viaCEPAddressDTO = FAKER.viaCEPAddressDTO();
        circuitBreakerRegistry.circuitBreaker(FIND_ADDRESS_BY_CEP).transitionToClosedState();
        doReturn(viaCEPAddressDTO).when(viaCEPClient).findByCEP(cep);

        addressService.findByCEP(cep);

        final var inOrder = inOrder(viaCEPClient, brasilAPIClient);
        inOrder.verify(viaCEPClient).findByCEP(cep);
        inOrder.verify(brasilAPIClient, never()).findByCEP(cep);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Given a closed state circuit breaker and a feign exception, When call find by cep, Then should call throw exception")
    void givenAClosedStateCircuitBreakerAndAFeignExceptionWhenCallFindByCEPThenShouldThrowException() {
        final var cep = FAKER.cep();
        circuitBreakerRegistry.circuitBreaker(FIND_ADDRESS_BY_CEP).transitionToClosedState();
        doThrow(FeignException.InternalServerError.class).when(viaCEPClient).findByCEP(cep);

        assertThatThrownBy(() -> addressService.findByCEP(cep)).isInstanceOf(FeignException.InternalServerError.class);

        final var inOrder = inOrder(viaCEPClient, brasilAPIClient);
        inOrder.verify(viaCEPClient).findByCEP(cep);
        inOrder.verify(brasilAPIClient, never()).findByCEP(cep);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Given a half open state circuit breaker, When call find by cep, Then should call ViaCEP client")
    void givenAHalfOpenStateCircuitBreakerWhenCallFindByCEPThenShouldCallViaCEPClient() {
        final var cep = FAKER.cep();
        final var viaCEPAddressDTO = FAKER.viaCEPAddressDTO();
        circuitBreakerRegistry.circuitBreaker(FIND_ADDRESS_BY_CEP).transitionToOpenState();
        circuitBreakerRegistry.circuitBreaker(FIND_ADDRESS_BY_CEP).transitionToHalfOpenState();
        doReturn(viaCEPAddressDTO).when(viaCEPClient).findByCEP(cep);

        addressService.findByCEP(cep);

        final var inOrder = inOrder(viaCEPClient, brasilAPIClient);
        inOrder.verify(viaCEPClient).findByCEP(cep);
        inOrder.verify(brasilAPIClient, never()).findByCEP(cep);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Given a half open state circuit breaker and a feign exception, When call find by cep, Then should call throw exception")
    void givenAnHalfOpenStateCircuitBreakerAndAFeignExceptionWhenCallFindByCEPThenShouldThrowException() {
        final var cep = FAKER.cep();
        circuitBreakerRegistry.circuitBreaker(FIND_ADDRESS_BY_CEP).transitionToOpenState();
        circuitBreakerRegistry.circuitBreaker(FIND_ADDRESS_BY_CEP).transitionToHalfOpenState();
        doThrow(FeignException.InternalServerError.class).when(viaCEPClient).findByCEP(cep);

        assertThatThrownBy(() -> addressService.findByCEP(cep)).isInstanceOf(FeignException.InternalServerError.class);

        final var inOrder = inOrder(viaCEPClient, brasilAPIClient);
        inOrder.verify(viaCEPClient).findByCEP(cep);
        inOrder.verify(brasilAPIClient, never()).findByCEP(cep);
        inOrder.verifyNoMoreInteractions();
    }

}
