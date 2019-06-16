package skaro.pokeaimpi.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import skaro.pokeaimpi.services.BadgeAwardService;
import skaro.pokeaimpi.web.dtos.NewAwardsDTO;
import skaro.pokeaimpi.web.exceptions.BadgeAwardNotFoundException;

@RestController   
@RequestMapping(path="/award")
public class BadgeAwardController {

	@Autowired
	BadgeAwardService badgeAwardService;
	
	@GetMapping
	public List<NewAwardsDTO> getAll() {
		return badgeAwardService.getAll();
	}
	
	@GetMapping(params={"badgeId"})
	public List<NewAwardsDTO> getByBadgeId(@RequestParam("badgeId") Integer badgeId) {
		return badgeAwardService.getByBadgeId(badgeId);
	}
	
	@GetMapping(params={"userId"})
	public List<NewAwardsDTO> getByUserId(@RequestParam("userId") Integer userId) {
		return badgeAwardService.getByUserId(userId);
	}
	
	@GetMapping(params={"badgeId", "userId"})
	public NewAwardsDTO getByBadgeIdAndUserId(@RequestParam("badgeId") Integer badgeId, @RequestParam("userId") Integer userId) {
		return badgeAwardService.getByBadgeIdAndUserId(badgeId, userId)
			.orElseThrow(() -> new BadgeAwardNotFoundException(badgeId, userId));
	}
	
	@PostMapping()
	public NewAwardsDTO awardBadges(@RequestBody NewAwardsDTO awardDTO) {
		return badgeAwardService.addBadgeAwards(awardDTO);
	}
	
	@PostMapping("/discord/user/{discordUserId}/role/{discordRoleId}")
	public NewAwardsDTO awardBadgeByDiscordRoleId(@RequestParam("discordUserId") Long discordUserId, @RequestParam("discordRoleId") Long discordRoleId) {
		return badgeAwardService.addBadgeAward(discordUserId, discordRoleId);
	}
	
}
