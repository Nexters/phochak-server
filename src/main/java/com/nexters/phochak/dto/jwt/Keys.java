package com.nexters.phochak.dto.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Keys {
    @JsonProperty(value = "keys")
    private List<Key> keyList;

    @Getter
    public static class Key {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;
    }
}
