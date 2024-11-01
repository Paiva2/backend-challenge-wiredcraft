package org.com.wired.adapters.infra.mapper;

import org.com.wired.application.infra.persistence.entity.AddressEntity;
import org.com.wired.application.infra.persistence.entity.UserEntity;
import org.com.wired.domain.entity.Address;
import org.springframework.beans.BeanUtils;

public class AddressMapperAdapter {
    public static Address toDomain(AddressEntity entityObject) {
        if (entityObject == null) return null;

        Address address = new Address();
        copyProperties(entityObject, address);

        return address;
    }

    public static AddressEntity toPersistence(Address domainObject) {
        if (domainObject == null) return null;

        AddressEntity entity = new AddressEntity();
        copyProperties(domainObject, entity);

        if (domainObject.getUser() != null) {
            UserEntity user = new UserEntity();
            copyProperties(domainObject.getUser(), user);
            entity.setUser(user);
        }

        return entity;
    }

    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
