package skaro.pokeaimpi.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import skaro.pokeaimpi.TestUtility;
import skaro.pokeaimpi.sdk.config.PokeAimPiProperties;
import skaro.pokeaimpi.sdk.request.PointAmount;
import skaro.pokeaimpi.sdk.resource.Badge;
import skaro.pokeaimpi.sdk.resource.NewAwardList;
import skaro.pokeaimpi.sdk.resource.User;
import skaro.pokeaimpi.sdk.resource.UserProgress;
import skaro.pokeaimpi.services.PointService;
import skaro.pokeaimpi.services.ProgressService;
import skaro.pokeaimpi.services.UserService;
import skaro.pokeaimpi.web.exceptions.SocialConnectionNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private PointService pointService;
	
	@MockBean
	PokeAimPiProperties messageProperties;
	
	@MockBean
	private ProgressService progressService;
	
	@Test
	public void getAll_shouldGetAllUsers() throws Exception {
		List<User> allUsers = new ArrayList<>();
		allUsers.add(new User());
		allUsers.add(new User());
		allUsers.add(new User());
		Mockito.when(userService.getAll()).thenReturn(allUsers);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/user"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(allUsers.size())));
	}
	
	@Test
	public void getByDiscordId_shouldGetUserWithSpecifiedId_whenUserExists() throws Exception {
		User userDTO = setUpMockUserDTO();
		String discordId = userDTO.getSocialProfile().getDiscordConnection().getDiscordId();
		Mockito.when(userService.getByDiscordId(discordId)).thenReturn(Optional.of(userDTO));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/user/discord/"+ discordId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.discordConnection.discordId", Matchers.is(discordId)));
	}
	
	@Test
	public void gettingAnyUser_shouldGetUserWithExpectedFields_whenUserExists() throws Exception {
		User userDTO = setUpMockUserDTO();
		String discordId = userDTO.getSocialProfile().getDiscordConnection().getDiscordId();
		Mockito.when(userService.getByDiscordId(discordId)).thenReturn(Optional.of(userDTO));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/user/discord/"+ discordId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.discordConnection").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.twitchConnection").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.discordConnection.discordId").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.twitchConnection.userName").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.points").exists());
	}
	
	@Test
	public void getByDiscordId_should404_whenNoDiscordIdExists() throws Exception {
		String discordId = "2";
		Mockito.when(userService.getByDiscordId(discordId)).thenThrow(new SocialConnectionNotFoundException(discordId));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/user/discord/"+ discordId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void getByTwitchName_shouldGetUserWithSpecifiedName() throws Exception {
		User userDTO = setUpMockUserDTO();
		String twitchUserName = userDTO.getSocialProfile().getTwitchConnection().getUserName();
		Mockito.when(userService.getByTwitchName(twitchUserName)).thenReturn(Optional.of(userDTO));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/user/twitch/"+ twitchUserName))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.twitchConnection.userName", Matchers.is(twitchUserName)));
	}
	
	@Test
	public void getByTwitchname_Should404_whenNoTwitchNameExists() throws Exception {
		String userName = "no_such_user";
		Mockito.when(userService.getByTwitchName(userName)).thenThrow(new SocialConnectionNotFoundException(userName));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/user/twitch/"+ userName))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void addPointsByDiscordId_shouldReturnAnEmptyListOfAwards_whenNoThresholdIsPassed() throws Exception {
		User userDTO = setUpMockUserDTO();
		String discordId = userDTO.getSocialProfile().getDiscordConnection().getDiscordId();
		NewAwardList testEmptyAwardDTO = setUpMockEmptyAwardDTO(userDTO, discordId);
		Mockito.when(pointService.addPointsViaDiscordId(discordId, 1)).thenReturn(testEmptyAwardDTO);
		
		PointAmount testRequest = new PointAmount();
		testRequest.setAmount(1);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/user/discord/"+discordId+"/points/add")
				.content(TestUtility.convertObjectToJsonBytes(testRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.user.socialProfile.discordConnection.discordId", Matchers.is(discordId)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.badges").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$.badges", Matchers.hasSize(0)));
	}
	
	@Test
	public void addPointsByDiscordId_shouldReturnListOfAwards_whenThresholdIsPassed() throws Exception {
		User userDTO = setUpMockUserDTO();
		String discordId = userDTO.getSocialProfile().getDiscordConnection().getDiscordId();
		NewAwardList testNonEmptyAwardDTO = setUpMockNonEmptyAwardDTO(userDTO, discordId);
		Mockito.when(pointService.addPointsViaDiscordId(discordId, 2)).thenReturn(testNonEmptyAwardDTO);
		PointAmount testRequest = new PointAmount();
		testRequest.setAmount(2);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/user/discord/"+discordId+"/points/add")
				.content(TestUtility.convertObjectToJsonBytes(testRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.user.socialProfile.discordConnection.discordId", Matchers.is(discordId)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.badges").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$.badges", Matchers.hasSize(2)));
	}
	
	@Test
	public void addPointsByDiscordId_should400_whenNegativePointsAreAdded() throws Exception {
		int discordId = 1;
		PointAmount testRequest = new PointAmount();
		testRequest.setAmount(1);
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/user/discord/"+discordId+"/points/add")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(builder.content("{\"amount\": -1}"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		mockMvc.perform(builder.content("{\"amount\": 0}"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void getProgressByDiscordId_shouldReturnProgressOfUserWithExpectedFields() throws Exception {
		String userDiscordId = "1";
		UserProgress progressDTO = setUpMockProgressDTO(userDiscordId);
		Mockito.when(progressService.getByDiscordId(userDiscordId)).thenReturn(progressDTO);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/user/discord/"+ userDiscordId + "/progress"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.user").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.nextBadge").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.currentHighestBadge").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.pointsToNextReward").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.currentPoints").exists());
	}
	
	private User setUpMockUserDTO() {
		User result = TestUtility.createEmptyUserDTO();
		
		result.setPoints(12);
		result.setId(1);
		result.getSocialProfile().getDiscordConnection().setDiscordId("1");
		result.getSocialProfile().getTwitchConnection().setUserName("twitch_name");
		
		return result;
	}
	
	private NewAwardList setUpMockEmptyAwardDTO(User user, String discordId) {
		NewAwardList result = new NewAwardList();
		user.getSocialProfile().getDiscordConnection().setDiscordId(discordId);
		result.setUser(user);
		result.setBadges(new ArrayList<Badge>());
		
		return result;
	}
	
	private NewAwardList setUpMockNonEmptyAwardDTO(User user, String discordId) {
		NewAwardList result = setUpMockEmptyAwardDTO(user, discordId);
		List<Badge> badges = new ArrayList<>();
		badges.add(new Badge());
		badges.add(new Badge());
		
		result.setBadges(badges);
		return result;
	}
	
	private UserProgress setUpMockProgressDTO(String userDiscordId) {
		UserProgress result = new UserProgress();
		result.setCurrentHighestBadge(new Badge());
		result.setCurrentPoints(1);
		result.setNextBadge(new Badge());
		result.setPointsToNextReward(1);
		
		User user = TestUtility.createEmptyUserDTO();
		user.getSocialProfile().getDiscordConnection().setDiscordId(userDiscordId);
		result.setUser(user);
		
		return result;
	}
}
