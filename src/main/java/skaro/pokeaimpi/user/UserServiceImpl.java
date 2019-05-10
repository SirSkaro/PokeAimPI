package skaro.pokeaimpi.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<UserDTO> getAll() {
		return userRepository.findAll()
				.stream()
				.map(user -> modelMapper.map(user, UserDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<UserDTO> getByDiscordId(Long id) {
		return userRepository
				.findAll()
				.stream()
				.filter(user -> user.getSocialProfile().getDiscordConnection().getDiscordId().equals(id))
				.map(user -> modelMapper.map(user, UserDTO.class))
				.findFirst();
	}

	@Override
	public Optional<UserDTO> getByTwitchName(String name) {
		return userRepository
				.findAll()
				.stream()
				.filter(user -> user.getSocialProfile().getTwitchConnection().getUserName().equals(name))
				.map(user -> modelMapper.map(user, UserDTO.class))
				.findFirst();
	}
	
}
