package skaro.pokeaimpi.services;

import static org.junit.Assert.assertNotNull;

import java.util.Optional;

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
import skaro.pokeaimpi.repository.BadgeAwardRepository;
import skaro.pokeaimpi.repository.BadgeRepository;
import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.BadgeAwardEntity;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.implementations.BadgeAwardServiceImpl;
import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;
import skaro.pokeaimpi.web.exceptions.BadgeNotAwardableException;
import skaro.pokeaimpi.web.exceptions.BadgeNotFoundException;
import skaro.pokeaimpi.web.exceptions.SocialConnectionNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
public class BadgeAwardServiceImplTest {

	@TestConfiguration
    static class BadgeAwardServiceImplTestContextConfiguration {
  
        @Bean
        public BadgeAwardService getBadgeService() {
            return new BadgeAwardServiceImpl();
        }
        
    }
	
	@Autowired
	private BadgeAwardService awardService;
	@MockBean
	private ModelMapper modelMapper;
	@MockBean
	private BadgeRepository badgeRepository;
	@MockBean
	private UserRepository userRepository;
	@MockBean
	private BadgeAwardRepository awardRepository;
	
	@Test
	public void addBadgeAward_shouldReturnBadgeAwardDTO_WhenUserAndBadgeExist() {
		Long userDiscordId = 1L;
		Long discordRoleId = 2L;
		BadgeEntity badge = TestUtility.createEmptyValidBadgeEntity();
		
		Mockito.when(userRepository.getByDiscordId(userDiscordId)).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(badgeRepository.getByDiscordRoleId(discordRoleId)).thenReturn(Optional.of(badge));
		Mockito.when(awardRepository.save(ArgumentMatchers.any(BadgeAwardEntity.class))).thenReturn(new BadgeAwardEntity());
		Mockito.when(modelMapper.map(ArgumentMatchers.any(BadgeAwardEntity.class), ArgumentMatchers.same(BadgeAwardDTO.class))).thenReturn(new BadgeAwardDTO());
		
		BadgeAwardDTO award = awardService.addBadgeAward(userDiscordId, discordRoleId);
		assertNotNull(award);
		Mockito.verify(awardRepository, VerificationModeFactory.times(1)).save(ArgumentMatchers.any(BadgeAwardEntity.class));
	}
	
	@Test(expected = SocialConnectionNotFoundException.class)
	public void addBadgeAward_shouldThrowException_WhenUserDoesNotExist() {
		Long userDiscordId = 1L;
		BadgeEntity badge = TestUtility.createEmptyValidBadgeEntity();
		Mockito.when(badgeRepository.getByDiscordRoleId(ArgumentMatchers.any())).thenReturn(Optional.of(badge));
		Mockito.when(userRepository.getByDiscordId(userDiscordId)).thenReturn(Optional.empty());
		
		awardService.addBadgeAward(userDiscordId, 0L);
	}
	
	@Test(expected = BadgeNotFoundException.class)
	public void addBadgeAward_shouldThrowException_WhenBadgeDoesNotExist() {
		Long discordRoleId = 1L;
		Mockito.when(userRepository.getByDiscordId(ArgumentMatchers.anyLong())).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(badgeRepository.getByDiscordRoleId(discordRoleId)).thenReturn(Optional.empty());
		
		awardService.addBadgeAward(0L, discordRoleId);
	}
	
	@Test(expected = BadgeNotAwardableException.class)
	public void addBadgeAward_shouldThrowException_WhenBadgeIsEarnableViaPoints() {
		BadgeEntity earnableBadge = EntityBuilder.of(BadgeEntity::new)
				.with(BadgeEntity::setCanBeEarnedWithPoints, true)
				.build();
		Mockito.when(badgeRepository.getByDiscordRoleId(ArgumentMatchers.any())).thenReturn(Optional.of(earnableBadge));
		
		awardService.addBadgeAward(0L, 0L);
	}
	
	
}
