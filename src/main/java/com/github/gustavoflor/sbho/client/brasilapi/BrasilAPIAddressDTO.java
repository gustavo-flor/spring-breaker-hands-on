package com.github.gustavoflor.sbho.client.brasilapi;

import com.github.gustavoflor.sbho.model.Address;

public record BrasilAPIAddressDTO(String state,
                                  String city,
                                  String neighborhood,
                                  String street) {

    public Address address() {
        return new Address(state, city, neighborhood, street);
    }

}
