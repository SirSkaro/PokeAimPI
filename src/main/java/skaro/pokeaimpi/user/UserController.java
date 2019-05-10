package skaro.pokeaimpi.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller   
public class UserController {
	
	@Autowired
	private UserService userService;
	
	public UserController() {
		
	}
	
	@GetMapping(path="/user")
	public @ResponseBody List<User> getAll() {
		return userService.getAll();
	}
	
	@RequestMapping("/user/discord/{id}")
	public @ResponseBody User getByDiscordId(@PathVariable(value="id") Long id) {
		return userService.getByDiscordId(id).orElse(null);
	}
}
