package skaro.pokeaimpi.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
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
import skaro.pokeaimpi.repository.entities.BadgeAwardEntity;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.implementations.ProgressServiceImpl;
import skaro.pokeaimpi.web.dtos.BadgeDTO;
import skaro.pokeaimpi.web.dtos.UserDTO;
import skaro.pokeaimpi.web.dtos.UserProgressDTO;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProgressServiceImplTest {

	@TestConfiguration
    static class PointServiceImplTestContextConfiguration {
  
        @Bean
        public ProgressService getProgressService() {
            return new ProgressServiceImpl();
        }
        
    }
	
	@Autowired
	private ProgressService progressService;
	@MockBean
	private UserRepository userRepository;
	@MockBean
	private BadgeRepository badgeRepository;
	@MockBean
	private BadgeAwardRepository awardRepository;
	@MockBean
	private ModelMapper modelMapper;
	
	private int threshold;
	private int nextBadgeThreshold;
	private Long discordId;
	private Long discordRoleId;
	private UserDTO userDTO;
	
	@Before
	public void setup() {
		threshold = 10;
		nextBadgeThreshold = 2 * threshold;
		discordId = 1L;
		discordRoleId = 2L;
		userDTO = TestUtility.createEmptyUserDTO();
		userDTO.getSocialProfile().getDiscordConnection().setDiscordId(discordId);
		
		
		BadgeDTO nextBadgeDTO = new BadgeDTO();
		nextBadgeDTO.setDiscordRoleId(discordRoleId);
		nextBadgeDTO.setPointThreshold(nextBadgeThreshold);
		BadgeEntity badgeEntity = new BadgeEntity();
		
		Mockito.when(badgeRepository.getFirstByCanBeEarnedWithPointsTrueAndPointThresholdGreaterThanOrderByPointThreshold(ArgumentMatchers.anyInt())).thenReturn(Optional.of(badgeEntity));
		Mockito.when(modelMapper.map(ArgumentMatchers.same(badgeEntity), ArgumentMatchers.same(BadgeDTO.class))).thenReturn(nextBadgeDTO);
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class))).thenReturn(userDTO);
	}
	
	@Test
	public void getByDiscordId_shouldGetProgressForUser_whenUserExists() {
		BadgeEntity awardedBadge = new BadgeEntity();
		BadgeAwardEntity awardEntity = new BadgeAwardEntity();
		awardEntity.setBadge(awardedBadge);
		List<BadgeAwardEntity> awards = new ArrayList<>();
		awards.add(awardEntity);
		BadgeDTO currentBadgeDTO = new BadgeDTO();
		currentBadgeDTO.setPointThreshold(threshold);
		currentBadgeDTO.setDiscordRoleId(discordRoleId);
		userDTO.setPoints(threshold);
		
		Mockito.when(userRepository.getByDiscordId(discordId)).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(awardRepository.findByUserDiscordIdSortThresholdDesc(discordId)).thenReturn(awards);
		Mockito.when(modelMapper.map(ArgumentMatchers.same(awardedBadge), ArgumentMatchers.same(BadgeDTO.class))).thenReturn(currentBadgeDTO);
		
		UserProgressDTO progress = progressService.getByDiscordId(discordId);
		
		assertEquals(threshold, progress.getCurrentPoints().intValue());
		assertEquals(nextBadgeThreshold - userDTO.getPoints(), progress.getPointsToNextReward().intValue());
		assertEquals(discordId, progress.getUser().getSocialProfile().getDiscordConnection().getDiscordId());
		assertEquals(discordRoleId, progress.getCurrentHighestBadge().getDiscordRoleId());
		assertEquals(discordRoleId, progress.getNextBadge().getDiscordRoleId());
		assertEquals(nextBadgeThreshold, progress.getNextBadge().getPointThreshold().intValue());
		assertEquals(threshold, progress.getCurrentHighestBadge().getPointThreshold().intValue());
	}
	
	@Test
	public void getByDiscordId_shouldGetProgressForNewUser_whenUserDoesNotExists() {
		Mockito.when(userRepository.getByDiscordId(discordId)).thenReturn(Optional.empty());
		Mockito.when(userRepository.saveAndFlush(ArgumentMatchers.any(UserEntity.class))).thenReturn(new UserEntity());
		Mockito.when(awardRepository.findByUserDiscordIdSortThresholdDesc(discordId)).thenReturn(new ArrayList<BadgeAwardEntity>());
		
		userDTO.setPoints(0);
		
		UserProgressDTO progress = progressService.getByDiscordId(discordId);
		
		assertEquals(0, progress.getCurrentPoints().intValue());
		assertEquals(nextBadgeThreshold, progress.getPointsToNextReward().intValue());
		assertEquals(discordId, progress.getUser().getSocialProfile().getDiscordConnection().getDiscordId());
		assertNull(progress.getCurrentHighestBadge());
		assertEquals(discordRoleId, progress.getNextBadge().getDiscordRoleId());
		assertEquals(nextBadgeThreshold, progress.getNextBadge().getPointThreshold().intValue());
	}
	
	@Test
	public void getByDiscordId_shouldGetProgressWithNoNextBadge_whenNoHigherBadgesAreAvailable() {
		Mockito.when(modelMapper.map(ArgumentMatchers.any(), ArgumentMatchers.same(BadgeDTO.class))).thenReturn(null);
		Mockito.when(userRepository.getByDiscordId(discordId)).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(awardRepository.findByUserDiscordIdSortThresholdDesc(discordId)).thenReturn(new ArrayList<BadgeAwardEntity>());
		
		UserProgressDTO progress = progressService.getByDiscordId(discordId);
		
		assertEquals(-1, progress.getPointsToNextReward().intValue());
		assertNull(progress.getNextBadge());
	}
	
}
