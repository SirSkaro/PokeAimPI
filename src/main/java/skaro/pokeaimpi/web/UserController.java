package skaro.pokeaimpi.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import skaro.pokeaimpi.service.UserService;
import skaro.pokeaimpi.web.dto.UserDTO;

@Controller   
public class UserController {
	
	@Autowired
	private UserService userService;
	
	public UserController() {
		
	}
	
	@GetMapping(path="/user")
	public @ResponseBody List<UserDTO> getAll() {
		return userService.getAll();
	}
	
	@GetMapping("/user/discord/{id}")
	public @ResponseBody UserDTO getByDiscordId(@PathVariable(value="id") Long id) {
		return userService.getByDiscordId(id).orElse(null);
	}
	
	
}
