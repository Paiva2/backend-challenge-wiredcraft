package org.com.wired.adapters.infra.persistence.repository.userFollower;

import lombok.AllArgsConstructor;
import org.com.wired.adapters.infra.mapper.UserFollowerMapper;
import org.com.wired.application.infra.persistence.entity.UserFollowerEntity;
import org.com.wired.domain.entity.UserFollower;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.usecase.userFollower.listFollowers.dto.ListUserFollowersPageDTO;
import org.com.wired.domain.usecase.userFollower.listFollowing.dto.ListFollowingDTO;
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
        Optional<UserFollowerEntity> userFollower = repository.findUserFollowing(userId, userFollowingId);
        if (userFollower.isEmpty()) return Optional.empty();
        return Optional.of(UserFollowerMapper.toDomain(userFollower.get()));
    }

    @Override
    public Optional<UserFollower> findUserFollower(Long userId, Long userFollowerId) {
        Optional<UserFollowerEntity> userFollower = repository.findUserFollower(userId, userFollowerId);
        if (userFollower.isEmpty()) return Optional.empty();
        return Optional.of(UserFollowerMapper.toDomain(userFollower.get()));
    }

    @Override
    public UserFollower persist(UserFollower userFollower) {
        UserFollowerEntity userFollowerEntity = repository.save(UserFollowerMapper.toPersistence(userFollower));
        return UserFollowerMapper.toDomain(userFollowerEntity);
    }

    @Override
    public void remove(UserFollower userFollower) {
        repository.delete(UserFollowerMapper.toPersistence(userFollower));
    }

    @Override
    public ListFollowingDTO listFollowing(Long userId, int page, int size, String followingName, String sort) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.fromString(sort), "usr.usr_name");
        Page<UserFollowerEntity> userFollowingPage = repository.findUserFollowersByFollowUserId(userId, followingName, pageable);
        Page<UserFollower> userFollowing = userFollowingPage.map(UserFollowerMapper::toDomain);

        return ListFollowingDTO.builder()
            .page(userFollowingPage.getNumber() + 1)
            .size(userFollowingPage.getSize())
            .total(userFollowingPage.getTotalElements())
            .isLast(userFollowingPage.isLast())
            .followers(userFollowing.stream().map(ListFollowingDTO::toUserFollowingDTO).toList())
            .build();
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
