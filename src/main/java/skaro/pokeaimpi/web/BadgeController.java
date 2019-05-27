package skaro.pokeaimpi.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import skaro.pokeaimpi.services.BadgeService;
import skaro.pokeaimpi.web.dtos.BadgeDTO;
import skaro.pokeaimpi.web.exceptions.BadgeNotFoundException;

@Controller   
@RequestMapping(path="/badge")
public class BadgeController {

	@Autowired
	private BadgeService badgeService;
	
	@GetMapping
	public @ResponseBody List<BadgeDTO> getAll() {
		return badgeService.getAll();
	}
	
	@GetMapping("/{id}")
	public @ResponseBody BadgeDTO getByDiscordId(@PathVariable(value="id") Integer id) {
		return badgeService.getById(id)
				.orElseThrow(() -> new BadgeNotFoundException(id));
	}
	
}
