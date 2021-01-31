package skaro.pokeaimpi.services;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.sdk.resource.Badge;

public interface BadgeService {
	List<Badge> getAll();
	Optional<Badge> getById(Integer id);
	Optional<Badge> getByDiscordRoleId(String discordRoleId);
	List<Badge> getBadgesBetween(int floor, int ceiling);
	Badge saveBadge(Badge badge);
	void deleteBadge(Integer id);
}
