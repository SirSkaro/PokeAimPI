package skaro.pokeaimpi.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.entities.UserEntity;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class BadgeRewardedException extends RuntimeException {
	private static final long serialVersionUID = -5004206198239702128L;
	
	public BadgeRewardedException(UserEntity user, BadgeEntity badge) {
		super(String.format("Badge %s (id %d) has already been rewarded to user %d",
				badge.getTitle(), badge.getId(), user.getId()));
	}
	
}
