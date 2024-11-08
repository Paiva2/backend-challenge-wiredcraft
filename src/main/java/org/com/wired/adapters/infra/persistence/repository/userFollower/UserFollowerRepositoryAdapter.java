package org.com.wired.adapters.infra.persistence.repository.userFollower;

import lombok.AllArgsConstructor;
import org.com.wired.adapters.infra.mapper.UserFollowerMapper;
import org.com.wired.application.infra.persistence.entity.UserFollowerEntity;
import org.com.wired.domain.entity.UserFollower;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.usecase.userFollower.listFollowers.dto.ListUserFollowersPageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class UserFollowerRepositoryAdapter implements UserFollowerRepositoryPort {
    private final UserFollowerRepositoryOrm repository;

    @Override
    public Optional<UserFollower> findUserFollowing(Long userId, Long userFollowingId) {
        Optional<UserFollowerEntity> userFollower = repository.findFollowing(userId, userFollowingId);
        if (userFollower.isEmpty()) return Optional.empty();
        return Optional.of(UserFollowerMapper.toDomain(userFollower.get()));
    }

    @Override
    public UserFollower persist(UserFollower userFollower) {
        UserFollowerEntity userFollowerEntity = repository.save(UserFollowerMapper.toPersistence(userFollower));
        return UserFollowerMapper.toDomain(userFollowerEntity);
    }

    @Override
    public ListUserFollowersPageDTO findUserFollowers(Long userId, int page, int perPage, String followerName, String sort) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.Direction.fromString(sort), "ufol.usr_name");
        Page<UserFollowerEntity> userFollowersPage = repository.findUserFollowers(userId, followerName, pageable);
        Page<UserFollower> userFollowers = userFollowersPage.map(UserFollowerMapper::toDomain);

        return ListUserFollowersPageDTO.builder()
            .page(userFollowersPage.getNumber() + 1)
            .size(userFollowersPage.getSize())
            .total(userFollowersPage.getTotalElements())
            .isLast(userFollowersPage.isLast())
            .followers(userFollowers.stream().map(ListUserFollowersPageDTO::toUserFollowersDto).toList())
            .build();
    }
}
