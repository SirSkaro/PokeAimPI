package skaro.pokeaimpi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import skaro.pokeaimpi.repository.entities.BadgeAwardEntity;

public interface BadgeAwardRepository extends JpaRepository<BadgeAwardEntity, Integer> {
	List<BadgeAwardEntity> findByUserId(long id);
	List<BadgeAwardEntity> findByBadgeId(long id);
	Optional<BadgeAwardEntity> findByBadgeIdAndUserId(long badgeId, long userId);
}
