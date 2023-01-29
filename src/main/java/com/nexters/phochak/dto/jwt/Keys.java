package com.nexters.phochak.dto.jwt;

import lombok.Getter;

import java.util.List;

@Getter
public class Keys {
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
