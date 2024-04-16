package com.github.gustavoflor.sbho.client.viacep;

import com.github.gustavoflor.sbho.model.Address;

public record AddressDTO(String uf,
                         String localidade,
                         String bairro,
                         String logradouro) {

    public Address address() {
        return new Address(uf, localidade, bairro, logradouro);
    }

}
