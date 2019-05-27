package skaro.pokeaimpi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import skaro.pokeaimpi.repository.entities.BadgeEntity;

public interface BadgeRepository extends JpaRepository<BadgeEntity, Integer> {
	
}
