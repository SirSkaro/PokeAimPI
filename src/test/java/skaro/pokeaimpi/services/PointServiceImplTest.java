package skaro.pokeaimpi.services;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
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
import skaro.pokeaimpi.repository.BadgeAwardRepository;
import skaro.pokeaimpi.repository.BadgeRepository;
import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.implementations.PointServiceImpl;
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
	private UserRepository userRepository;
	@MockBean
	private ModelMapper modelMapper;
	@MockBean
	private BadgeRepository badgeRepository;
	@MockBean
	private BadgeAwardRepository awardRepository;
	
	@Before
	public void setup() {
		List<BadgeEntity> badges = Arrays.asList(new BadgeEntity(), new BadgeEntity());
		Mockito.when(badgeRepository.getByCanBeEarnedWithPointsTrueAndPointThresholdBetween(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(badges);
	}
	
	@Test
	public void addPointsViaDiscordId_shouldReturnAwardWithBadges_whenBadgesAreEarnedAndUserExists() {
		Long discordId = 1L;
		int pointsToAward = 10;
		UserEntity user = EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setDiscordId, discordId)
				.with(UserEntity::setPoints, 0)
				.build();
		UserDTO userDTO = TestUtility.createEmptyUserDTO();
		userDTO.setPoints(pointsToAward);
		userDTO.getSocialProfile().getDiscordConnection().setDiscordId(discordId);
		
		Mockito.when(userRepository.getByDiscordId(discordId)).thenReturn(Optional.of(user));
		Mockito.when(userRepository.save(ArgumentMatchers.any(UserEntity.class))).thenReturn(user);
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class))).thenReturn(userDTO);
		
		NewAwardsDTO awards = pointService.addPointsViaDiscordId(discordId, pointsToAward);
		
		assertEquals(pointsToAward, awards.getUser().getPoints().intValue());
		assertEquals(2, awards.getBadges().size());
		assertEquals(discordId, awards.getUser().getSocialProfile().getDiscordConnection().getDiscordId());
	}
	
	@Test
	public void addPointsViaDiscordId_shouldReturnAwardWithBadges_whenBadgesAreEarnedAndUserDoesNotExist() {
		Long discordId = 1L;
		int pointsToAward = 10;
		UserDTO userDTO = TestUtility.createEmptyUserDTO();
		userDTO.setPoints(pointsToAward);
		userDTO.getSocialProfile().getDiscordConnection().setDiscordId(discordId);
		
		Mockito.when(userRepository.getByDiscordId(discordId)).thenReturn(Optional.empty());
		Mockito.when(userRepository.save(ArgumentMatchers.any(UserEntity.class))).then(AdditionalAnswers.returnsFirstArg());
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class))).thenReturn(userDTO);
		
		NewAwardsDTO awards = pointService.addPointsViaDiscordId(discordId, pointsToAward);
		
		assertEquals(pointsToAward, awards.getUser().getPoints().intValue());
		assertEquals(2, awards.getBadges().size());
		Mockito.verify(userRepository, VerificationModeFactory.atLeastOnce()).save(ArgumentMatchers.any(UserEntity.class));
	}
	
	@Test
	public void addPointsViaDiscordId_shouldNotOverflowUserPoints_whenUserHasMaximumPoints() {
		Long discordId = 1L;
		int pointsToAward = 10;
		UserEntity user = EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setDiscordId, discordId)
				.with(UserEntity::setPoints, Integer.MAX_VALUE)
				.build();
		
		Mockito.when(userRepository.getByDiscordId(discordId)).thenReturn(Optional.of(user));
		Mockito.when(userRepository.save(ArgumentMatchers.any(UserEntity.class))).thenReturn(user);
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class)))
			.thenAnswer(answer -> { 
				UserDTO userDTO = TestUtility.createEmptyUserDTO();
				int newAmount = ((UserEntity)answer.getArgument(0)).getPoints();
				userDTO.setPoints(newAmount);
				userDTO.getSocialProfile().getDiscordConnection().setDiscordId(discordId);
				return userDTO;
			});
		
		NewAwardsDTO awards = pointService.addPointsViaDiscordId(discordId, pointsToAward);
		
		assertEquals(Integer.MAX_VALUE, awards.getUser().getPoints().intValue());
		assertEquals(0, awards.getBadges().size());
		assertEquals(discordId, awards.getUser().getSocialProfile().getDiscordConnection().getDiscordId());
	}
	
}
