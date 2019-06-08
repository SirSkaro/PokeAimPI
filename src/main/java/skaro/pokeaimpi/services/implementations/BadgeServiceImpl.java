package skaro.pokeaimpi.services.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skaro.pokeaimpi.repository.BadgeRepository;
import skaro.pokeaimpi.services.BadgeService;
import skaro.pokeaimpi.web.dtos.BadgeDTO;

@Service
public class BadgeServiceImpl implements BadgeService {

	@Autowired
	private BadgeRepository badgeRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<BadgeDTO> getAll() {
		return badgeRepository.findAll()
				.stream()
				.map(badge -> modelMapper.map(badge, BadgeDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<BadgeDTO> getById(Integer id) {
		return badgeRepository.findById(id)
				.map(badge -> modelMapper.map(badge, BadgeDTO.class));
	}

	@Override
	public List<BadgeDTO> getBadgesBetween(int floor, int ceiling) {
		return badgeRepository.getByPointThresholdBetween(floor, ceiling)
				.stream()
				.map(badge -> modelMapper.map(badge, BadgeDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<BadgeDTO> getByDiscordRoleId(Long discordRoleId) {
		return badgeRepository.getByDiscordRoleId(discordRoleId)
				.map(badge -> modelMapper.map(badge, BadgeDTO.class));
	}

}
