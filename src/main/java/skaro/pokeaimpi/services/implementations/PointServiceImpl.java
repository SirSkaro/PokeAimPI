package skaro.pokeaimpi.services.implementations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skaro.pokeaimpi.repository.BadgeAwardRepository;
import skaro.pokeaimpi.repository.BadgeRepository;
import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.BadgeAwardEntity;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.PointService;
import skaro.pokeaimpi.web.dtos.BadgeDTO;
import skaro.pokeaimpi.web.dtos.NewAwardsDTO;
import skaro.pokeaimpi.web.dtos.UserDTO;

@Service
public class PointServiceImpl implements PointService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BadgeAwardRepository awardRepository;
	@Autowired
	private BadgeRepository badgeRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public NewAwardsDTO addPointsViaDiscordId(String discordId, int pointAmount) {
		UserEntity user = userRepository.getByDiscordId(discordId)
				.orElseGet(() -> createUserWithDiscordId(discordId));
		return awardPoints(user, pointAmount);
	}

	@Override
	public NewAwardsDTO addPointsViaTwitchName(String twitchName, int pointAmount) {
		UserEntity user = userRepository.getByTwitchUserName(twitchName)
				.orElseGet(() -> createUserWithTwitchName(twitchName));
		return awardPoints(user, pointAmount);
	}
	
	private NewAwardsDTO awardPoints(UserEntity user, int pointAmount) {
		int previousPointAmount = user.getPoints();
		int newPointAmount;
		
		if(previousPointAmount == Integer.MAX_VALUE) {
			return createNewAwardsDTO(user, Arrays.asList());
		} else if (isOverflow(previousPointAmount, pointAmount)) {
			newPointAmount = Integer.MAX_VALUE;
		} else {
			newPointAmount = previousPointAmount + pointAmount;
		}
		
		user = updatePointAmount(user, newPointAmount);
		List<BadgeEntity> badgesToAward = badgeRepository.getByCanBeEarnedWithPointsTrueAndPointThresholdBetween(previousPointAmount + 1, newPointAmount);
		
		saveNewAwards(user, badgesToAward);
		NewAwardsDTO result = createNewAwardsDTO(user, badgesToAward);
		return result;
	}
	
	private UserEntity createUserWithDiscordId(String id) {
		UserEntity newUser =  EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setDiscordId, id)
				.with(UserEntity::setPoints, 0)
				.build();
		
		return userRepository.save(newUser);
	}
	
	private UserEntity createUserWithTwitchName(String name) {
		UserEntity newUser =  EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setTwitchUserName, name)
				.with(UserEntity::setPoints, 0)
				.build();
		
		return userRepository.save(newUser);
	}
	
	private boolean isOverflow(int left, int right) {
	    return right > 0
	            ? Integer.MAX_VALUE - right < left
	            : Integer.MIN_VALUE - right > left;
	}
	
	private UserEntity updatePointAmount(UserEntity user, int newAmount) {
		user.setPoints(newAmount);
		return userRepository.save(user);
	}
	
	private NewAwardsDTO createNewAwardsDTO(UserEntity user, List<BadgeEntity> badges) {
		NewAwardsDTO badgeAwardDTO = new NewAwardsDTO();
		badgeAwardDTO.setUser(modelMapper.map(user, UserDTO.class));
		
		List<BadgeDTO> badgeDTOs = badges.stream()
			.map(badgeEntity -> modelMapper.map(badgeEntity, BadgeDTO.class))
			.collect(Collectors.toList());
		badgeAwardDTO.setBadges(badgeDTOs);
		
		return badgeAwardDTO;
	}
	
	private List<BadgeAwardEntity> saveNewAwards(UserEntity user, List<BadgeEntity> badges) {
		return badges.stream()
			.map(badge -> new BadgeAwardEntity(user, badge))
			.map(award -> awardRepository.save(award))
			.collect(Collectors.toList());
	}
}
