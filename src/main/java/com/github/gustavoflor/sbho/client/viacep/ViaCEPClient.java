package com.github.gustavoflor.sbho.client.viacep;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "via-cep-client")
public interface ViaCEPClient {

    @GetMapping("/ws/{cep}/json")
    AddressDTO findByCEP(@PathVariable String cep);

}
