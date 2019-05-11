package skaro.pokeaimpi.repository.entities;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class TwitchConnection {
	private String userName;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}