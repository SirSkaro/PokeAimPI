package skaro.pokeaimpi.services;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;

public interface BadgeAwardService {
	List<BadgeAwardDTO> getAll();
	List<BadgeAwardDTO> getByBadgeId(Integer id);
	List<BadgeAwardDTO> getByUserId(Integer id);
	List<BadgeAwardDTO> getByUserDiscordId(String discordId);
	List<BadgeAwardDTO> getByBadgeDiscordRoleId(String discordId);
	Optional<BadgeAwardDTO> getByBadgeIdAndUserId(Integer badgeId, Integer userId);
	Optional<BadgeAwardDTO> getByDiscordRoleIdAndUserDiscordId(String discordRoleId, String userDiscordId);
	BadgeAwardDTO addBadgeAward(String discordUserId, String discordRoleId);
}
