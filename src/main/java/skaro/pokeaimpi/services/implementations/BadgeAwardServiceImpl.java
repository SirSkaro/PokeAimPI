package skaro.pokeaimpi.services.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skaro.pokeaimpi.repository.BadgeAwardRepository;
import skaro.pokeaimpi.repository.BadgeRepository;
import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.BadgeAwardEntity;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.BadgeAwardService;
import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;
import skaro.pokeaimpi.web.exceptions.BadgeNotAwardableException;
import skaro.pokeaimpi.web.exceptions.BadgeNotFoundException;
import skaro.pokeaimpi.web.exceptions.SocialConnectionNotFoundException;

@Service
public class BadgeAwardServiceImpl implements BadgeAwardService {

	@Autowired
	private BadgeAwardRepository awardRepository;
	@Autowired
	private BadgeRepository badgeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<BadgeAwardDTO> getAll() {
		return awardRepository.findAll()
				.stream()
				.map(award -> modelMapper.map(award, BadgeAwardDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<BadgeAwardDTO> getByBadgeId(Integer id) {
		return awardRepository.findByBadgeId(id)
				.stream()
				.map(award -> modelMapper.map(award, BadgeAwardDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<BadgeAwardDTO> getByUserId(Integer id) {
		return awardRepository.findByUserId(id)
				.stream()
				.map(award -> modelMapper.map(award, BadgeAwardDTO.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<BadgeAwardDTO> getByUserDiscordId(Long discordId) {
		return awardRepository.findByUserDiscordIdSortThresholdDesc(discordId)
				.stream()
				.map(award -> modelMapper.map(award, BadgeAwardDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<BadgeAwardDTO> getByBadgeIdAndUserId(Integer userId, Integer badgeId) {
		return awardRepository.findByBadgeIdAndUserId(badgeId, userId)
				.map(award -> modelMapper.map(award, BadgeAwardDTO.class));
	}
	
	@Override
	public BadgeAwardDTO addBadgeAward(Long userDiscordId, Long discordRoleId) {
		BadgeEntity badge = badgeRepository.getByDiscordRoleId(discordRoleId)
				.orElseThrow(() -> new BadgeNotFoundException(discordRoleId));
		
		if(badge.getCanBeEarnedWithPoints()) {
			throw new BadgeNotAwardableException(badge);
		}
		
		UserEntity user = userRepository.getByDiscordId(userDiscordId)
				.orElseThrow(() -> new SocialConnectionNotFoundException(userDiscordId));
		
		BadgeAwardEntity badgeAward = awardRepository.save(new BadgeAwardEntity(user, badge));
		return modelMapper.map(badgeAward, BadgeAwardDTO.class);
	}

	@Override
	public List<BadgeAwardDTO> getByBadgeDiscordRoleId(Long discordId) {
		return awardRepository.findByBadgeDiscordRoleId(discordId)
				.stream()
				.map(award -> modelMapper.map(award, BadgeAwardDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<BadgeAwardDTO> getByDiscordRoleIdAndUserDiscordId(Long discordRoleId, Long userDiscordId) {
		return awardRepository.findByBadgeDiscordRoleIdAndUserDiscordId(discordRoleId, userDiscordId)
				.map(award -> modelMapper.map(award, BadgeAwardDTO.class));
	}

}
