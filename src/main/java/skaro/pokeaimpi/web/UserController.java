package skaro.pokeaimpi.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import skaro.pokeaimpi.services.UserService;
import skaro.pokeaimpi.web.dtos.UserDTO;
import skaro.pokeaimpi.web.exceptions.SocialConnectionNotFoundException;

@Controller   
@RequestMapping(path="/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	public UserController() {
		
	}
	
	@GetMapping
	public @ResponseBody List<UserDTO> getAll() {
		return userService.getAll();
	}
	
	@GetMapping("/discord/{id}")
	public @ResponseBody UserDTO getByDiscordId(@PathVariable(value="id") Long id) {
		return userService.getByDiscordId(id)
				.orElseThrow(() -> new SocialConnectionNotFoundException(id));
	}
	
	@GetMapping("/twitch/{name}")
	public @ResponseBody UserDTO getByTwitchName(@PathVariable(value="name") String name) {
		return userService.getByTwitchName(name)
				.orElseThrow(() -> new SocialConnectionNotFoundException(name));
	}
	
	
}
