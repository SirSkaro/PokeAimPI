package skaro.pokeaimpi.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.implementations.UserServiceImpl;
import skaro.pokeaimpi.web.dtos.DiscordConnection;
import skaro.pokeaimpi.web.dtos.SocialProfile;
import skaro.pokeaimpi.web.dtos.TwitchConnection;
import skaro.pokeaimpi.web.dtos.UserDTO;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceImplTest {
	
	@TestConfiguration
    static class UserServiceImplTestContextConfiguration {
  
        @Bean
        public UserService employeeService() {
            return new UserServiceImpl();
        }
        
    }
	
	@Autowired
	private UserService userService;
	@MockBean
	private UserRepository userRepository;
	@MockBean
	private ModelMapper modelMapper;
	
	@Test
	public void getAll_shouldGetAllUsers() throws Exception {
		UserDTO userDTO = new UserDTO();
		List<UserEntity> allUsers = new ArrayList<>();
		allUsers.add(new UserEntity());
		allUsers.add(new UserEntity());
		allUsers.add(new UserEntity());
	    Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class))).thenReturn(userDTO);
		Mockito.when(userRepository.findAll()).thenReturn(allUsers);
		
		List<UserDTO> resultUsers = userService.getAll();
		
		assertEquals(allUsers.size(), resultUsers.size());
		for(UserDTO dto : resultUsers) {
			assertNotNull(dto);
		}
	}
	
	@Test
	public void getByDiscordId_shouldGetUserWithDiscordId_whenUserExists() throws Exception {
		UserDTO userDTO = createEmptyUserDTO();
		Long discordId = 1L;
		userDTO.getSocialProfile().getDiscordConnection().setDiscordId(discordId);
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class))).thenReturn(userDTO);
		Mockito.when(userRepository.findByDiscordId(discordId)).thenReturn(Optional.of(new UserEntity()));
		
		Optional<UserDTO> userWithDiscordId = userService.getByDiscordId(discordId);
		assertTrue(userWithDiscordId.isPresent());
		assertEquals(discordId, userWithDiscordId.get().getSocialProfile().getDiscordConnection().getDiscordId());
	}
	
	@Test
	public void getByTwitchName_shouldGetUserWithTwitchName_whenUserExists() throws Exception {
		UserDTO userDTO = createEmptyUserDTO();
		String twitchName = "twitch";
		userDTO.getSocialProfile().getTwitchConnection().setUserName(twitchName);
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class))).thenReturn(userDTO);
		Mockito.when(userRepository.findByTwitchUserName(twitchName)).thenReturn(Optional.of(new UserEntity()));
		
		Optional<UserDTO> userWithTwitchName = userService.getByTwitchName(twitchName);
		assertTrue(userWithTwitchName.isPresent());
		assertEquals(twitchName, userWithTwitchName.get().getSocialProfile().getTwitchConnection().getUserName());
	}
	
	private UserDTO createEmptyUserDTO() {
		UserDTO result = new UserDTO();
		SocialProfile profile = new SocialProfile();
		DiscordConnection discordConnection = new DiscordConnection();
		TwitchConnection twitchConnection = new TwitchConnection();
		profile.setDiscordConnection(discordConnection);
		profile.setTwitchConnection(twitchConnection);
		result.setSocialProfile(profile);
		
		return result;
	}
	
}