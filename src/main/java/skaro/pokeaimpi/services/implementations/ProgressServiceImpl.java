package skaro.pokeaimpi.services.implementations;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skaro.pokeaimpi.repository.BadgeAwardRepository;
import skaro.pokeaimpi.repository.BadgeRepository;
import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.BadgeAwardEntity;
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.sdk.resource.Badge;
import skaro.pokeaimpi.sdk.resource.User;
import skaro.pokeaimpi.sdk.resource.UserProgress;
import skaro.pokeaimpi.services.ProgressService;

@Service
public class ProgressServiceImpl implements ProgressService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BadgeRepository badgeRepository;
	@Autowired
	private BadgeAwardRepository awardRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public UserProgress getByDiscordId(String discordId) {
		UserProgress result = new UserProgress();
		User user = getOrCreateUser(discordId);
		result.setUser(user);
		result.setCurrentHighestBadge(getCurrentHighestBadge(discordId));
		result.setCurrentPoints(user.getPoints());
		
		Badge nextBadge = getNextBadge(user);
		result.setNextBadge(nextBadge);
		
		if(nextBadge != null)
			result.setPointsToNextReward( nextBadge.getPointThreshold() - user.getPoints() );
		else
			result.setPointsToNextReward(-1);
		
		return result;
	}
	
	private User getOrCreateUser(String discordId) {
		UserEntity user = userRepository.getByDiscordId(discordId)
				.orElseGet(() -> createNewUser(discordId));
		return modelMapper.map(user, User.class);
	}
	
	private UserEntity createNewUser(String discordId) {
		UserEntity newUser = EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setDiscordId, discordId)
				.with(UserEntity::setPoints, 0)
				.build();
		return userRepository.saveAndFlush(newUser);
	}

	private Badge getCurrentHighestBadge(String discordId) {
		List<BadgeAwardEntity> awards = awardRepository.findByUserDiscordIdSortThresholdDesc(discordId);
		
		if(awards.isEmpty()) {
			return null;
		}
		
		return modelMapper.map(awards.get(0).getBadge(), Badge.class);
	}
	
	private Badge getNextBadge(User user) {
		return badgeRepository.getFirstByCanBeEarnedWithPointsTrueAndPointThresholdGreaterThanOrderByPointThreshold(user.getPoints())
				.map(badge -> modelMapper.map(badge, Badge.class))
				.orElse(null);
	}
	
}
