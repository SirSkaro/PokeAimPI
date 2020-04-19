package skaro.pokeaimpi.services;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.web.dtos.BadgeDTO;

public interface BadgeService {
	List<BadgeDTO> getAll();
	Optional<BadgeDTO> getById(Integer id);
	Optional<BadgeDTO> getByDiscordRoleId(String discordRoleId);
	List<BadgeDTO> getBadgesBetween(int floor, int ceiling);
	BadgeDTO saveBadge(BadgeDTO badge);
}
