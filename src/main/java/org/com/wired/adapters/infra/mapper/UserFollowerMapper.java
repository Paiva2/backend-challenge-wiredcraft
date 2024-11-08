package org.com.wired.adapters.infra.mapper;

import org.com.wired.application.infra.persistence.entity.FollowerEntity;
import org.com.wired.application.infra.persistence.entity.UserEntity;
import org.com.wired.application.infra.persistence.entity.UserFollowerEntity;
import org.com.wired.domain.entity.Follower;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.entity.UserFollower;
import org.springframework.beans.BeanUtils;

public class UserFollowerMapper {
    public static UserFollower toDomain(UserFollowerEntity entityObject) {
        if (entityObject == null) return null;

        UserFollower userFollower = new UserFollower();
        copyProperties(entityObject, userFollower);

        if (entityObject.getFollower() != null) {
            Follower follower = new Follower();
            copyProperties(entityObject.getFollower(), follower);
            userFollower.setFollower(follower);

            if (entityObject.getFollower().getUser() != null) {
                User user = new User();
                copyProperties(entityObject.getFollower().getUser(), user);
                userFollower.getFollower().setUser(user);
            }
        }

        if (entityObject.getUser() != null) {
            User user = new User();
            copyProperties(entityObject.getUser(), user);
            userFollower.setUser(user);
        }

        return userFollower;
    }

    public static UserFollowerEntity toPersistence(UserFollower domainObject) {
        if (domainObject == null) return null;

        UserFollowerEntity entity = new UserFollowerEntity();
        copyProperties(domainObject, entity);

        if (domainObject.getKeyId() != null) {
            UserFollowerEntity.KeyId keyId = new UserFollowerEntity.KeyId();
            copyProperties(domainObject.getKeyId(), keyId);
            entity.setKeyId(keyId);
        }

        if (domainObject.getFollower() != null) {
            FollowerEntity followerEntity = new FollowerEntity();
            copyProperties(domainObject.getFollower(), followerEntity);
            entity.setFollower(followerEntity);
        }

        if (domainObject.getUser() != null) {
            UserEntity userEntity = new UserEntity();
            copyProperties(domainObject.getUser(), userEntity);
            entity.setUser(userEntity);
        }

        return entity;
    }

    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
