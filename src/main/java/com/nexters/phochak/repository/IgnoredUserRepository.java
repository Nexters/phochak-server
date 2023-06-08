package com.nexters.phochak.repository;

import com.nexters.phochak.domain.IgnoredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IgnoredUserRepository extends JpaRepository<IgnoredUser, Long> {

    @Modifying
    @Query("delete from IgnoredUser i where i.user.id = :me and i.ignoredUser.id = :pageOwnerId")
    void deleteIgnore(@Param("me")Long me, @Param("pageOwnerId")Long pageOwnerId);

    @Modifying
    @Query("select i from IgnoredUser i left join fetch User u on i.ignoredUser.id = u.id where i.user.id = :me")
    List<IgnoredUser> getIgnoreUserListByUserId(Long me);
}
