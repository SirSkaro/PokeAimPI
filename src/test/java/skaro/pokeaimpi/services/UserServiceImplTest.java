package skaro.pokeaimpi.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import skaro.pokeaimpi.TestUtility;
import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.implementations.UserServiceImpl;
import skaro.pokeaimpi.web.dtos.UserDTO;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceImplTest {
	
	@TestConfiguration
    static class UserServiceImplTestContextConfiguration {
  
        @Bean
        public UserService getUserService() {
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
		List<UserEntity> allUsers = Arrays.asList(new UserEntity(), new UserEntity(), new UserEntity());
	    Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class))).thenReturn(userDTO);
		Mockito.when(userRepository.findAll()).thenReturn(allUsers);
		
		List<UserDTO> resultUsers = userService.getAll();
		
		assertEquals(allUsers.size(), resultUsers.size());
		for(UserDTO dto : resultUsers) {
			assertNotNull(dto);
		}
	}
	
	@Test
	public void getByDiscordId_shouldGetUserWithDiscordId_whenUserExists() {
		UserDTO userDTO = TestUtility.createEmptyUserDTO();
		Long discordId = 1L;
		userDTO.getSocialProfile().getDiscordConnection().setDiscordId(discordId);
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class))).thenReturn(userDTO);
		Mockito.when(userRepository.getByDiscordId(discordId)).thenReturn(Optional.of(new UserEntity()));
		
		Optional<UserDTO> userWithDiscordId = userService.getByDiscordId(discordId);
		assertTrue(userWithDiscordId.isPresent());
		assertEquals(discordId, userWithDiscordId.get().getSocialProfile().getDiscordConnection().getDiscordId());
	}
	
	@Test
	public void getByTwitchName_shouldGetUserWithTwitchName_whenUserExists() {
		UserDTO userDTO = TestUtility.createEmptyUserDTO();
		String twitchName = "twitch";
		userDTO.getSocialProfile().getTwitchConnection().setUserName(twitchName);
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class))).thenReturn(userDTO);
		Mockito.when(userRepository.getByTwitchUserName(twitchName)).thenReturn(Optional.of(new UserEntity()));
		
		Optional<UserDTO> userWithTwitchName = userService.getByTwitchName(twitchName);
		assertTrue(userWithTwitchName.isPresent());
		assertEquals(twitchName, userWithTwitchName.get().getSocialProfile().getTwitchConnection().getUserName());
	}
	
	@Test
	public void createOrUpdate_shouldPersistUser() {
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class))).thenReturn(new UserDTO());
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserDTO.class), ArgumentMatchers.same(UserEntity.class))).thenReturn(new UserEntity());
		Mockito.when(userRepository.save(ArgumentMatchers.any(UserEntity.class))).thenReturn(new UserEntity());

		userService.createOrUpdate(new UserDTO());

		Mockito.verify(userRepository, VerificationModeFactory.times(1)).save(ArgumentMatchers.any(UserEntity.class));
	}
	
}
