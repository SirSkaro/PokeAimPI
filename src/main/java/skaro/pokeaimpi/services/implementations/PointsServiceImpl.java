package skaro.pokeaimpi.services.implementations;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.BadgeService;
import skaro.pokeaimpi.services.PointsService;
import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;
import skaro.pokeaimpi.web.dtos.BadgeDTO;
import skaro.pokeaimpi.web.dtos.UserDTO;

@Service
public class PointsServiceImpl implements PointsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private BadgeService badgeService;

	@Override
	public BadgeAwardDTO addPointsViaDiscordId(Long discordId, int pointAmount) {
		UserEntity user = userRepository.findByDiscordId(discordId)
				.orElse(createEntityWithDiscord(discordId));
		int previousPointAmount = user.getPoints();
		int newPointAmount = previousPointAmount + pointAmount;
		user.incrementPointsBy(pointAmount);
		userRepository.saveAndFlush(user);
		
		List<BadgeDTO> badgesToReward = badgeService.getBadgesBetween(previousPointAmount, newPointAmount);
		BadgeAwardDTO badgeAwardDTO = new BadgeAwardDTO();
		badgeAwardDTO.setUser(modelMapper.map(user, UserDTO.class));
		badgeAwardDTO.setBadges(badgesToReward);
		
		return badgeAwardDTO;
	}

	@Override
	public BadgeAwardDTO addPointsViaTwitchName(String twitchName, int pointAmount) {
		UserEntity user = userRepository.findByTwitchUserName(twitchName)
				.orElse(new UserEntity());
		
		int previousPointAmount = user.getPoints();
		int newPointAmount = previousPointAmount + pointAmount;
		user.incrementPointsBy(pointAmount);
		userRepository.saveAndFlush(user);
		
		List<BadgeDTO> badgesToReward = badgeService.getBadgesBetween(previousPointAmount, newPointAmount);
		BadgeAwardDTO badgeAwardDTO = new BadgeAwardDTO();
		badgeAwardDTO.setUser(modelMapper.map(user, UserDTO.class));
		badgeAwardDTO.setBadges(badgesToReward);
		
		return badgeAwardDTO;
	}
	
	private UserEntity createEntityWithDiscord(Long id) {
		return EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setDiscordId, id)
				.with(UserEntity::setPoints, 0)
				.build();
	}
}
