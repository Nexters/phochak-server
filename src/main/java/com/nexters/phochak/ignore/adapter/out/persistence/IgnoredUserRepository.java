package com.nexters.phochak.ignore.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IgnoredUserRepository extends JpaRepository<IgnoredUserEntity, IgnoredUserEntityRelation> {

    @Modifying
    @Query("delete from ignored_user i " +
            "where i.ignoredUsersRelation.user.id = :me " +
            "and i.ignoredUsersRelation.ignoredUser.id = :ignoredUserId")
    void deleteIgnore(@Param("me")Long me, @Param("ignoredUserId")Long ignoredUserId);

    @Modifying
    @Query("select i from ignored_user i " +
            "left join fetch UserEntity u on i.ignoredUsersRelation.ignoredUser.id = u.id " +
            "where i.ignoredUsersRelation.user.id = :me")
    List<IgnoredUserEntity> getIgnoreUserListByUserId(@Param("me") Long me);

    Boolean existsByIgnoredUserRelation(IgnoredUserEntityRelation ignoredUsersRelation);
}
