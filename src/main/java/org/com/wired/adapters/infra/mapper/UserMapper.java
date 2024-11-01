package org.com.wired.adapters.infra.mapper;

import org.com.wired.application.infra.persistence.entity.UserEntity;
import org.com.wired.domain.entity.Address;
import org.com.wired.domain.entity.User;
import org.springframework.beans.BeanUtils;

public class UserMapper {
    public static User toDomain(UserEntity entityObject) {
        if (entityObject == null) return null;

        User user = new User();
        copyProperties(entityObject, user);

        if (entityObject.getAddress() != null) {
            Address address = new Address();
            BeanUtils.copyProperties(entityObject.getAddress(), address);
            user.setAddress(address);
        }

        return user;
    }

    public static UserEntity toPersistence(User domainObject) {
        if (domainObject == null) return null;

        UserEntity entity = new UserEntity();
        copyProperties(domainObject, entity);

        return entity;
    }

    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
