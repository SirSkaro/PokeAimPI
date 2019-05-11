package skaro.pokeaimpi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import skaro.pokeaimpi.repository.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	Optional<UserEntity> findBySocialProfileDiscordConnectionDiscordId(Long discordId);
	Optional<UserEntity> findBySocialProfileTwitchConnectionUserName(String userName);
}
