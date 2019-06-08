package skaro.pokeaimpi.services;

import java.util.List;
import java.util.Optional;

import skaro.pokeaimpi.web.dtos.BadgeDTO;

public interface BadgeService {
	
	public List<BadgeDTO> getAll();
	public Optional<BadgeDTO> getById(Integer id);
	public Optional<BadgeDTO> getByDiscordRoleId(Long discordRoleId);
	public List<BadgeDTO> getBadgesBetween(int floor, int ceiling);
	
}
