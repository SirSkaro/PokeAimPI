package skaro.pokeaimpi.services;

public interface PointsService {
	public void addPointsViaDiscordId(Long discordId, int pointAmount);
	public void addPointsViaTwitchName(String twitchName, int pointAmount);
}
