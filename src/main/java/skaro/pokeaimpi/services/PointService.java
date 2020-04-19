package skaro.pokeaimpi.services;

import skaro.pokeaimpi.web.dtos.NewAwardsDTO;

public interface PointService {
	NewAwardsDTO addPointsViaDiscordId(String discordId, int pointAmount);
	NewAwardsDTO addPointsViaTwitchName(String twitchName, int pointAmount);
}
