package skaro.pokeaimpi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.web.dtos.UserDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= { PokeAimAPIConfig.class} )
public class ModelMapperTest {
	
	@Autowired
	private ModelMapper userModelMapper;
	
	@Test
	public void userEntityShouldDeepMapToUserDTO() {
		
		UserEntity entity = EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setId, 1)
				.with(UserEntity::setDiscordId, 1234L)
				.with(UserEntity::setPoints, 100)
				.with(UserEntity::setTwitchUserName, "pokeaim")
				.build();
		
		UserDTO dto = userModelMapper.map(entity, UserDTO.class);
		
		assertEquals(entity.getId(), dto.getId());
		assertEquals(entity.getPoints(), dto.getPoints());
		assertEquals(entity.getTwitchUserName(), dto.getSocialProfile().getTwitchConnection().getUserName());
		assertEquals(entity.getDiscordId(), dto.getSocialProfile().getDiscordConnection().getDiscordId());
	}
	
	
}
