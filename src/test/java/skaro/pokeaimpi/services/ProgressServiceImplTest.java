package skaro.pokeaimpi.services;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import skaro.pokeaimpi.repository.entities.EntityBuilder;
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
	
	@Test
	public void getByDiscordId_shouldGetProgressForUser_whenUserExists() {
		int threshold = 10;
		int nextBadgeThreshold = 2 * threshold;
		Long discordId = 1L;
		Long discordRoleId = 2L;
		UserEntity userEntity = EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setDiscordId, discordId)
				.with(UserEntity::setPoints,threshold)
				.build();
		UserDTO userDTO = TestUtility.createEmptyUserDTO();
		userDTO.getSocialProfile().getDiscordConnection().setDiscordId(discordId);
		userDTO.setPoints(threshold);
		List<BadgeAwardEntity> awards = new ArrayList<>();
		awards.add(new BadgeAwardEntity());
		BadgeEntity badgeEntity = new BadgeEntity();
		badgeEntity.setDiscordRoleId(discordRoleId);
		BadgeDTO nextBadgeDTO = new BadgeDTO();
		nextBadgeDTO.setDiscordRoleId(discordRoleId);
		nextBadgeDTO.setPointThreshold(nextBadgeThreshold);
		BadgeDTO currentBadgeDTO = new BadgeDTO();
		currentBadgeDTO.setPointThreshold(threshold);
		currentBadgeDTO.setDiscordRoleId(discordRoleId);
		
		Mockito.when(userRepository.getByDiscordId(discordId)).thenReturn(Optional.of(userEntity));
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class))).thenReturn(userDTO);
		Mockito.when(awardRepository.findByUserDiscordIdOrderByBadgePointThresholdDesc(discordId)).thenReturn(awards);
		Mockito.when(badgeRepository.getFirstByCanBeEarnedWithPointsTrueAndPointThresholdGreaterThanOrderByPointThreshold(threshold)).thenReturn(Optional.of(badgeEntity));
		Mockito.when(modelMapper.map(ArgumentMatchers.eq(badgeEntity), ArgumentMatchers.same(BadgeDTO.class))).thenReturn(nextBadgeDTO);
		Mockito.when(modelMapper.map(ArgumentMatchers.isNull(), ArgumentMatchers.same(BadgeDTO.class))).thenReturn(currentBadgeDTO);
		
		UserProgressDTO progress = progressService.getByDiscordId(discordId);
		
		assertEquals(threshold, progress.getCurrentPoints().intValue());
		assertEquals(nextBadgeThreshold - threshold, progress.getPointToNextReward().intValue());
		assertEquals(discordId, progress.getUser().getSocialProfile().getDiscordConnection().getDiscordId());
		assertEquals(discordRoleId, progress.getCurrentHighestBadge().getDiscordRoleId());
		assertEquals(discordRoleId, progress.getNextBadge().getDiscordRoleId());
		assertEquals(nextBadgeThreshold, progress.getNextBadge().getPointThreshold().intValue());
		assertEquals(threshold, progress.getCurrentHighestBadge().getPointThreshold().intValue());
		
	}
	
	
}
