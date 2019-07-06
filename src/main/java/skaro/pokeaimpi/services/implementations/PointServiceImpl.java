package skaro.pokeaimpi.services.implementations;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.BadgeAwardService;
import skaro.pokeaimpi.services.BadgeService;
import skaro.pokeaimpi.services.PointService;
import skaro.pokeaimpi.services.UserService;
import skaro.pokeaimpi.web.dtos.BadgeDTO;
import skaro.pokeaimpi.web.dtos.NewAwardsDTO;
import skaro.pokeaimpi.web.dtos.UserDTO;

@Service
public class PointServiceImpl implements PointService {

	@Autowired
	private UserService userService;
	@Autowired
	private BadgeAwardService awardService;
	@Autowired
	private BadgeService badgeService;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public NewAwardsDTO addPointsViaDiscordId(Long discordId, int pointAmount) {
		UserDTO user = userService.getByDiscordId(discordId)
				.orElse(createUserWithDiscordId(discordId));
		return awardPoints(user, pointAmount);
	}

	@Override
	public NewAwardsDTO addPointsViaTwitchName(String twitchName, int pointAmount) {
		UserDTO user = userService.getByTwitchName(twitchName)
				.orElse(createUserWithTwitchName(twitchName));
		return awardPoints(user, pointAmount);
	}
	
	private NewAwardsDTO awardPoints(UserDTO user, int pointAmount) {
		int previousPointAmount = user.getPoints();
		int newPointAmount = previousPointAmount + pointAmount;
		
		user = updatePointAmount(user, newPointAmount);
		List<BadgeDTO> badgesToAward = badgeService.getBadgesBetween(previousPointAmount + 1, newPointAmount);
		
		saveNewAwards(user, badgesToAward);
		NewAwardsDTO result = createNewAwardsDTO(user, badgesToAward);
		return result;
	}
	
	private UserDTO createUserWithDiscordId(Long id) {
		UserEntity newUser =  EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setDiscordId, id)
				.with(UserEntity::setPoints, 0)
				.build();
		
		return modelMapper.map(newUser, UserDTO.class);
	}
	
	private UserDTO createUserWithTwitchName(String name) {
		UserEntity newUser =  EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setTwitchUserName, name)
				.with(UserEntity::setPoints, 0)
				.build();
		
		return modelMapper.map(newUser, UserDTO.class);
	}
	
	private UserDTO updatePointAmount(UserDTO user, int newAmount) {
		user.setPoints(newAmount);
		return userService.createOrUpdate(user);
	}
	
	private NewAwardsDTO createNewAwardsDTO(UserDTO user, List<BadgeDTO> badges) {
		NewAwardsDTO badgeAwardDTO = new NewAwardsDTO();
		badgeAwardDTO.setBadges(badges);
		badgeAwardDTO.setUser(user);
		
		return badgeAwardDTO;
	}
	
	private void saveNewAwards(UserDTO user, List<BadgeDTO> badges) {
		for(BadgeDTO badge : badges) {
			awardService.addBadgeAward(user, badge);
		}
	}
}
