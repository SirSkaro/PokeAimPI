package skaro.pokeaimpi.services.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.services.UserService;
import skaro.pokeaimpi.web.dtos.UserDTO;

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
		return userRepository.findByDiscordId(id)
				.map(user -> modelMapper.map(user, UserDTO.class));
	}

	@Override
	public Optional<UserDTO> getByTwitchName(String name) {
		return userRepository.findByTwitchUserName(name)
				.map(user -> modelMapper.map(user, UserDTO.class));
	}
	
}
