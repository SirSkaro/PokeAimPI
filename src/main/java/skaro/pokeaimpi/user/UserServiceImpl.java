package skaro.pokeaimpi.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> getByDiscordId(Long id) {
		return userRepository
				.findAll()
				.stream()
				.filter(user -> user.getSocialProfile().getDiscordConnection().getDiscordId().equals(id))
				.findFirst();
	}

	@Override
	public Optional<User> getByTwitchName(String name) {
		return userRepository
				.findAll()
				.stream()
				.filter(user -> user.getSocialProfile().getTwitchConnection().getUserName().equals(name))
				.findFirst();
	}
	
}
