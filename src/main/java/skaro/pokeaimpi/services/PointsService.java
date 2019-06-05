package skaro.pokeaimpi.services;

import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;

public interface PointsService {
	public BadgeAwardDTO addPointsViaDiscordId(Long discordId, int pointAmount);
	public BadgeAwardDTO addPointsViaTwitchName(String twitchName, int pointAmount);
}
