package skaro.pokeaimpi.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.PointsService;
import skaro.pokeaimpi.web.exceptions.UserNotFoundException;

@Service
public class PointsServiceImpl implements PointsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void addPointsViaDiscordId(Long discordId, int pointAmount) {
		UserEntity user = userRepository.findBySocialProfileDiscordConnectionDiscordId(discordId)
				.orElseThrow(() -> new UserNotFoundException("Could not find user with discord id "+ discordId));
		
		user.incrementPoints(pointAmount);
		
	}

	@Override
	public void addPointsViaTwitchName(String twitchName, int pointAmount) {
		UserEntity user = userRepository.findBySocialProfileTwitchConnectionUserName(twitchName)
				.orElseThrow(() -> new UserNotFoundException("Could not find user with Twitch name "+ twitchName));
		
		user.incrementPoints(pointAmount);
		
	}

}
