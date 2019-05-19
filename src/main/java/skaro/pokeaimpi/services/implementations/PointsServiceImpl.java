package skaro.pokeaimpi.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.DiscordConnection;
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.SocialProfile;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.PointsService;

@Service
public class PointsServiceImpl implements PointsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void addPointsViaDiscordId(Long discordId, int pointAmount) {
		UserEntity user = userRepository.findBySocialProfileDiscordConnectionDiscordId(discordId)
				.orElse(createEntityWithDiscord(discordId));
		
		user.incrementPointsBy(pointAmount);
		userRepository.saveAndFlush(user);
	}

	@Override
	public void addPointsViaTwitchName(String twitchName, int pointAmount) {
		UserEntity user = userRepository.findBySocialProfileTwitchConnectionUserName(twitchName)
				.orElse(new UserEntity());
		
		user.incrementPointsBy(pointAmount);
		userRepository.saveAndFlush(user);
	}
	
	private UserEntity createEntityWithDiscord(Long id) {
		SocialProfile profile = new SocialProfile();
		DiscordConnection discordConnection = new DiscordConnection();
		discordConnection.setDiscordId(id);
		profile.setDiscordConnection(discordConnection);
		
		return EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setSocialProfile, profile)
				.with(UserEntity::setPoints, 0)
				.build();
	}
}
