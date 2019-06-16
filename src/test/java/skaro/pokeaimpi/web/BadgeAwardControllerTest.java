package skaro.pokeaimpi.web;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import skaro.pokeaimpi.services.BadgeAwardService;
import skaro.pokeaimpi.web.dtos.NewAwardsDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(BadgeAwardController.class)
public class BadgeAwardControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private BadgeAwardService awardService;
	
	@Test
	public void getAll_shouldGetAllAwards() throws Exception {
		List<NewAwardsDTO> allAwards = createListOfEmptyBadges();
		Mockito.when(awardService.getAll()).thenReturn(allAwards);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/award"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(allAwards.size())));
	}
	
	@Test
	public void getByBadgeId_shouldGetAllAwardsWithBadgeId() throws Exception {
//		List<NewAwardsDTO> allAwards = createListOfEmptyBadges();
//		int badgeId = 1;
//		allAwards.forEach(badge -> badge.get);
//		
//		Mockito.when(awardService.getByBadgeId(id)).thenReturn(allAwards);
//		
//		mockMvc.perform(MockMvcRequestBuilders.get("/award"))
//		.andDo(MockMvcResultHandlers.print())
//		.andExpect(MockMvcResultMatchers.status().isOk())
//		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//		.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
	}
	
	private List<NewAwardsDTO> createListOfEmptyBadges() {
		List<NewAwardsDTO> result = new ArrayList<>();
		result.add(new NewAwardsDTO());
		result.add(new NewAwardsDTO());
		result.add(new NewAwardsDTO());
		
		return result;
	}
	
}
