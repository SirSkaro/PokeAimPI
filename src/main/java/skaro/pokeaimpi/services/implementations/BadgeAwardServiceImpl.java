package skaro.pokeaimpi.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import skaro.pokeaimpi.repository.BadgeAwardRepository;
import skaro.pokeaimpi.services.BadgeAwardService;
import skaro.pokeaimpi.web.dtos.BadgeAwardDTO;

public class BadgeAwardServiceImpl implements BadgeAwardService {

	@Autowired
	private BadgeAwardRepository awardRepository;
	
	@Override
	public List<BadgeAwardDTO> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<BadgeAwardDTO> getByBadgeId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<BadgeAwardDTO> getByUserId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
