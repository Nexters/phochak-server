package com.nexters.phochak.post.application.port.out;

import java.util.List;

public interface GetHashtagAutocompletePort {
    List<String> search(String hashtag, int resultSize);
}
