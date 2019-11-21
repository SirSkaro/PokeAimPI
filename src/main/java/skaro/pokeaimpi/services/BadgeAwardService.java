package skaro.pokeaimpi.services;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;

public interface BadgeAwardService {
	
	public List<BadgeAwardDTO> getAll();
	public List<BadgeAwardDTO> getByBadgeId(Integer id);
	public List<BadgeAwardDTO> getByUserId(Integer id);
	public List<BadgeAwardDTO> getByUserDiscordId(Long discordId);
	public List<BadgeAwardDTO> getByBadgeDiscordRoleId(Long discordId);
	public Optional<BadgeAwardDTO> getByBadgeIdAndUserId(Integer badgeId, Integer userId);
	public Optional<BadgeAwardDTO> getByDiscordRoleIdAndUserDiscordId(Long discordRoleId, Long userDiscordId);
	public BadgeAwardDTO addBadgeAward(Long discordUserId, Long discordRoleId);
	
}
