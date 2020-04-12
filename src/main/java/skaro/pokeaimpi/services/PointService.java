package skaro.pokeaimpi.services;

import skaro.pokeaimpi.web.dtos.NewAwardsDTO;

public interface PointService {
	public NewAwardsDTO addPointsViaDiscordId(String discordId, int pointAmount);
	public NewAwardsDTO addPointsViaTwitchName(String twitchName, int pointAmount);
}
