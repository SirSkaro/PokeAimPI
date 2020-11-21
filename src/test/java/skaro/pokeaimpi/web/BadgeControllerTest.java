package skaro.pokeaimpi.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import skaro.pokeaimpi.TestUtility;
import skaro.pokeaimpi.sdk.resource.Badge;
import skaro.pokeaimpi.services.BadgeService;
import skaro.pokeaimpi.web.exceptions.BadgeNotFoundException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BadgeController.class)
public class BadgeControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private BadgeService badgeService;
	
	@Test
	public void getAll_shouldGetAllBadges() throws Exception {
		List<Badge> allBadges = new ArrayList<>();
		allBadges.add(new Badge());
		allBadges.add(new Badge());
		allBadges.add(new Badge());
		
		Mockito.when(badgeService.getAll()).thenReturn(allBadges);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/badge"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(allBadges.size())));
	}
	
	@Test
	public void gettingAnyBadge_shouldGetBadgeWithExpectedFields_whenBadgeExists() throws Exception {
		Badge badge = createValidBadge();
		Mockito.when(badgeService.getById(badge.getId())).thenReturn(Optional.of(badge));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/badge/"+badge.getId()))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.pointThreshold").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.canBeEarnedWithPoints").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.imageUri").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.title").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.discordRoleId").exists());
	}
	
	@Test
	public void getById_shouldGetBadgeWithSpecifiedPrimaryKey_whenBadgeExists() throws Exception {
		Badge badge = createValidBadge();
		Mockito.when(badgeService.getById(badge.getId())).thenReturn(Optional.of(badge));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/badge/"+badge.getId()))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(badge.getId())));
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
		String discordRoleId = "1000";
		Mockito.when(badgeService.getByDiscordRoleId(discordRoleId)).thenReturn(Optional.of(createMockBadgeWithDiscordRoleId(discordRoleId)));
		mockMvc.perform(MockMvcRequestBuilders.get("/badge/discord/"+discordRoleId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.discordRoleId", Matchers.is(discordRoleId)));
	}
	
	@Test
	public void getByDiscordRoleId_should404_whenBadgeDoesNotExist() throws Exception {
		String discordRoleId = "2";
		Mockito.when(badgeService.getByDiscordRoleId(discordRoleId)).thenThrow(new BadgeNotFoundException(discordRoleId));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/badge/"+discordRoleId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void createBadge_shouldReturnNewlyCreatedBadge_whenBadgeIsValid() throws Exception {
		Badge badge = createValidBadge();
		Mockito.when(badgeService.saveBadge(ArgumentMatchers.any(Badge.class))).thenReturn(badge);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/badge")
				.content(TestUtility.convertObjectToJsonBytes(badge))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.greaterThanOrEqualTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.pointThreshold", Matchers.greaterThanOrEqualTo(0)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.discordRoleId", Matchers.not(Matchers.emptyOrNullString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.not(Matchers.emptyOrNullString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.not(Matchers.emptyOrNullString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.imageUri", Matchers.not(Matchers.emptyOrNullString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.canBeEarnedWithPoints", Matchers.oneOf(true, false)));
	}
	
	@Test
	public void createBadge_should400_whenBadgePointThresholdIsNotValid() throws Exception {
		Badge badge = createValidBadge();
		Mockito.when(badgeService.saveBadge(ArgumentMatchers.any(Badge.class))).thenReturn(badge);
		
		badge.setPointThreshold(-1);
		failPostToBadgeEndpoint(badge);
	}
	
	@Test
	public void createBadge_should400_whenBadgeImageURIIsNotValid() throws Exception {
		Badge badge = createValidBadge();
		Mockito.when(badgeService.saveBadge(ArgumentMatchers.any(Badge.class))).thenReturn(badge);
		
		badge.setImageUri("");
		failPostToBadgeEndpoint(badge);
		badge.setImageUri(null);
		failPostToBadgeEndpoint(badge);
		badge.setImageUri("invalid_url");
		failPostToBadgeEndpoint(badge);
	}
	
	@Test
	public void createBadge_should400_whenBadgeTitleIsNotValid() throws Exception {
		Badge badge = createValidBadge();
		Mockito.when(badgeService.saveBadge(ArgumentMatchers.any(Badge.class))).thenReturn(badge);
		
		badge.setTitle("");
		failPostToBadgeEndpoint(badge);
		badge.setTitle(null);
		failPostToBadgeEndpoint(badge);
	}
	
	@Test
	public void createBadge_should400_whenBadgeDescriptionIsNotValid() throws Exception {
		Badge badge = createValidBadge();
		Mockito.when(badgeService.saveBadge(ArgumentMatchers.any(Badge.class))).thenReturn(badge);
		
		badge.setDescription("");
		failPostToBadgeEndpoint(badge);
		badge.setDescription(null);
		failPostToBadgeEndpoint(badge);
	}
	
	private Badge createMockBadgeWithDiscordRoleId(String discordRoleId) {
		Badge result = new Badge();
		result.setDiscordRoleId(discordRoleId);
		return result;
	}
	
	private Badge createValidBadge() {
		Badge result = new Badge();
		result.setDescription("this is a test badge");
		result.setDiscordRoleId("9999");
		result.setImageUri("https://www.imgur.com/image.png");
		result.setTitle("my test badge");
		result.setId(9);
		result.setCanBeEarnedWithPoints(true);
		result.setPointThreshold(1);
		
		return result;
	}
	
	private void failPostToBadgeEndpoint(Badge badge) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/badge")
				.content(TestUtility.convertObjectToJsonBytes(badge))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
}
