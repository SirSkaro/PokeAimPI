package skaro.pokeaimpi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import skaro.pokeaimpi.repository.entities.BadgeAwardEntity;

public interface BadgeAwardRepository extends JpaRepository<BadgeAwardEntity, Integer> {
	List<BadgeAwardEntity> findByUserId(Integer id);
	List<BadgeAwardEntity> findByBadgeId(Integer id);
	List<BadgeAwardEntity> findByBadgeDiscordRoleId(Long discordRoleId);
	Optional<BadgeAwardEntity> findByBadgeIdAndUserId(Integer badgeId, Integer userId);
	Optional<BadgeAwardEntity> findByBadgeDiscordRoleIdAndUserDiscordId(Long discordRoleId, Long discordId);
	
	@Query("SELECT award FROM BadgeAwardEntity award WHERE award.user.discordId = :discordId ORDER BY award.badge.pointThreshold DESC")
	List<BadgeAwardEntity> findByUserDiscordIdOrderByBadgePointThresholdDesc(@Param("discordId") Long discordId);
}
