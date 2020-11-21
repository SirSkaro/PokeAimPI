package skaro.pokeaimpi.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import skaro.pokeaimpi.TestUtility;
import skaro.pokeaimpi.repository.BadgeAwardRepository;
import skaro.pokeaimpi.repository.BadgeRepository;
import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.BadgeAwardEntity;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.sdk.resource.BadgeAwardRecord;
import skaro.pokeaimpi.services.implementations.BadgeAwardServiceImpl;
import skaro.pokeaimpi.web.exceptions.BadgeNotAwardableException;
import skaro.pokeaimpi.web.exceptions.BadgeNotFoundException;
import skaro.pokeaimpi.web.exceptions.BadgeRewardedException;
import skaro.pokeaimpi.web.exceptions.SocialConnectionNotFoundException;

@ExtendWith(SpringExtension.class)
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
		String userDiscordId = "1";
		String discordRoleId = "2";
		BadgeEntity badge = TestUtility.createEmptyValidBadgeEntity();
		
		Mockito.when(userRepository.getByDiscordId(userDiscordId)).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(badgeRepository.getByDiscordRoleId(discordRoleId)).thenReturn(Optional.of(badge));
		Mockito.when(awardRepository.save(ArgumentMatchers.any(BadgeAwardEntity.class))).thenReturn(new BadgeAwardEntity());
		Mockito.when(modelMapper.map(ArgumentMatchers.any(BadgeAwardEntity.class), ArgumentMatchers.same(BadgeAwardRecord.class))).thenReturn(new BadgeAwardRecord());
		
		BadgeAwardRecord award = awardService.addBadgeAward(userDiscordId, discordRoleId);
		assertNotNull(award);
		Mockito.verify(awardRepository, VerificationModeFactory.times(1)).save(ArgumentMatchers.any(BadgeAwardEntity.class));
	}
	
	@Test
	public void addBadgeAward_shouldThrowException_WhenUserDoesNotExist() {
		String userDiscordId = "1";
		BadgeEntity badge = TestUtility.createEmptyValidBadgeEntity();
		Mockito.when(badgeRepository.getByDiscordRoleId(ArgumentMatchers.any())).thenReturn(Optional.of(badge));
		Mockito.when(userRepository.getByDiscordId(userDiscordId)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(SocialConnectionNotFoundException.class, () -> awardService.addBadgeAward(userDiscordId, "0"));
	}
	
	@Test
	public void addBadgeAward_shouldThrowException_WhenBadgeDoesNotExist() {
		String discordRoleId = "1";
		Mockito.when(userRepository.getByDiscordId(ArgumentMatchers.anyString())).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(badgeRepository.getByDiscordRoleId(discordRoleId)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(BadgeNotFoundException.class, () -> awardService.addBadgeAward("0", discordRoleId));
	}
	
	@Test
	public void addBadgeAward_shouldThrowException_WhenBadgeIsEarnableViaPoints() {
		BadgeEntity earnableBadge = EntityBuilder.of(BadgeEntity::new)
				.with(BadgeEntity::setCanBeEarnedWithPoints, true)
				.build();
		Mockito.when(badgeRepository.getByDiscordRoleId(ArgumentMatchers.any())).thenReturn(Optional.of(earnableBadge));
		
		Assertions.assertThrows(BadgeNotAwardableException.class, () -> awardService.addBadgeAward("0", "0"));
	}
	
	@Test
	public void addBadgeAward_shouldThrowException_WhenUserAlreadyHasBadge() {
		String userDiscordId = "1";
		String discordRoleId = "2";
		BadgeEntity badge = TestUtility.createEmptyValidBadgeEntity();
		Mockito.when(badgeRepository.getByDiscordRoleId(ArgumentMatchers.any())).thenReturn(Optional.of(badge));
		Mockito.when(userRepository.getByDiscordId(userDiscordId)).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(awardRepository.findByBadgeDiscordRoleIdAndUserDiscordId(discordRoleId, userDiscordId))
			.thenReturn(Optional.of(new BadgeAwardEntity()));
		
		Assertions.assertThrows(BadgeRewardedException.class, () -> awardService.addBadgeAward(userDiscordId, discordRoleId));
	}
	
}
