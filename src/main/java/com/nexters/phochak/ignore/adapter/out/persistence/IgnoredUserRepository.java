package com.nexters.phochak.ignore.adapter.out.persistence;

import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IgnoredUserRepository extends JpaRepository<IgnoredUserEntity, IgnoredUserEntityRelation> {

    @Modifying
    @Query("delete from ignored_user i " +
            "where i.ignoredUserRelation.user.id = :me " +
            "and i.ignoredUserRelation.ignoredUser.id = :ignoredUserId")
    void deleteIgnore(@Param("me")Long me, @Param("ignoredUserId")Long ignoredUserId);

    @Modifying
    @Query("select i from ignored_user i " +
            "left join fetch UserEntity u on i.ignoredUserRelation.ignoredUser.id = u.id " +
            "where i.ignoredUserRelation.user = :me")
    List<IgnoredUserEntity> getIgnoreUserListByUserId(@Param("me") UserEntity me);

    boolean existsByIgnoredUserRelation(IgnoredUserEntityRelation ignoredUserRelation);
}
