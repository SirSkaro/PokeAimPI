package skaro.pokeaimpi.service;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.web.dto.UserDTO;

public interface UserService {

	public List<UserDTO> getAll();
	public Optional<UserDTO> getByDiscordId(Long id);
	public Optional<UserDTO> getByTwitchName(String name);
	
}
