package skaro.pokeaimpi.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import skaro.pokeaimpi.services.PointService;
import skaro.pokeaimpi.services.UserService;
import skaro.pokeaimpi.web.dtos.NewAwardsDTO;
import skaro.pokeaimpi.web.dtos.PointsDTO;
import skaro.pokeaimpi.web.dtos.UserDTO;
import skaro.pokeaimpi.web.exceptions.SocialConnectionNotFoundException;

@RestController   
@RequestMapping(path="/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private PointService pointService;
	
	public UserController() {
		
	}
	
	@GetMapping
	public @ResponseBody List<UserDTO> getAll() {
		return userService.getAll();
	}
	
	@GetMapping("/discord/{id}")
	public UserDTO getByDiscordId(@PathVariable(value="id") Long id) {
		return userService.getByDiscordId(id)
				.orElseThrow(() -> new SocialConnectionNotFoundException(id));
	}
	
	@GetMapping("/twitch/{name}")
	public UserDTO getByTwitchName(@PathVariable(value="name") String name) {
		return userService.getByTwitchName(name)
				.orElseThrow(() -> new SocialConnectionNotFoundException(name));
	}
	
	@PostMapping("/discord/{id}/points/add")
	public NewAwardsDTO addPointsByDiscordId(@PathVariable(value="id") Long id, @Valid @RequestBody PointsDTO pointRequest) {
		return pointService.addPointsViaDiscordId(id, pointRequest.getAmount());
	}
	
}
