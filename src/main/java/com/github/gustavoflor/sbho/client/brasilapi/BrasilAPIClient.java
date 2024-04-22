package com.github.gustavoflor.sbho.client.brasilapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "brasil-api-client")
public interface BrasilAPIClient {

    @GetMapping("/api/cep/v1/{cep}")
    BrasilAPIAddressDTO findByCEP(@PathVariable String cep);

}
