package skaro.pokeaimpi.services;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import skaro.pokeaimpi.TestUtility;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.implementations.PointServiceImpl;
import skaro.pokeaimpi.web.dtos.BadgeDTO;
import skaro.pokeaimpi.web.dtos.NewAwardsDTO;
import skaro.pokeaimpi.web.dtos.UserDTO;

@RunWith(SpringJUnit4ClassRunner.class)
public class PointServiceImplTest {

	@TestConfiguration
    static class PointServiceImplTestContextConfiguration {
  
        @Bean
        public PointService getPointService() {
            return new PointServiceImpl();
        }
        
    }
	
	@Autowired
	private PointService pointService;
	@MockBean
	private UserService userService;
	@MockBean
	private ModelMapper modelMapper;
	@MockBean
	private BadgeService badgeService;
	@MockBean
	private BadgeAwardService awardService;
	
	@Before
	public void setup() {
		List<BadgeDTO> badges = Arrays.asList(new BadgeDTO(), new BadgeDTO());
		Mockito.when(badgeService.getBadgesBetween(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(badges);
	}
	
	@Test
	public void addPointsViaDiscordId_shouldReturnAwardWithBadges_whenBadgesAreEarnedAndUserExists() {
		UserDTO user = TestUtility.createEmptyUserDTO();
		Long discordId = 1L;
		int pointsToAward = 10;
		user.getSocialProfile().getDiscordConnection().setDiscordId(discordId);
		
		Mockito.when(userService.getByDiscordId(discordId)).thenReturn(Optional.of(user));
		Mockito.when(userService.createOrUpdate(user)).thenReturn(user);
		
		NewAwardsDTO awards = pointService.addPointsViaDiscordId(discordId, pointsToAward);
		
		assertEquals(pointsToAward, awards.getUser().getPoints().intValue());
		assertEquals(2, awards.getBadges().size());
		assertEquals(discordId, user.getSocialProfile().getDiscordConnection().getDiscordId());
		Mockito.verify(userService, VerificationModeFactory.times(1)).createOrUpdate(ArgumentMatchers.any(UserDTO.class));
		Mockito.verify(awardService, VerificationModeFactory.atLeastOnce()).addBadgeAward(ArgumentMatchers.any(UserDTO.class), ArgumentMatchers.any(BadgeDTO.class));
	}
	
	@Test
	public void addPointsViaDiscordId_shouldReturnAwardWithBadges_whenBadgesAreEarnedAndUserDoesNotExist() {
		UserDTO user = TestUtility.createEmptyUserDTO();
		Long discordId = 1L;
		int pointsToAward = 10;
		user.getSocialProfile().getDiscordConnection().setDiscordId(discordId);
		
		Mockito.when(userService.getByDiscordId(discordId)).thenReturn(Optional.empty());
		Mockito.when(userService.createOrUpdate(user)).thenReturn(user);
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class))).thenReturn(user);
		
		NewAwardsDTO awards = pointService.addPointsViaDiscordId(discordId, pointsToAward);
		
		assertEquals(pointsToAward, awards.getUser().getPoints().intValue());
		assertEquals(2, awards.getBadges().size());
		assertEquals(discordId, user.getSocialProfile().getDiscordConnection().getDiscordId());
		Mockito.verify(userService, VerificationModeFactory.times(1)).createOrUpdate(ArgumentMatchers.any(UserDTO.class));
		Mockito.verify(awardService, VerificationModeFactory.atLeastOnce()).addBadgeAward(ArgumentMatchers.any(UserDTO.class), ArgumentMatchers.any(BadgeDTO.class));
	}
	
}
