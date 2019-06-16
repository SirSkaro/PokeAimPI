package skaro.pokeaimpi.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import skaro.pokeaimpi.services.BadgeAwardService;
import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;
import skaro.pokeaimpi.web.dtos.BadgeDTO;
import skaro.pokeaimpi.web.dtos.UserDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(BadgeAwardController.class)
public class BadgeAwardControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private BadgeAwardService awardService;
	
	@Test
	public void getAll_shouldGetAllAwards() throws Exception {
		List<BadgeAwardDTO> allAwards = createListOfEmptyBadges();
		Mockito.when(awardService.getAll()).thenReturn(allAwards);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/award"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(allAwards.size())));
	}
	
	@Test
	public void gettingAnyAward_shouldGetAwardWithExpectedFields_whenBadgeExists() throws Exception {
		int badgeId = 1;
		int userId = 1;
		Mockito.when(awardService.getByBadgeIdAndUserId(badgeId, userId)).thenReturn(Optional.of(createEmptyAward()));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/award")
				.param("badgeId", Integer.toString(badgeId))
				.param("userId", Integer.toString(userId)))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.user").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.badge").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.awardDate").exists());
	}
	
	@Test
	public void getByBadgeId_shouldGetAllAwardsWithBadgeId() throws Exception {
		List<BadgeAwardDTO> allAwards = createListOfEmptyBadges();
		int badgeId = 1;
		allAwards.forEach(award -> award.setBadge(createBadgeWithId(badgeId)));
		Mockito.when(awardService.getByBadgeId(badgeId)).thenReturn(allAwards);
		
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/award")
				.param("badgeId", Integer.toString(badgeId)))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(allAwards.size())));
		
		for(int i = 0; i < allAwards.size(); i++) {
			actions.andExpect(MockMvcResultMatchers.jsonPath("$["+i+"].badge.id", Matchers.is(badgeId)));
		}
	}
	
	private List<BadgeAwardDTO> createListOfEmptyBadges() {
		List<BadgeAwardDTO> result = new ArrayList<>();
		result.add(new BadgeAwardDTO());
		result.add(new BadgeAwardDTO());
		result.add(new BadgeAwardDTO());
		
		return result;
	}
	
	private BadgeAwardDTO createEmptyAward() {
		BadgeAwardDTO result = new BadgeAwardDTO();
		result.setUser(new UserDTO());
		result.setBadge(new BadgeDTO());
		result.setAwardDate(new Date());
		
		return result;
	}
	
	private BadgeDTO createBadgeWithId(int id) {
		BadgeDTO result = new BadgeDTO();
		result.setId(id);
		return result;
	}
	
}
