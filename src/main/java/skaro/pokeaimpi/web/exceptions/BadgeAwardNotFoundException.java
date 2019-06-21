package skaro.pokeaimpi.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BadgeAwardNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4694582058849289877L;
	
	public BadgeAwardNotFoundException(Integer badgeId, Integer userId) {
		super("Could not find award for user " + userId + " and badge "+ badgeId);
	}

	public BadgeAwardNotFoundException(Long discordRoleId, Long userDiscordId) {
		super("Could not find award for user with Discord id" + userDiscordId + " and badge with Discord role id "+ discordRoleId);
	}

}
