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
import skaro.pokeaimpi.services.ProgressService;
import skaro.pokeaimpi.web.dtos.BadgeDTO;
import skaro.pokeaimpi.web.dtos.UserDTO;
import skaro.pokeaimpi.web.dtos.UserProgressDTO;

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
	public UserProgressDTO getByDiscordId(String discordId) {
		UserProgressDTO result = new UserProgressDTO();
		UserDTO user = getOrCreateUser(discordId);
		result.setUser(user);
		result.setCurrentHighestBadge(getCurrentHighestBadge(discordId));
		result.setCurrentPoints(user.getPoints());
		
		BadgeDTO nextBadge = getNextBadge(user);
		result.setNextBadge(nextBadge);
		
		if(nextBadge != null)
			result.setPointsToNextReward( nextBadge.getPointThreshold() - user.getPoints() );
		else
			result.setPointsToNextReward(-1);
		
		return result;
	}
	
	private UserDTO getOrCreateUser(String discordId) {
		UserEntity user = userRepository.getByDiscordId(discordId)
				.orElseGet(() -> createNewUser(discordId));
		return modelMapper.map(user, UserDTO.class);
	}
	
	private UserEntity createNewUser(String discordId) {
		UserEntity newUser = EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setDiscordId, discordId)
				.with(UserEntity::setPoints, 0)
				.build();
		return userRepository.saveAndFlush(newUser);
	}

	private BadgeDTO getCurrentHighestBadge(String discordId) {
		List<BadgeAwardEntity> awards = awardRepository.findByUserDiscordIdSortThresholdDesc(discordId);
		
		if(awards.isEmpty()) {
			return null;
		}
		
		return modelMapper.map(awards.get(0).getBadge(), BadgeDTO.class);
	}
	
	private BadgeDTO getNextBadge(UserDTO user) {
		return badgeRepository.getFirstByCanBeEarnedWithPointsTrueAndPointThresholdGreaterThanOrderByPointThreshold(user.getPoints())
				.map(badge -> modelMapper.map(badge, BadgeDTO.class))
				.orElse(null);
	}
	
}
