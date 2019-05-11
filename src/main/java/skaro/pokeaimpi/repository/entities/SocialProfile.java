package skaro.pokeaimpi.repository.entities;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class SocialProfile {
	private DiscordConnection discordConnection;
	private TwitchConnection twitchConnection;
	
	public DiscordConnection getDiscordConnection() {
		return discordConnection;
	}
	public TwitchConnection getTwitchConnection() {
		return twitchConnection;
	}
	public void setDiscordConnection(DiscordConnection discordConnection) {
		this.discordConnection = discordConnection;
	}
	public void setTwitchConnection(TwitchConnection twitchConnection) {
		this.twitchConnection = twitchConnection;
	}
	
}
