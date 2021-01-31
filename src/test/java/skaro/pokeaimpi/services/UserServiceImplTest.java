package skaro.pokeaimpi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import skaro.pokeaimpi.TestUtility;
import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.sdk.resource.User;
import skaro.pokeaimpi.services.implementations.UserServiceImpl;

@ExtendWith(SpringExtension.class)
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
		User User = new User();
		List<UserEntity> allUsers = Arrays.asList(new UserEntity(), new UserEntity(), new UserEntity());
	    Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(User.class))).thenReturn(User);
		Mockito.when(userRepository.findAll()).thenReturn(allUsers);
		
		List<User> resultUsers = userService.getAll();
		
		assertEquals(allUsers.size(), resultUsers.size());
		for(User dto : resultUsers) {
			assertNotNull(dto);
		}
	}
	
	@Test
	public void getByDiscordId_shouldGetUserWithDiscordId_whenUserExists() {
		User User = TestUtility.createEmptyUserDTO();
		String discordId = "1";
		User.getSocialProfile().getDiscordConnection().setDiscordId(discordId);
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(User.class))).thenReturn(User);
		Mockito.when(userRepository.getByDiscordId(discordId)).thenReturn(Optional.of(new UserEntity()));
		
		Optional<User> userWithDiscordId = userService.getByDiscordId(discordId);
		assertTrue(userWithDiscordId.isPresent());
		assertEquals(discordId, userWithDiscordId.get().getSocialProfile().getDiscordConnection().getDiscordId());
	}
	
	@Test
	public void getByTwitchName_shouldGetUserWithTwitchName_whenUserExists() {
		User User = TestUtility.createEmptyUserDTO();
		String twitchName = "twitch";
		User.getSocialProfile().getTwitchConnection().setUserName(twitchName);
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(User.class))).thenReturn(User);
		Mockito.when(userRepository.getByTwitchUserName(twitchName)).thenReturn(Optional.of(new UserEntity()));
		
		Optional<User> userWithTwitchName = userService.getByTwitchName(twitchName);
		assertTrue(userWithTwitchName.isPresent());
		assertEquals(twitchName, userWithTwitchName.get().getSocialProfile().getTwitchConnection().getUserName());
	}
	
	@Test
	public void createOrUpdate_shouldPersistUser() {
		Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(User.class))).thenReturn(new User());
		Mockito.when(modelMapper.map(ArgumentMatchers.any(User.class), ArgumentMatchers.same(UserEntity.class))).thenReturn(new UserEntity());
		Mockito.when(userRepository.save(ArgumentMatchers.any(UserEntity.class))).thenReturn(new UserEntity());

		userService.createOrUpdate(new User());

		Mockito.verify(userRepository, VerificationModeFactory.times(1)).save(ArgumentMatchers.any(UserEntity.class));
	}
	
}
