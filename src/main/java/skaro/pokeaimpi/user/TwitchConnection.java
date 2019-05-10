package skaro.pokeaimpi.user;

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
