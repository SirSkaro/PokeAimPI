package skaro.pokeaimpi.services.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.sdk.resource.User;
import skaro.pokeaimpi.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<User> getAll() {
		return userRepository.findAll()
				.stream()
				.map(user -> modelMapper.map(user, User.class))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<User> getByDiscordId(String id) {
		return userRepository.getByDiscordId(id)
				.map(user -> modelMapper.map(user, User.class));
	}

	@Override
	public Optional<User> getByTwitchName(String name) {
		return userRepository.getByTwitchUserName(name)
				.map(user -> modelMapper.map(user, User.class));
	}
	
	@Override
	public User createOrUpdate(User user) {
		UserEntity newUserEntity = modelMapper.map(user, UserEntity.class);
		newUserEntity = userRepository.save(newUserEntity);
		return modelMapper.map(newUserEntity, User.class);
	}
	
}
