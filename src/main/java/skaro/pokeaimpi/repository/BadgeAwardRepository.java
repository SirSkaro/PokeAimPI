package skaro.pokeaimpi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import skaro.pokeaimpi.repository.entities.BadgeAwardEntity;

public interface BadgeAwardRepository extends JpaRepository<BadgeAwardEntity, Integer> {
	List<BadgeAwardEntity> findByUserId(Integer id);
	List<BadgeAwardEntity> findByBadgeId(Integer id);
	Optional<BadgeAwardEntity> findByBadgeIdAndUserId(Integer badgeId, Integer userId);
}
