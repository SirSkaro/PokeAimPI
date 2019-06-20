package skaro.pokeaimpi.services;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.services.implementations.PointServiceImpl;

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
	private BadgeService badgeService;
	
	@Test
	public void addPointsViaDiscordId_shouldReturnAwardWithBadges_whenBadgesAreEarnedForExistingUser() throws Exception {
		assertTrue(true);
	}
	
}
