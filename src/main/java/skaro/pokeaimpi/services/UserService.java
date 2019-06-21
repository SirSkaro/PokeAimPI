package skaro.pokeaimpi.services;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.web.dtos.UserDTO;

public interface UserService {

	public List<UserDTO> getAll();
	public UserDTO createOrUpdate(UserDTO user);
	public Optional<UserDTO> getByDiscordId(Long id);
	public Optional<UserDTO> getByTwitchName(String name);
	
}
