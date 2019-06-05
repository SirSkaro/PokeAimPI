package skaro.pokeaimpi.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import skaro.pokeaimpi.services.BadgeAwardService;
import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;

@RestController   
@RequestMapping(path="/award")
public class BadgeAwardController {

	@Autowired
	BadgeAwardService badgeAwardService;
	
	@GetMapping
	public List<BadgeAwardDTO> getAll() {
		return badgeAwardService.getAll();
	}
	
	@PostMapping(params={"badgeId"})
	public void getByBadgeId(@RequestParam("badgeId") Integer badgeId) {
		badgeAwardService.getByBadgeId(badgeId);
	}
	
	@PostMapping(params={"userId"})
	public void getByUserId(@RequestParam("userId") Integer userId) {
		badgeAwardService.getByUserId(userId);
	}
	
	@PostMapping(params={"badgeId", "userId"})
	public void getByBadgeIdAndUserId(
			@RequestParam("badgeId") Integer badgeId,
			@RequestParam("userId") Integer userId) {
		badgeAwardService.getByBadgeIdAndUserId(badgeId, userId);
	}
	
}
