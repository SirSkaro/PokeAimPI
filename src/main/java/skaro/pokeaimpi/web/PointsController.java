package skaro.pokeaimpi.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import skaro.pokeaimpi.services.PointsService;

@Controller
@RequestMapping(path="/points")
public class PointsController {
	
	@Autowired
	private PointsService pointsService;
	
	@PostMapping(path="/add", params={"discordId","amount"})
	public @ResponseBody void addPointsViaDiscord(
			@RequestParam("discordId") Long discordId, 
			@RequestParam("amount") Integer amount) {
		pointsService.addPointsViaDiscordId(discordId, amount);
	}
	
	@PostMapping(path="/add", params={"twitchName", "amount"})
	public @ResponseBody void addPointsViaTwitch(
			@RequestParam("twitchName") String twitchName,
			@RequestParam("amount") Integer amount) {
		pointsService.addPointsViaTwitchName(twitchName, amount);
	}
	
}
