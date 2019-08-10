package skaro.pokeaimpi.services;

import skaro.pokeaimpi.web.dtos.UserProgressDTO;

public interface ProgressService {
	UserProgressDTO getByDiscordId(Long discordId);
}
