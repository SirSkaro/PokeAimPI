package skaro.pokeaimpi.services;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;

public interface BadgeAwardService {
	
	public List<BadgeAwardDTO> getAll();
	public Optional<BadgeAwardDTO> getByBadgeId(Integer id);
	public Optional<BadgeAwardDTO> getByUserId(Integer id);
	
}
