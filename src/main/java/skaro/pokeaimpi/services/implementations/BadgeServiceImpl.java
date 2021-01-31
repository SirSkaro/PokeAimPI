package skaro.pokeaimpi.services.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skaro.pokeaimpi.repository.BadgeRepository;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.eventhandler.BadgeEventHandler;
import skaro.pokeaimpi.sdk.resource.Badge;
import skaro.pokeaimpi.services.BadgeService;
import skaro.pokeaimpi.web.exceptions.BadgeNotFoundException;

@Service
public class BadgeServiceImpl implements BadgeService {

	@Autowired
	private BadgeRepository badgeRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired(required = false)
	private BadgeEventHandler eventHandler;
	
	@Override
	public List<Badge> getAll() {
		return badgeRepository.findAll()
				.stream()
				.map(badge -> modelMapper.map(badge, Badge.class))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Badge> getById(Integer id) {
		return badgeRepository.findById(id)
				.map(badge -> modelMapper.map(badge, Badge.class));
	}

	@Override
	public List<Badge> getBadgesBetween(int floor, int ceiling) {
		return badgeRepository.getByCanBeEarnedWithPointsTrueAndPointThresholdBetween(floor, ceiling)
				.stream()
				.map(badge -> modelMapper.map(badge, Badge.class))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Badge> getByDiscordRoleId(String discordRoleId) {
		return badgeRepository.getByDiscordRoleId(discordRoleId)
				.map(badge -> modelMapper.map(badge, Badge.class));
	}

	@Override
	public Badge saveBadge(Badge badge) {
		BadgeEntity badgeEntity = modelMapper.map(badge, BadgeEntity.class);
		badgeEntity = badgeRepository.save(badgeEntity);
		
		return modelMapper.map(badgeEntity, Badge.class);
	}

	@Override
	public void deleteBadge(Integer id) {
		badgeRepository.findById(id)
			.map(badge -> modelMapper.map(badge, Badge.class))
			.map(badge -> {
				badgeRepository.deleteById(badge.getId());
				signalDeleteEvent(badge);
				return badge;
			})
			.orElseThrow(() -> new BadgeNotFoundException(id));
	}
	
	private void signalDeleteEvent(Badge badge) {
		if(eventHandler != null) {
			eventHandler.sendBadgeDeleteEvent(badge);
		}
	}
	

}
