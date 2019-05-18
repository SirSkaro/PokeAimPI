package skaro.pokeaimpi.web.exceptions;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5076166103373186644L;
	
	public UserNotFoundException(String msg) {
		super(msg);
	}

}
