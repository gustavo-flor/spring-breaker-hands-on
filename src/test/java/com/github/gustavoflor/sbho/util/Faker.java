package com.github.gustavoflor.sbho.util;

import com.github.gustavoflor.sbho.client.brasilapi.BrasilAPIAddressDTO;
import com.github.gustavoflor.sbho.client.viacep.ViaCEPAddressDTO;

import java.util.Random;

public class Faker {

    private static final Random RANDOM = new Random();

    public String cep() {
        return numerify("######-###");
    }

    public BrasilAPIAddressDTO brasilAPIAddressDTO() {
        final var state = letterify("??");
        final var city = letterify("?????????");
        final var neighborhood = letterify("????? ? ????");
        final var street = letterify("???????? ???????");
        return new BrasilAPIAddressDTO(state, city, neighborhood, street);
    }

    public ViaCEPAddressDTO viaCEPAddressDTO() {
        final var state = letterify("??");
        final var city = letterify("?????????");
        final var neighborhood = letterify("????? ? ????");
        final var street = letterify("???????? ???????");
        return new ViaCEPAddressDTO(state, city, neighborhood, street);
    }

    public String letterify(final String letterString) {
        return letterify(letterString, false);
    }

    public String letterify(final String letterString, final boolean isUpper) {
        return letterHelper((isUpper) ? 65 : 97, letterString);
    }

    private String letterHelper(final int baseChar, final String template) {
        final var sb = new StringBuilder();
        for (int i = 0; i < template.length(); i++) {
            if (template.charAt(i) == '?') {
                sb.append((char) (baseChar + RANDOM.nextInt(26)));
            } else {
                sb.append(template.charAt(i));
            }
        }

        return sb.toString();
    }

    public String numerify(final String template) {
        final var sb = new StringBuilder();
        for (int i = 0; i < template.length(); i++) {
            if (template.charAt(i) == '#') {
                sb.append(RANDOM.nextInt(10));
            } else {
                sb.append(template.charAt(i));
            }
        }
        return sb.toString();
    }

}
