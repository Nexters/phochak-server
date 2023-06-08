package com.nexters.phochak.repository;

import com.nexters.phochak.domain.IgnoredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IgnoredUserRepository extends JpaRepository<IgnoredUser, Long> {

    @Modifying
    @Query("delete from IgnoredUser i where i.user.id = :me and i.ignoredUser.id = :pageOwnerId")
    void deleteIgnore(@Param("me")Long me, @Param("pageOwnerId")Long pageOwnerId);
}
