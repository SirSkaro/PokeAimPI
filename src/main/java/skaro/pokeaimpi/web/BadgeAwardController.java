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
import skaro.pokeaimpi.web.exceptions.BadgeAwardNotFoundException;

@RestController   
@RequestMapping(path="/award")
public class BadgeAwardController {

	@Autowired
	BadgeAwardService badgeAwardService;
	
	@GetMapping
	public List<BadgeAwardDTO> getAll() {
		return badgeAwardService.getAll();
	}
	
	@GetMapping(params={"badgeId"})
	public List<BadgeAwardDTO> getByBadgeId(@RequestParam("badgeId") Integer badgeId) {
		return badgeAwardService.getByBadgeId(badgeId);
	}
	
	@GetMapping(params={"userId"})
	public List<BadgeAwardDTO> getByUserId(@RequestParam("userId") Integer userId) {
		return badgeAwardService.getByUserId(userId);
	}
	
	@GetMapping(params={"userDiscordId"})
	public List<BadgeAwardDTO> getByUserDiscordId(@RequestParam("userDiscordId") Long userDiscordId) {
		return badgeAwardService.getByUserDiscordId(userDiscordId);
	}
	
	@GetMapping(params={"discordRoleId"})
	public List<BadgeAwardDTO> getByBadgeDiscordRoleId(@RequestParam("discordRoleId") Long discordRoleId) {
		return badgeAwardService.getByBadgeDiscordRoleId(discordRoleId);
	}
	
	@GetMapping(params={"badgeId", "userId"})
	public BadgeAwardDTO getByBadgeIdAndUserId(@RequestParam("badgeId") Integer badgeId, @RequestParam("userId") Integer userId) {
		return badgeAwardService.getByBadgeIdAndUserId(badgeId, userId)
			.orElseThrow(() -> new BadgeAwardNotFoundException(badgeId, userId));
	}
	
	@GetMapping(params={"discordRoleId", "userDiscordId"})
	public BadgeAwardDTO getByBadgeDiscordRoleIdAndUserDiscordId(@RequestParam("discordRoleId") Long discordRoleId, @RequestParam("userDiscordId") Long userDiscordId) {
		return badgeAwardService.getByDiscordRoleIdAndUserDiscordId(discordRoleId, userDiscordId)
			.orElseThrow(() -> new BadgeAwardNotFoundException(discordRoleId, userDiscordId));
	}
	
	@PostMapping("/discord/user/{discordUserId}/role/{discordRoleId}")
	public BadgeAwardDTO awardBadgeByDiscordRoleId(@RequestParam("discordUserId") Long discordUserId, @RequestParam("discordRoleId") Long discordRoleId) {
		return badgeAwardService.addBadgeAward(discordUserId, discordRoleId);
	}
	
}
