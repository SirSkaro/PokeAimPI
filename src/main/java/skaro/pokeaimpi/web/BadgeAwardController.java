package skaro.pokeaimpi.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import skaro.pokeaimpi.sdk.resource.BadgeAwardRecord;
import skaro.pokeaimpi.services.BadgeAwardService;
import skaro.pokeaimpi.web.exceptions.BadgeAwardNotFoundException;

@RestController   
@RequestMapping(path="/award")
public class BadgeAwardController {

	@Autowired
	BadgeAwardService badgeAwardService;
	
	@GetMapping
	public List<BadgeAwardRecord> getAll() {
		return badgeAwardService.getAll();
	}
	
	@GetMapping(params={"badgeId"})
	public List<BadgeAwardRecord> getByBadgeId(@RequestParam("badgeId") Integer badgeId) {
		return badgeAwardService.getByBadgeId(badgeId);
	}
	
	@GetMapping(params={"userId"})
	public List<BadgeAwardRecord> getByUserId(@RequestParam("userId") Integer userId) {
		return badgeAwardService.getByUserId(userId);
	}
	
	@GetMapping(params={"userDiscordId"})
	public List<BadgeAwardRecord> getByUserDiscordId(@RequestParam("userDiscordId") String userDiscordId) {
		return badgeAwardService.getByUserDiscordId(userDiscordId);
	}
	
	@GetMapping(params={"discordRoleId"})
	public List<BadgeAwardRecord> getByBadgeDiscordRoleId(@RequestParam("discordRoleId") String discordRoleId) {
		return badgeAwardService.getByBadgeDiscordRoleId(discordRoleId);
	}
	
	@GetMapping(params={"badgeId", "userId"})
	public BadgeAwardRecord getByBadgeIdAndUserId(@RequestParam("badgeId") Integer badgeId, @RequestParam("userId") Integer userId) {
		return badgeAwardService.getByBadgeIdAndUserId(badgeId, userId)
			.orElseThrow(() -> new BadgeAwardNotFoundException(badgeId, userId));
	}
	
	@GetMapping(params={"discordRoleId", "userDiscordId"})
	public BadgeAwardRecord getByBadgeDiscordRoleIdAndUserDiscordId(@RequestParam("discordRoleId") String discordRoleId, @RequestParam("userDiscordId") String userDiscordId) {
		return badgeAwardService.getByDiscordRoleIdAndUserDiscordId(discordRoleId, userDiscordId)
			.orElseThrow(() -> new BadgeAwardNotFoundException(discordRoleId, userDiscordId));
	}
	
	@PostMapping("/discord/user/{userDiscordId}/role/{discordRoleId}")
	@ResponseStatus(HttpStatus.CREATED)
	public BadgeAwardRecord awardBadge(@PathVariable("userDiscordId") String userDiscordId, @PathVariable("discordRoleId") String discordRoleId) {
		return badgeAwardService.addBadgeAward(userDiscordId, discordRoleId);
	}
	
}
