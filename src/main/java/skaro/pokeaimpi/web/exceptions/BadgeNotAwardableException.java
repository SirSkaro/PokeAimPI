package skaro.pokeaimpi.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import skaro.pokeaimpi.repository.entities.BadgeEntity;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class BadgeNotAwardableException extends RuntimeException {

	private static final long serialVersionUID = 3936039688609011732L;
	
	public BadgeNotAwardableException(BadgeEntity badge) {
		super(String.format("The badge %s (id %d) can only be earned via points)", 
				badge.getTitle(), badge.getId()));
	}

}
