package com.stepa7.webchat.repository;

import com.stepa7.webchat.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String login);

    boolean existsUserByLogin(String login);

    boolean existsUserByMail(String mail);
}
