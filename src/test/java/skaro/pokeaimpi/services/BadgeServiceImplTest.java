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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import skaro.pokeaimpi.repository.BadgeRepository;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.services.implementations.BadgeServiceImpl;
import skaro.pokeaimpi.web.dtos.BadgeDTO;

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
		BadgeDTO badgeDTO = new BadgeDTO();
		List<BadgeEntity> allBadges= Arrays.asList(new BadgeEntity(), new BadgeEntity(), new BadgeEntity());
	    Mockito.when(modelMapper.map(ArgumentMatchers.any(BadgeEntity.class), ArgumentMatchers.same(BadgeDTO.class))).thenReturn(badgeDTO);
		Mockito.when(badgeRepository.findAll()).thenReturn(allBadges);
		
		List<BadgeDTO> resultBadges = badgeService.getAll();
		
		assertEquals(allBadges.size(), resultBadges.size());
		for(BadgeDTO dto : resultBadges) {
			assertNotNull(dto);
		}
	}
	
	@Test
	public void getByDiscordRoleId_shouldGetBadgeWithDiscordRoleId_whenBadgeExists() {
		BadgeDTO badgeDTO = new BadgeDTO();
		Long roleId = 1L;
		badgeDTO.setDiscordRoleId(roleId);
		Mockito.when(modelMapper.map(ArgumentMatchers.any(BadgeEntity.class), ArgumentMatchers.same(BadgeDTO.class))).thenReturn(badgeDTO);
		Mockito.when(badgeRepository.getByDiscordRoleId(roleId)).thenReturn(Optional.of(new BadgeEntity()));
		
		Optional<BadgeDTO> badgeWithDiscordRoleId = badgeService.getByDiscordRoleId(roleId);
		assertTrue(badgeWithDiscordRoleId.isPresent());
		assertEquals(roleId, badgeWithDiscordRoleId.get().getDiscordRoleId());
	}
	
}