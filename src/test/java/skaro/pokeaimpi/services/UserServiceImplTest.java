package skaro.pokeaimpi.services;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
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
	    Mockito.when(modelMapper.map(ArgumentMatchers.any(UserEntity.class), ArgumentMatchers.same(UserDTO.class))).thenReturn(userDTO);
		    
		List<UserEntity> allUsers = new ArrayList<>();
		allUsers.add(new UserEntity());
		allUsers.add(new UserEntity());
		allUsers.add(new UserEntity());
		Mockito.when(userRepository.findAll()).thenReturn(allUsers);
		
		List<UserDTO> resultUsers = userService.getAll();
		
		for(UserDTO dto : resultUsers) {
			assertNotNull(dto);
		}
	}
	
}
