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
	public UserProgressDTO getByDiscordId(Long discordId) {
		UserProgressDTO result = new UserProgressDTO();
		UserDTO user = getOrCreateUser(discordId);
		result.setUser(user);
		result.setCurrentHighestBadge(getCurrentHighestBadge(discordId));
		result.setNextBadge(getNextBadge(user));
		result.setCurrentPoints(user.getPoints());
		result.setPointToNextReward( result.getNextBadge().getPointThreshold() - user.getPoints() );
		
		return result;
	}
	
	private UserDTO getOrCreateUser(Long discordId) {
		UserEntity user = userRepository.getByDiscordId(discordId)
				.orElse(createNewUser(discordId));
		return modelMapper.map(user, UserDTO.class);
	}
	
	private UserEntity createNewUser(Long discordId) {
		UserEntity newUser = EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setDiscordId, discordId)
				.build();
		return userRepository.save(newUser);
	}

	private BadgeDTO getCurrentHighestBadge(Long discordId) {
		List<BadgeAwardEntity> awards = awardRepository.findByUserDiscordIdOrderByBadgePointThresholdDesc(discordId);
		
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
