package skaro.pokeaimpi.services;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.web.dtos.UserDTO;

public interface UserService {
	List<UserDTO> getAll();
	UserDTO createOrUpdate(UserDTO user);
	Optional<UserDTO> getByDiscordId(String id);
	Optional<UserDTO> getByTwitchName(String name);
}
