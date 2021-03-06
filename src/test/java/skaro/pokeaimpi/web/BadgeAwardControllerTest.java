package skaro.pokeaimpi.web;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import skaro.pokeaimpi.TestUtility;
import skaro.pokeaimpi.sdk.resource.Badge;
import skaro.pokeaimpi.sdk.resource.BadgeAwardRecord;
import skaro.pokeaimpi.sdk.resource.User;
import skaro.pokeaimpi.services.BadgeAwardService;
import skaro.pokeaimpi.web.exceptions.BadgeNotFoundException;
import skaro.pokeaimpi.web.exceptions.SocialConnectionNotFoundException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BadgeAwardController.class)
public class BadgeAwardControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private BadgeAwardService awardService;
	
	@Test
	public void getAll_shouldGetAllAwards() throws Exception {
		List<BadgeAwardRecord> allAwards = createListOfEmptyBadges();
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
		List<BadgeAwardRecord> allAwards = createListOfEmptyBadges();
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
	
	@Test
	public void getByUserId_shouldGetAllAwardsForTheUser() throws Exception {
		List<BadgeAwardRecord> allAwards = createListOfEmptyBadges();
		int userId = 1;
		allAwards.forEach(award -> award.setUser(createUserWithId(userId)));
		Mockito.when(awardService.getByUserId(userId)).thenReturn(allAwards);
		
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/award")
				.param("userId", Integer.toString(userId)))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(allAwards.size())));
		
		for(int i = 0; i < allAwards.size(); i++) {
			actions.andExpect(MockMvcResultMatchers.jsonPath("$["+i+"].user.id", Matchers.is(userId)));
		}
	}
	
	@Test
	public void awardBadge_shouldReturnNewAward_WhenUserAndBadgeExist() throws Exception {
		BadgeAwardRecord newAward = createEmptyAward();
		String userDiscordId = "1";
		String discordRoleId = "2";
		newAward.getBadge().setDiscordRoleId(discordRoleId);
		newAward.setUser(createUserWithId(0));
		newAward.getUser().getSocialProfile().getDiscordConnection().setDiscordId(userDiscordId);
		Mockito.when(awardService.addBadgeAward(userDiscordId, discordRoleId)).thenReturn(newAward);
		
		String awardUrl = "/award/discord/user/" + userDiscordId + "/role/" + discordRoleId;
		mockMvc.perform(MockMvcRequestBuilders.post(awardUrl))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.user.socialProfile.discordConnection.discordId", Matchers.is(userDiscordId)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.badge.discordRoleId", Matchers.is(discordRoleId)));
	}
	
	@Test
	public void awardBadge_should4XX_WhenUserDoesNotExist() throws Exception {
		String discordId = "1";
		Mockito.when(awardService.addBadgeAward(ArgumentMatchers.eq(discordId), ArgumentMatchers.anyString())).thenThrow(new SocialConnectionNotFoundException(discordId));
		
		String awardUrl = "/award/discord/user/" + discordId + "/role/0";
		mockMvc.perform(MockMvcRequestBuilders.post(awardUrl))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
	
	@Test
	public void awardBadge_should4XX_WhenBadgeDoesNotExist() throws Exception {
		String roleId = "1";
		Mockito.when(awardService.addBadgeAward(ArgumentMatchers.anyString(), ArgumentMatchers.eq(roleId))).thenThrow(new BadgeNotFoundException(roleId));
		
		String awardUrl = "/award/discord/user/0/role/"+ roleId;
		mockMvc.perform(MockMvcRequestBuilders.post(awardUrl))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
	
	private List<BadgeAwardRecord> createListOfEmptyBadges() {
		List<BadgeAwardRecord> result = new ArrayList<>();
		result.add(new BadgeAwardRecord());
		result.add(new BadgeAwardRecord());
		result.add(new BadgeAwardRecord());
		
		return result;
	}
	
	private BadgeAwardRecord createEmptyAward() {
		BadgeAwardRecord result = new BadgeAwardRecord();
		result.setUser(new User());
		result.setBadge(new Badge());
		result.setAwardDate(new Date());
		
		return result;
	}
	
	private Badge createBadgeWithId(int id) {
		Badge result = new Badge();
		result.setId(id);
		return result;
	}
	
	private User createUserWithId(int id) {
		User result = TestUtility.createEmptyUserDTO();
		result.setId(id);
		return result;
	}
	
}
