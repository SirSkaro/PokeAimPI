package skaro.pokeaimpi.web.dtos;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class DiscordConnection {
	private String discordId;
	
	public DiscordConnection() {
		
	}
		
	public String getDiscordId() {
		return discordId;
	}
	
	public void setDiscordId(String discordId) {
		this.discordId = discordId;
	}
	
}
