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
import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;

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
	
	private List<BadgeAwardDTO> createListOfEmptyBadges() {
		List<BadgeAwardDTO> result = new ArrayList<>();
		result.add(new BadgeAwardDTO());
		result.add(new BadgeAwardDTO());
		result.add(new BadgeAwardDTO());
		
		return result;
	}
	
}
