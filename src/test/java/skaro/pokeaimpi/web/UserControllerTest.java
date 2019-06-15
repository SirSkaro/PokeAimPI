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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import skaro.pokeaimpi.services.PointService;
import skaro.pokeaimpi.services.UserService;
import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;
import skaro.pokeaimpi.web.dtos.BadgeDTO;
import skaro.pokeaimpi.web.dtos.DiscordConnection;
import skaro.pokeaimpi.web.dtos.PointsDTO;
import skaro.pokeaimpi.web.dtos.SocialProfile;
import skaro.pokeaimpi.web.dtos.TwitchConnection;
import skaro.pokeaimpi.web.dtos.UserDTO;
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
	
	@Before
	public void setUp() {
		UserDTO testUserDTO = setUpMockUserDTO();
		Long discordId = 1L;
		
		testUserDTO.getSocialProfile().getDiscordConnection().setDiscordId(discordId);
		Mockito.when(userService.getByDiscordId(discordId)).thenReturn(Optional.of(testUserDTO));
	}
	
	@Test
	public void getAll_shouldGetAllUsers() throws Exception {
		List<UserDTO> usersInRepo = new ArrayList<>();
		usersInRepo.add(new UserDTO());
		usersInRepo.add(new UserDTO());
		usersInRepo.add(new UserDTO());
		Mockito.when(userService.getAll()).thenReturn(usersInRepo);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/user"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
	}
	
	@Test
	public void getByDiscordId_shouldGetUserWithSpecifiedId_whenUserExists() throws Exception {
		int discordId = 1;
		mockMvc.perform(MockMvcRequestBuilders.get("/user/discord/"+ discordId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.discordConnection.discordId", Matchers.is(discordId)));
	}
	
	@Test
	public void gettingAnyUser_shouldGetUserWithExpectedFields_whenUserExists() throws Exception {
		int discordId = 1;
		mockMvc.perform(MockMvcRequestBuilders.get("/user/discord/"+ discordId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.discordConnection").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.twitchConnection").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.discordConnection.discordId", Matchers.anything()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.twitchConnection.userName", Matchers.anything()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.anything()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.points", Matchers.anything()));
	}
	
	@Test
	public void getByDiscordId_should404_whenNoDiscordIdExists() throws Exception {
		long discordId = 2;
		Mockito.when(userService.getByDiscordId(discordId)).thenThrow(new SocialConnectionNotFoundException(discordId));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/user/discord/"+ discordId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void getByTwitchName_shouldGetUserWithSpecifiedName() throws Exception {
		UserDTO testUserDTO = setUpMockUserDTO();
		String userName = "test_user";
		testUserDTO.getSocialProfile().getTwitchConnection().setUserName(userName);
		Mockito.when(userService.getByTwitchName("test_user")).thenReturn(Optional.of(testUserDTO));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/user/twitch/"+ userName))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.twitchConnection.userName", Matchers.is(userName)));
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
		UserDTO testUserDTO = setUpMockUserDTO();
		long discordId = 1;
		BadgeAwardDTO testEmptyAwardDTO = setUpMockEmptyAwardDTO(testUserDTO, discordId);
		Mockito.when(pointService.addPointsViaDiscordId(discordId, 1)).thenReturn(testEmptyAwardDTO);
		
		PointsDTO testRequest = new PointsDTO();
		testRequest.setAmount(1);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/user/discord/"+discordId+"/points/add")
				.content("{\"amount\":1}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.user.socialProfile.discordConnection.discordId", Matchers.is((int)discordId)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.badges").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$.badges", Matchers.hasSize(0)));
	}
	
	@Test
	public void addPointsByDiscordId_shouldReturnListOfAwards_whenThresholdIsPassed() throws Exception {
		UserDTO testUserDTO = setUpMockUserDTO();
		long discordId = 1;
		BadgeAwardDTO testNonEmptyAwardDTO = setUpMockNonEmptyAwardDTO(testUserDTO, discordId);
		Mockito.when(pointService.addPointsViaDiscordId(discordId, 2)).thenReturn(testNonEmptyAwardDTO);
		PointsDTO testRequest = new PointsDTO();
		testRequest.setAmount(1);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/user/discord/"+discordId+"/points/add")
				.content("{\"amount\":2}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.user.socialProfile.discordConnection.discordId", Matchers.is((int)discordId)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.badges").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$.badges", Matchers.hasSize(2)));
	}
	
	@Test
	public void addPointsByDiscordId_should400_whenNegativePointsAreAdded() throws Exception {
		int discordId = 1;
		PointsDTO testRequest = new PointsDTO();
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
	
	private UserDTO setUpMockUserDTO() {
		UserDTO discordUserDTO = new UserDTO();
		SocialProfile profile = new SocialProfile();
		DiscordConnection discordConnection = new DiscordConnection();
		TwitchConnection twitchConnection = new TwitchConnection();
		profile.setDiscordConnection(discordConnection);
		profile.setTwitchConnection(twitchConnection);
		discordUserDTO.setSocialProfile(profile);
		
		return discordUserDTO;
	}
	
	private BadgeAwardDTO setUpMockEmptyAwardDTO(UserDTO user, Long discordId) {
		BadgeAwardDTO result = new BadgeAwardDTO();
		user.getSocialProfile().getDiscordConnection().setDiscordId(discordId);
		result.setUser(user);
		result.setBadges(new ArrayList<BadgeDTO>());
		
		return result;
	}
	
	private BadgeAwardDTO setUpMockNonEmptyAwardDTO(UserDTO user, Long discordId) {
		BadgeAwardDTO result = setUpMockEmptyAwardDTO(user, discordId);
		List<BadgeDTO> badges = new ArrayList<>();
		badges.add(new BadgeDTO());
		badges.add(new BadgeDTO());
		
		result.setBadges(badges);
		return result;
	}
}
