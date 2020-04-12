package skaro.pokeaimpi.services;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;

public interface BadgeAwardService {
	
	public List<BadgeAwardDTO> getAll();
	public List<BadgeAwardDTO> getByBadgeId(Integer id);
	public List<BadgeAwardDTO> getByUserId(Integer id);
	public List<BadgeAwardDTO> getByUserDiscordId(String discordId);
	public List<BadgeAwardDTO> getByBadgeDiscordRoleId(String discordId);
	public Optional<BadgeAwardDTO> getByBadgeIdAndUserId(Integer badgeId, Integer userId);
	public Optional<BadgeAwardDTO> getByDiscordRoleIdAndUserDiscordId(String discordRoleId, String userDiscordId);
	public BadgeAwardDTO addBadgeAward(String discordUserId, String discordRoleId);
	
}
