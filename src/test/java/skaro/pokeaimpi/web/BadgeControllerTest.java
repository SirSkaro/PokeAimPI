package skaro.pokeaimpi.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Before;
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

import skaro.pokeaimpi.services.BadgeService;
import skaro.pokeaimpi.web.dtos.BadgeDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(BadgeController.class)
public class BadgeControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private BadgeService badgeService;
	
	@Before
	public void setUp() {
		List<BadgeDTO> allBadges = new ArrayList<>();
		allBadges.add(new BadgeDTO());
		allBadges.add(new BadgeDTO());
		allBadges.add(new BadgeDTO());
		
		Mockito.when(badgeService.getAll()).thenReturn(allBadges);
		Mockito.when(badgeService.getById(1)).thenReturn(Optional.of(createMockBadgeWithPrimaryKey(1)));
		Mockito.when(badgeService.getById(2)).thenReturn(Optional.empty());
		Mockito.when(badgeService.getByDiscordRoleId(1000L)).thenReturn(Optional.of(createMockBadgeWithDiscordRoleId(1000L)));
	}
	
	@Test
	public void getAll_shouldGetAllBadges() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/badge"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
	}
	
	@Test
	public void gettingAnyBadge_shouldGetBadgeWithExpectedFields_whenBadgeExists() throws Exception {
		int badgePrimaryKey = 1;
		mockMvc.perform(MockMvcRequestBuilders.get("/badge/"+badgePrimaryKey))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id",Matchers.anything()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.pointThreshold", Matchers.anything()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.imageUri", Matchers.anything()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.anything()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.anything()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.discordRoleId", Matchers.anything()));
	}
	
	@Test
	public void getById_shouldGetBadgeWithSpecifiedPrimaryKey_whenBadgeExists() throws Exception {
		int badgePrimaryKey = 1;
		mockMvc.perform(MockMvcRequestBuilders.get("/badge/"+badgePrimaryKey))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(badgePrimaryKey)));
	}
	
	@Test
	public void getById_should404_whenBadgeDoesNotExist() throws Exception {
		int badgePrimaryKey = 2;
		mockMvc.perform(MockMvcRequestBuilders.get("/badge/"+badgePrimaryKey))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void getByDiscordRoleId_shouldGetBadgeWithDiscordRoleId_whenBadgeExists() throws Exception {
		int discordRoleId = 1000;
		mockMvc.perform(MockMvcRequestBuilders.get("/badge/discord/"+discordRoleId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.discordRoleId", Matchers.is(discordRoleId)));
	}
	
	private BadgeDTO createMockBadgeWithPrimaryKey(int primaryKey) {
		BadgeDTO result = new BadgeDTO();
		result.setId(primaryKey);
		return result;
	}
	
	private BadgeDTO createMockBadgeWithDiscordRoleId(Long discordRoleId) {
		BadgeDTO result = new BadgeDTO();
		result.setDiscordRoleId(discordRoleId);
		return result;
	}
	
}
