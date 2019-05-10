package skaro.pokeaimpi.user;

import java.util.List;
import java.util.Optional;

public interface UserService {

	public List<User> getAll();
	public Optional<User> getByDiscordId(Long id);
	public Optional<User> getByTwitchName(String name);
	
}
