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
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.services.BadgeAwardService;
import skaro.pokeaimpi.web.dtos.NewAwardsDTO;
import skaro.pokeaimpi.web.dtos.BadgeDTO;
import skaro.pokeaimpi.web.dtos.UserDTO;
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
	public List<NewAwardsDTO> getAll() {
		return awardRepository.findAll()
				.stream()
				.map(award -> modelMapper.map(award, NewAwardsDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<NewAwardsDTO> getByBadgeId(Integer id) {
		return awardRepository.findByBadgeId(id)
				.stream()
				.map(award -> modelMapper.map(award, NewAwardsDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<NewAwardsDTO> getByUserId(Integer id) {
		return awardRepository.findByUserId(id)
				.stream()
				.map(award -> modelMapper.map(award, NewAwardsDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<NewAwardsDTO> getByBadgeIdAndUserId(Integer userId, Integer badgeId) {
		return awardRepository.findByBadgeIdAndUserId(badgeId, userId)
				.map(award -> modelMapper.map(award, NewAwardsDTO.class));
	}

	@Override
	public NewAwardsDTO addBadgeAwards(NewAwardsDTO badgeAward) {
		UserEntity user = modelMapper.map(badgeAward.getUser(), UserEntity.class);
		List<BadgeEntity> badges = extractBadgeEntitiesOfAwardDTO(badgeAward);
		
		List<BadgeAwardEntity> awardEntities = saveBadgeAwards(user, badges);
		return createAwardDTO(badgeAward.getUser(), awardEntities);
	}
	
	@Override
	public NewAwardsDTO addBadgeAward(Long discordUserId, Long discordRoleId) {
		UserEntity user = userRepository.findByDiscordId(discordUserId)
				.orElseThrow(() -> new SocialConnectionNotFoundException(discordUserId));
		BadgeEntity badge = badgeRepository.getByDiscordRoleId(discordRoleId)
				.orElseThrow(() -> new BadgeNotFoundException(discordRoleId));
		
		BadgeAwardEntity badgeAward = awardRepository.save(new BadgeAwardEntity(user, badge));
		return modelMapper.map(badgeAward, NewAwardsDTO.class);
	}
	
	private NewAwardsDTO createAwardDTO(UserDTO user, List<BadgeAwardEntity> awardEntities) {
		NewAwardsDTO resultDTO = new NewAwardsDTO();
		resultDTO.setUser(user);
		resultDTO.setBadges(extractBadgeDTOsOfAwardEntities(awardEntities));
		return resultDTO;
	}
	
	private List<BadgeEntity> extractBadgeEntitiesOfAwardDTO(NewAwardsDTO badgeAwardDTO) {
		return badgeAwardDTO.getBadges()
				.stream()
				.map(badgeDTO -> modelMapper.map(badgeDTO, BadgeEntity.class))
				.collect(Collectors.toList());
	}
	
	private List<BadgeAwardEntity> saveBadgeAwards(UserEntity user, List<BadgeEntity> badges) {
		return badges.stream()
				.map(badge -> createAwardEntity(user, badge))
				.map(award -> awardRepository.save(award))
				.collect(Collectors.toList());
	}
	
	private List<BadgeDTO> extractBadgeDTOsOfAwardEntities(List<BadgeAwardEntity> awards) {
		return awards.stream()
				.map(award -> modelMapper.map(award.getBadge(), BadgeDTO.class))
				.collect(Collectors.toList());
	}
	
	private BadgeAwardEntity createAwardEntity(UserEntity user, BadgeEntity badge) {
		return EntityBuilder.of(BadgeAwardEntity::new)
				.with(BadgeAwardEntity::setUser, user)
				.with(BadgeAwardEntity::setBadge, badge)
				.build();
	}

}
