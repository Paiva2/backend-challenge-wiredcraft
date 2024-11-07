package org.com.wired.adapters.infra.mapper;

import org.com.wired.application.infra.persistence.entity.FollowerEntity;
import org.com.wired.application.infra.persistence.entity.UserEntity;
import org.com.wired.domain.entity.Follower;
import org.com.wired.domain.entity.User;
import org.springframework.beans.BeanUtils;

public class FollowerMapper {
    public static Follower toDomain(FollowerEntity entityObject) {
        if (entityObject == null) return null;

        Follower follower = new Follower();
        copyProperties(entityObject, follower);

        if (entityObject.getUser() != null) {
            User user = new User();
            copyProperties(entityObject.getUser(), user);
            follower.setUser(user);
        }

        return follower;
    }

    public static FollowerEntity toPersistence(Follower domainObject) {
        if (domainObject == null) return null;

        FollowerEntity followerEntity = new FollowerEntity();
        copyProperties(domainObject, followerEntity);

        if (domainObject.getUser() != null) {
            UserEntity userEntity = new UserEntity();
            copyProperties(domainObject.getUser(), userEntity);
            followerEntity.setUser(userEntity);
        }

        return followerEntity;
    }

    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
