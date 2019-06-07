package skaro.pokeaimpi.services;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;

public interface BadgeAwardService {
	
	public List<BadgeAwardDTO> getAll();
	public List<BadgeAwardDTO> getByBadgeId(Integer id);
	public List<BadgeAwardDTO> getByUserId(Integer id);
	public Optional<BadgeAwardDTO> getByBadgeIdAndUserId(Integer badgeId, Integer userId);
	public BadgeAwardDTO addBadgeAwards(BadgeAwardDTO badgeAward);
	
}
