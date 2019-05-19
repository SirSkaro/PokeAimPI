package skaro.pokeaimpi.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SocialConnectionNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = -147459727451904180L;

	public SocialConnectionNotFoundException(Long id) {
		super("Could not find user's social connection with id "+ id);
	}
	
	public SocialConnectionNotFoundException(String userName) {
		super("Could not find user's social connection with username "+ userName);
	}
}
