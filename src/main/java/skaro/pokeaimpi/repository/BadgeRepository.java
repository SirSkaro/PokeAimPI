package skaro.pokeaimpi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import skaro.pokeaimpi.repository.entities.BadgeEntity;

public interface BadgeRepository extends JpaRepository<BadgeEntity, Integer> {
	
	public List<BadgeEntity> getByCanBeEarnedWithPointsTrueAndPointThresholdBetween(int floor, int ceiling);
	public Optional<BadgeEntity> getByDiscordRoleId(Long discordRoleId);
	
}
