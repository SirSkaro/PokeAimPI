package skaro.pokeaimpi.repository.entity;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class DiscordConnection {
	private Long discordId;
	
	public DiscordConnection() {
		
	}
		
	public Long getDiscordId() {
		return discordId;
	}
	
	public void setDiscordId(Long discordId) {
		this.discordId = discordId;
	}
	
}
