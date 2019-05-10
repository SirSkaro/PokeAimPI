package skaro.pokeaimpi.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller    
@RequestMapping(path="/api")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	public UserController() {
		
	}
	
	@GetMapping(path="/user")
	List<User> getAll() {
		return userRepository.findAll();
	}
}
