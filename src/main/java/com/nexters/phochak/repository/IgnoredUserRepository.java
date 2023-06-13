package com.nexters.phochak.repository;

import com.nexters.phochak.domain.IgnoredUsers;
import com.nexters.phochak.domain.IgnoredUsersRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IgnoredUserRepository extends JpaRepository<IgnoredUsers, IgnoredUsersRelation> {

    @Modifying
    @Query("delete from IgnoredUsers i " +
            "where i.ignoredUsersRelation.user.id = :me " +
            "and i.ignoredUsersRelation.ignoredUser.id = :ignoredUserId")
    void deleteIgnore(@Param("me")Long me, @Param("ignoredUserId")Long ignoredUserId);

    @Modifying
    @Query("select i from IgnoredUsers i " +
            "left join fetch User u on i.ignoredUsersRelation.ignoredUser.id = u.id " +
            "where i.ignoredUsersRelation.user.id = :me")
    List<IgnoredUsers> getIgnoreUserListByUserId(@Param("me") Long me);

    Boolean existsByIgnoredUsersRelation(IgnoredUsersRelation ignoredUsersRelation);
}
