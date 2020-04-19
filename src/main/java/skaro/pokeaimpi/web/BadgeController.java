package skaro.pokeaimpi.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import skaro.pokeaimpi.services.BadgeService;
import skaro.pokeaimpi.web.dtos.BadgeDTO;
import skaro.pokeaimpi.web.exceptions.BadgeNotFoundException;

@RestController    
@RequestMapping(path="/badge")
public class BadgeController {

	@Autowired
	private BadgeService badgeService;
	
	@GetMapping
	public List<BadgeDTO> getAll() {
		return badgeService.getAll();
	}
	
	@GetMapping("/{id}")
	public BadgeDTO getById(@PathVariable(value="id") Integer id) {
		return badgeService.getById(id)
				.orElseThrow(() -> new BadgeNotFoundException(id));
	}
	
	@GetMapping("/discord/{id}")
	public BadgeDTO getByDiscordRoleId(@PathVariable(value="id") String discordRoleId) {
		return badgeService.getByDiscordRoleId(discordRoleId)
				.orElseThrow(() -> new BadgeNotFoundException(discordRoleId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BadgeDTO saveBadge(@RequestBody @Valid BadgeDTO badge) {
		return badgeService.saveBadge(badge);
	}
	
}
