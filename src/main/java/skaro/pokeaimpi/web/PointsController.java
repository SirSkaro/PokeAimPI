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
	public @ResponseBody Integer addPointsViaDiscord(
			@RequestParam("discordId") Long id, 
			@RequestParam("amount") Integer amount) {
		return 10;
	}
	
	@PostMapping(path="/add", params={"twitchName", "amount"})
	public @ResponseBody Integer addPointsViaTwitch(
			@RequestParam("twitchName") String twitchName,
			@RequestParam("amount") Integer amount) {
		return 20;
	}
	
}
