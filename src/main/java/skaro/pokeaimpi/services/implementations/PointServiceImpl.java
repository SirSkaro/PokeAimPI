package skaro.pokeaimpi.services.implementations;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.BadgeService;
import skaro.pokeaimpi.services.PointService;
import skaro.pokeaimpi.web.dtos.NewAwardsDTO;
import skaro.pokeaimpi.web.dtos.BadgeDTO;
import skaro.pokeaimpi.web.dtos.UserDTO;

@Service
public class PointServiceImpl implements PointService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private BadgeService badgeService;

	@Override
	public NewAwardsDTO addPointsViaDiscordId(Long discordId, int pointAmount) {
		UserEntity user = userRepository.findByDiscordId(discordId)
				.orElse(createEntityWithDiscord(discordId));
		return awardPoints(user, pointAmount);
	}

	@Override
	public NewAwardsDTO addPointsViaTwitchName(String twitchName, int pointAmount) {
		UserEntity user = userRepository.findByTwitchUserName(twitchName)
				.orElse(new UserEntity());
		return awardPoints(user, pointAmount);
	}
	
	private NewAwardsDTO awardPoints(UserEntity user, int pointAmount) {
		int previousPointAmount = user.getPoints();
		int newPointAmount = previousPointAmount + pointAmount;
		
		updatePointAmount(user, pointAmount);
		return getBadgesToAward(user, previousPointAmount, newPointAmount);
	}
	
	private UserEntity createEntityWithDiscord(Long id) {
		return EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setDiscordId, id)
				.with(UserEntity::setPoints, 0)
				.build();
	}
	
	private UserEntity updatePointAmount(UserEntity user, int pointsToAdd) {
		user.incrementPointsBy(pointsToAdd);
		return userRepository.saveAndFlush(user);
	}
	
	private NewAwardsDTO getBadgesToAward(UserEntity user, int previousPointAmount, int newPointAmount) {
		NewAwardsDTO badgesToAward = getAwardsInRange(previousPointAmount, newPointAmount);
		badgesToAward.setUser(modelMapper.map(user, UserDTO.class));
		
		return badgesToAward;
	}
	
	private NewAwardsDTO getAwardsInRange(int previousPointAmount, int newPointAmount) {
		List<BadgeDTO> badgesToReward = badgeService.getBadgesBetween(previousPointAmount, newPointAmount);
		NewAwardsDTO badgeAwardDTO = new NewAwardsDTO();
		badgeAwardDTO.setBadges(badgesToReward);
		
		return badgeAwardDTO;
	}
}
