package skaro.pokeaimpi.services;

import skaro.pokeaimpi.sdk.resource.UserProgress;

public interface ProgressService {
	UserProgress getByDiscordId(String discordId);
}
