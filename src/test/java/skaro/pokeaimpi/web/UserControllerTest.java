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
		List<UserDTO> usersInRepo = new ArrayList<>();
		usersInRepo.add(new UserDTO());
		usersInRepo.add(new UserDTO());
		usersInRepo.add(new UserDTO());
		UserDTO testUserDTO = setUpMockUserDTO();
		Long discordId = testUserDTO.getSocialProfile().getDiscordConnection().getDiscordId();
		
		Mockito.when(userService.getAll()).thenReturn(usersInRepo);
		Mockito.when(userService.getByDiscordId(discordId)).thenReturn(Optional.of(testUserDTO));
		Mockito.when(userService.getByDiscordId(discordId)).thenReturn(Optional.of(testUserDTO));
		Mockito.when(userService.getByDiscordId(2L)).thenThrow(new SocialConnectionNotFoundException(2L));
		Mockito.when(userService.getByTwitchName("test_user")).thenReturn(Optional.of(testUserDTO));
		Mockito.when(userService.getByTwitchName("no_such_user")).thenThrow(new SocialConnectionNotFoundException("no_such_user"));
		
		BadgeAwardDTO testEmptyAwardDTO = setUpMockEmptyAwardDTO(testUserDTO);
		BadgeAwardDTO testNonEmptyAwardDTO = setUpMockNonEmptyAwardDTO(testUserDTO);
		Mockito.when(pointService.addPointsViaDiscordId(discordId, 1)).thenReturn(testEmptyAwardDTO);
		Mockito.when(pointService.addPointsViaDiscordId(discordId, 2)).thenReturn(testNonEmptyAwardDTO);
	}
	
	@Test
	public void getAll_ShouldGetAllUsersFromUserService() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
	}
	
	@Test
	public void getByDiscordId_ShouldGetUserWithSpecifiedId() throws Exception {
		int discordId = 1;
		mockMvc.perform(MockMvcRequestBuilders.get("/user/discord/"+ discordId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.discordConnection.discordId", Matchers.is(discordId)));
	}
	
	@Test
	public void getByDiscordId_Should404_WhenNoDiscordIdExists() throws Exception {
		int discordId = 2;
		mockMvc.perform(MockMvcRequestBuilders.get("/user/discord/"+ discordId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void getByTwitchName_ShouldGetUserWithSpecifiedName() throws Exception {
		String userName = "test_user";
		mockMvc.perform(MockMvcRequestBuilders.get("/user/twitch/"+ userName))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.twitchConnection.userName", Matchers.is(userName)));
	}
	
	@Test
	public void getByTwitchname_Should404_WhenNoTwitchNameExists() throws Exception {
		String userName = "no_such_user";
		mockMvc.perform(MockMvcRequestBuilders.get("/user/twitch/"+ userName))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void addPointsByDiscordId_shouldReturnAnEmptyListOfAwards_WhenNoThresholdIsPassed() throws Exception {
		int discordId = 1;
		PointsDTO testRequest = new PointsDTO();
		testRequest.setAmount(1);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/user/discord/"+discordId+"/points/add")
				.content("{\"amount\":1}")
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
	public void addPointsByDiscordId_shouldReturnListOfAwards_WhenThresholdIsPassed() throws Exception {
		int discordId = 1;
		PointsDTO testRequest = new PointsDTO();
		testRequest.setAmount(1);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/user/discord/"+discordId+"/points/add")
				.content("{\"amount\":2}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.user.socialProfile.discordConnection.discordId", Matchers.is(discordId)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.badges").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$.badges", Matchers.hasSize(2)));
	}
	
	private UserDTO setUpMockUserDTO() {
		UserDTO discordUserDTO = new UserDTO();
		SocialProfile profile = new SocialProfile();
		DiscordConnection discordConnection = new DiscordConnection();
		discordConnection.setDiscordId(1L);
		TwitchConnection twitchConnection = new TwitchConnection();
		twitchConnection.setUserName("test_user");
		profile.setDiscordConnection(discordConnection);
		profile.setTwitchConnection(twitchConnection);
		discordUserDTO.setSocialProfile(profile);
		
		return discordUserDTO;
	}
	
	private BadgeAwardDTO setUpMockEmptyAwardDTO(UserDTO user) {
		BadgeAwardDTO result = new BadgeAwardDTO();
		result.setUser(user);
		result.setBadges(new ArrayList<BadgeDTO>());
		
		return result;
	}
	
	private BadgeAwardDTO setUpMockNonEmptyAwardDTO(UserDTO user) {
		BadgeAwardDTO result = setUpMockEmptyAwardDTO(user);
		List<BadgeDTO> badges = new ArrayList<>();
		badges.add(new BadgeDTO());
		badges.add(new BadgeDTO());
		
		result.setBadges(badges);
		return result;
	}
}
