package com.nexters.phochak.repository;

import com.nexters.phochak.domain.IgnoredUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IgnoredUserRepository extends JpaRepository<IgnoredUsers, Long> {

    @Modifying
    @Query("delete from IgnoredUsers i where i.user.id = :me and i.ignoredUser.id = :ignoredUserId")
    void deleteIgnore(@Param("me")Long me, @Param("pageOwnerId")Long ignoredUserId);

    @Modifying
    @Query("select i from IgnoredUsers i left join fetch User u on i.ignoredUser.id = u.id where i.user.id = :me")
    List<IgnoredUsers> getIgnoreUserListByUserId(Long me);
}
