package skaro.pokeaimpi.services;

import skaro.pokeaimpi.sdk.resource.NewAwardList;

public interface PointService {
	NewAwardList addPointsViaDiscordId(String discordId, int pointAmount);
	NewAwardList addPointsViaTwitchName(String twitchName, int pointAmount);
}
