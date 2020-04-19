package skaro.pokeaimpi.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BadgeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 2649865683523864796L;

	public BadgeNotFoundException(Integer id) {
		super("Could not find user with id " + id);
	}
	
	public BadgeNotFoundException(String discordRoleId) {
		super("Could not find user with Discord role id " + discordRoleId);
	}
	
}
