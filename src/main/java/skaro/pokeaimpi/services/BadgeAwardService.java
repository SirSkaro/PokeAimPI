package skaro.pokeaimpi.services;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.sdk.resource.BadgeAwardRecord;

public interface BadgeAwardService {
	List<BadgeAwardRecord> getAll();
	List<BadgeAwardRecord> getByBadgeId(Integer id);
	List<BadgeAwardRecord> getByUserId(Integer id);
	List<BadgeAwardRecord> getByUserDiscordId(String discordId);
	List<BadgeAwardRecord> getByBadgeDiscordRoleId(String discordId);
	Optional<BadgeAwardRecord> getByBadgeIdAndUserId(Integer badgeId, Integer userId);
	Optional<BadgeAwardRecord> getByDiscordRoleIdAndUserDiscordId(String discordRoleId, String userDiscordId);
	BadgeAwardRecord addBadgeAward(String discordUserId, String discordRoleId);
}
