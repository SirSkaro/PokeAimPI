package skaro.pokeaimpi.services;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.sdk.resource.User;

public interface UserService {
	List<User> getAll();
	User createOrUpdate(User user);
	Optional<User> getByDiscordId(String id);
	Optional<User> getByTwitchName(String name);
}
