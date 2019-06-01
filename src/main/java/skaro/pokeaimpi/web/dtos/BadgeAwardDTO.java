package skaro.pokeaimpi.web.dtos;

import lombok.Data;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.entities.UserEntity;

@Data
public class BadgeAwardDTO {

	private UserEntity user;
	private BadgeEntity badge;
	
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public BadgeEntity getBadge() {
		return badge;
	}
	public void setBadge(BadgeEntity badge) {
		this.badge = badge;
	}
	
}
