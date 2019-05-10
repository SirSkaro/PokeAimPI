package skaro.pokeaimpi.user;

import java.util.List;
import java.util.Optional;

public interface UserService {

	public List<UserDTO> getAll();
	public Optional<UserDTO> getByDiscordId(Long id);
	public Optional<UserDTO> getByTwitchName(String name);
	
}
