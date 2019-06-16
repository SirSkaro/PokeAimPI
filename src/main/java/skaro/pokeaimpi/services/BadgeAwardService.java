package skaro.pokeaimpi.services;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.web.dtos.NewAwardsDTO;

public interface BadgeAwardService {
	
	public List<NewAwardsDTO> getAll();
	public List<NewAwardsDTO> getByBadgeId(Integer id);
	public List<NewAwardsDTO> getByUserId(Integer id);
	public Optional<NewAwardsDTO> getByBadgeIdAndUserId(Integer badgeId, Integer userId);
	public NewAwardsDTO addBadgeAwards(NewAwardsDTO badgeAward);
	public NewAwardsDTO addBadgeAward(Long discordUserId, Long discordRoleId);
	
}
