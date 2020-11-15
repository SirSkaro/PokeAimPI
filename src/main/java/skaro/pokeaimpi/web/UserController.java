package skaro.pokeaimpi.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import skaro.pokeaimpi.sdk.request.PointAmount;
import skaro.pokeaimpi.sdk.resource.NewAwardList;
import skaro.pokeaimpi.sdk.resource.User;
import skaro.pokeaimpi.sdk.resource.UserProgress;
import skaro.pokeaimpi.services.PointService;
import skaro.pokeaimpi.services.ProgressService;
import skaro.pokeaimpi.services.UserService;
import skaro.pokeaimpi.web.exceptions.SocialConnectionNotFoundException;

@RestController   
@RequestMapping(path="/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private PointService pointService;
	@Autowired
	private ProgressService progressService;
	
	public UserController() {
		
	}
	
	@GetMapping
	public List<User> getAll() {
		return userService.getAll();
	}
	
	@GetMapping("/discord/{id}")
	public User getByDiscordId(@PathVariable(value="id") String id) {
		return userService.getByDiscordId(id)
				.orElseThrow(() -> new SocialConnectionNotFoundException(id));
	}
	
	@GetMapping("/discord/{id}/progress")
	public UserProgress getProgressByDiscordId(@PathVariable(value="id") String id) {
		return progressService.getByDiscordId(id);
	}
	
	@GetMapping("/twitch/{name}")
	public User getByTwitchName(@PathVariable(value="name") String name) {
		return userService.getByTwitchName(name)
				.orElseThrow(() -> new SocialConnectionNotFoundException(name));
	}
	
	@PostMapping("/discord/{id}/points/add")
	public NewAwardList addPointsByDiscordId(@PathVariable(value="id") String id, @Valid @RequestBody PointAmount pointRequest) {
		return pointService.addPointsViaDiscordId(id, pointRequest.getAmount());
	}
	
}
