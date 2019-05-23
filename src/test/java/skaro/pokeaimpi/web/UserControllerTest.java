package skaro.pokeaimpi.web;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import skaro.pokeaimpi.services.UserService;
import skaro.pokeaimpi.web.dtos.DiscordConnection;
import skaro.pokeaimpi.web.dtos.SocialProfile;
import skaro.pokeaimpi.web.dtos.UserDTO;
import skaro.pokeaimpi.web.exceptions.SocialConnectionNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	@Before
	public void setUp() {
		List<UserDTO> usersInRepo = new ArrayList<>();
		usersInRepo.add(new UserDTO());
		usersInRepo.add(new UserDTO());
		usersInRepo.add(new UserDTO());
		
		when(userService.getAll()).thenReturn(usersInRepo);
		when(userService.getByDiscordId(1L)).thenReturn(Optional.of(setUpMockDiscordUserDTO()));
		when(userService.getByDiscordId(2L)).thenThrow(new SocialConnectionNotFoundException(2L));
	}
	
	@Test
	public void getAllShouldGetAllUsersFromUserService() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
	}
	
	@Test
	public void getByDiscordIdShouldGetUserWithSpecifiedId() throws Exception {
		int discordId = 1;
		mockMvc.perform(MockMvcRequestBuilders.get("/user/discord/"+ discordId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
		.andExpect(MockMvcResultMatchers.jsonPath("$.socialProfile.discordConnection.discordId", Matchers.is(discordId)));
	}
	
	@Test
	public void getByDiscordIdShould404WhenNoDiscordIdExists() throws Exception {
		int discordId = 2;
		mockMvc.perform(MockMvcRequestBuilders.get("/user/discord/"+ discordId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	private UserDTO setUpMockDiscordUserDTO() {
		UserDTO discordUserDTO = new UserDTO();
		SocialProfile profile = new SocialProfile();
		DiscordConnection discordConnection = new DiscordConnection();
		discordConnection.setDiscordId(1L);
		profile.setDiscordConnection(discordConnection);
		discordUserDTO.setSocialProfile(profile);
		
		return discordUserDTO;
	}
}
