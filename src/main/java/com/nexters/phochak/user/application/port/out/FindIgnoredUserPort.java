package com.nexters.phochak.user.application.port.out;

import com.nexters.phochak.user.domain.User;

public interface FindIgnoredUserPort {
    boolean checkIgnoredRelation(User user, User pageOwner);
}
