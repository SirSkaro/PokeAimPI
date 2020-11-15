package skaro.pokeaimpi.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
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

import skaro.pokeaimpi.repository.BadgeRepository;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.sdk.resource.Badge;
import skaro.pokeaimpi.services.implementations.BadgeServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
public class BadgeServiceImplTest {

	@TestConfiguration
    static class BadgeServiceImplTestContextConfiguration {
  
        @Bean
        public BadgeService getBadgeService() {
            return new BadgeServiceImpl();
        }
        
    }
	
	@Autowired
	private BadgeService badgeService;
	@MockBean
	private ModelMapper modelMapper;
	@MockBean
	private BadgeRepository badgeRepository;
	
	@Test
	public void getAll_shouldGetAllBadges() throws Exception {
		Badge badge = new Badge();
		List<BadgeEntity> allBadges= Arrays.asList(new BadgeEntity(), new BadgeEntity(), new BadgeEntity());
	    Mockito.when(modelMapper.map(ArgumentMatchers.any(BadgeEntity.class), ArgumentMatchers.same(Badge.class))).thenReturn(badge);
		Mockito.when(badgeRepository.findAll()).thenReturn(allBadges);
		
		List<Badge> resultBadges = badgeService.getAll();
		
		assertEquals(allBadges.size(), resultBadges.size());
		for(Badge dto : resultBadges) {
			assertNotNull(dto);
		}
	}
	
	@Test
	public void getByDiscordRoleId_shouldGetBadgeWithDiscordRoleId_whenBadgeExists() {
		Badge badge = new Badge();
		String roleId = "1";
		badge.setDiscordRoleId(roleId);
		Mockito.when(modelMapper.map(ArgumentMatchers.any(BadgeEntity.class), ArgumentMatchers.same(Badge.class))).thenReturn(badge);
		Mockito.when(badgeRepository.getByDiscordRoleId(roleId)).thenReturn(Optional.of(new BadgeEntity()));
		
		Optional<Badge> badgeWithDiscordRoleId = badgeService.getByDiscordRoleId(roleId);
		assertTrue(badgeWithDiscordRoleId.isPresent());
		assertEquals(roleId, badgeWithDiscordRoleId.get().getDiscordRoleId());
	}
	
	@Test
	public void saveBadge_shouldPersistBadge() {
		Mockito.when(modelMapper.map(ArgumentMatchers.any(BadgeEntity.class), ArgumentMatchers.same(Badge.class))).thenReturn(new Badge());
		Mockito.when(modelMapper.map(ArgumentMatchers.any(Badge.class), ArgumentMatchers.same(BadgeEntity.class))).thenReturn(new BadgeEntity());
		Mockito.when(badgeRepository.save(ArgumentMatchers.any(BadgeEntity.class))).thenReturn(new BadgeEntity());
		
		badgeService.saveBadge(new Badge());
		Mockito.verify(badgeRepository, VerificationModeFactory.times(1)).save(ArgumentMatchers.any(BadgeEntity.class));
	}
	
}
