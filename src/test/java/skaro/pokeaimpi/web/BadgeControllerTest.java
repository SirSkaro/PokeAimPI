package skaro.pokeaimpi.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import skaro.pokeaimpi.TestUtility;
import skaro.pokeaimpi.services.BadgeService;
import skaro.pokeaimpi.web.dtos.BadgeDTO;
import skaro.pokeaimpi.web.exceptions.BadgeNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(BadgeController.class)
public class BadgeControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private BadgeService badgeService;
	
	@Before
	public void setUp() {
		Mockito.when(badgeService.getById(1)).thenReturn(Optional.of(createMockBadgeWithPrimaryKey(1)));
	}
	
	@Test
	public void getAll_shouldGetAllBadges() throws Exception {
		List<BadgeDTO> allBadges = new ArrayList<>();
		allBadges.add(new BadgeDTO());
		allBadges.add(new BadgeDTO());
		allBadges.add(new BadgeDTO());
		
		Mockito.when(badgeService.getAll()).thenReturn(allBadges);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/badge"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(allBadges.size())));
	}
	
	@Test
	public void gettingAnyBadge_shouldGetBadgeWithExpectedFields_whenBadgeExists() throws Exception {
		int badgePrimaryKey = 1;
		mockMvc.perform(MockMvcRequestBuilders.get("/badge/"+badgePrimaryKey))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id",Matchers.anything()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.pointThreshold", Matchers.anything()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.canBeEarnedWithPoints", Matchers.anything()))
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
		Mockito.when(badgeService.getById(badgePrimaryKey)).thenThrow(new BadgeNotFoundException(badgePrimaryKey));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/badge/"+badgePrimaryKey))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void getByDiscordRoleId_shouldGetBadgeWithDiscordRoleId_whenBadgeExists() throws Exception {
		long discordRoleId = 1000;
		Mockito.when(badgeService.getByDiscordRoleId(discordRoleId)).thenReturn(Optional.of(createMockBadgeWithDiscordRoleId(discordRoleId)));
		mockMvc.perform(MockMvcRequestBuilders.get("/badge/discord/"+discordRoleId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.discordRoleId", Matchers.is((int)discordRoleId)));
	}
	
	@Test
	public void getByDiscordRoleId_should404_whenBadgeDoesNotExist() throws Exception {
		long badgePrimaryKey = 2;
		Mockito.when(badgeService.getByDiscordRoleId(badgePrimaryKey)).thenThrow(new BadgeNotFoundException(badgePrimaryKey));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/badge/"+badgePrimaryKey))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void createBadge_shouldReturnNewlyCreatedBadge_whenBadgeIsValid() throws Exception {
		BadgeDTO badge = createValidBadge();
		Mockito.when(badgeService.saveBadge(ArgumentMatchers.any(BadgeDTO.class))).thenReturn(badge);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/badge")
				.content(TestUtility.convertObjectToJsonBytes(badge))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.greaterThanOrEqualTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.pointThreshold", Matchers.greaterThanOrEqualTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.discordRoleId", Matchers.greaterThanOrEqualTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.not(Matchers.isEmptyOrNullString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.not(Matchers.isEmptyOrNullString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.imageUri", Matchers.not(Matchers.isEmptyOrNullString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.canBeEarnedWithPoints", Matchers.isOneOf(true, false)));
	}
	
	@Test
	public void createBadge_should400_whenBadgePointThresholdIsNotValid() throws Exception {
		BadgeDTO badge = createValidBadge();
		Mockito.when(badgeService.saveBadge(ArgumentMatchers.any(BadgeDTO.class))).thenReturn(badge);
		
		badge.setPointThreshold(-1);
		failPostToBadgeEndpoint(badge);
	}
	
	@Test
	public void createBadge_should400_whenBadgeImageURIIsNotValid() throws Exception {
		BadgeDTO badge = createValidBadge();
		Mockito.when(badgeService.saveBadge(ArgumentMatchers.any(BadgeDTO.class))).thenReturn(badge);
		
		badge.setImageUri("");
		failPostToBadgeEndpoint(badge);
		badge.setImageUri(null);
		failPostToBadgeEndpoint(badge);
		badge.setImageUri("invalid_url");
		failPostToBadgeEndpoint(badge);
	}
	
	@Test
	public void createBadge_should400_whenBadgeTitleIsNotValid() throws Exception {
		BadgeDTO badge = createValidBadge();
		Mockito.when(badgeService.saveBadge(ArgumentMatchers.any(BadgeDTO.class))).thenReturn(badge);
		
		badge.setTitle("");
		failPostToBadgeEndpoint(badge);
		badge.setTitle(null);
		failPostToBadgeEndpoint(badge);
	}
	
	@Test
	public void createBadge_should400_whenBadgeDescriptionIsNotValid() throws Exception {
		BadgeDTO badge = createValidBadge();
		Mockito.when(badgeService.saveBadge(ArgumentMatchers.any(BadgeDTO.class))).thenReturn(badge);
		
		badge.setDescription("");
		failPostToBadgeEndpoint(badge);
		badge.setDescription(null);
		failPostToBadgeEndpoint(badge);
	}
	
	@Test
	public void updateBadge_shouldReturn200_whenBadgeIsValidAndBadgeExists() throws Exception {
		BadgeDTO badge = createValidBadge();
		
		Mockito.when(badgeService.updateBadgeByDiscordRoleId(ArgumentMatchers.any(), ArgumentMatchers.any(BadgeDTO.class)))
		.thenReturn(Optional.of(badge));
		
		mockMvc.perform(MockMvcRequestBuilders.put("/badge/discord/"+badge.getDiscordRoleId())
				.content(TestUtility.convertObjectToJsonBytes(badge))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void updateBadge_shouldReturn404_whenBadgeDoesNotExist() throws Exception {
		BadgeDTO badge = createValidBadge();
		
		Mockito.when(badgeService.updateBadgeByDiscordRoleId(ArgumentMatchers.any(), ArgumentMatchers.any(BadgeDTO.class)))
		.thenThrow(new BadgeNotFoundException(badge.getDiscordRoleId()));
		
		mockMvc.perform(MockMvcRequestBuilders.put("/badge/discord/"+badge.getDiscordRoleId())
				.content(TestUtility.convertObjectToJsonBytes(badge))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
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
	
	private BadgeDTO createValidBadge() {
		BadgeDTO result = new BadgeDTO();
		result.setDescription("this is a test badge");
		result.setDiscordRoleId(9999L);
		result.setImageUri("https://www.imgur.com/image.png");
		result.setTitle("my test badge");
		result.setId(9);
		result.setCanBeEarnedWithPoints(true);
		result.setPointThreshold(1);
		
		return result;
	}
	
	private void failPostToBadgeEndpoint(BadgeDTO badge) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/badge")
				.content(TestUtility.convertObjectToJsonBytes(badge))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
}
