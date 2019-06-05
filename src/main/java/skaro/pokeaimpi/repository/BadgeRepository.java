package skaro.pokeaimpi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import skaro.pokeaimpi.repository.entities.BadgeEntity;

public interface BadgeRepository extends JpaRepository<BadgeEntity, Integer> {
	
	public List<BadgeEntity> getByPointThresholdBetween(int floor, int ceiling);
	
}
