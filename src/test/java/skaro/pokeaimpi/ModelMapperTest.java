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
import skaro.pokeaimpi.web.dtos.DiscordConnection;
import skaro.pokeaimpi.web.dtos.SocialProfile;
import skaro.pokeaimpi.web.dtos.TwitchConnection;
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
		
		compareEntityAndDTO(entity, dto);
	}
	
	@Test
	public void userDTOShouldDeepMapToUserEntity()  {
		UserDTO dto = createUserDTO();
		UserEntity entity = userModelMapper.map(dto, UserEntity.class);
		
		compareEntityAndDTO(entity, dto);
	}
	
	private void compareEntityAndDTO(UserEntity entity, UserDTO dto) {
		assertEquals(entity.getId(), dto.getId());
		assertEquals(entity.getPoints(), dto.getPoints());
		assertEquals(entity.getTwitchUserName(), dto.getSocialProfile().getTwitchConnection().getUserName());
		assertEquals(entity.getDiscordId(), dto.getSocialProfile().getDiscordConnection().getDiscordId());
	}
	
	private UserDTO createUserDTO() {
		UserDTO result = new UserDTO();
		SocialProfile profile = new SocialProfile();
		DiscordConnection discordConnection = new DiscordConnection();
		TwitchConnection twitchConnection = new TwitchConnection();
		discordConnection.setDiscordId(1L);
		twitchConnection.setUserName("axe");
		profile.setDiscordConnection(discordConnection);
		profile.setTwitchConnection(twitchConnection);
		result.setSocialProfile(profile);
		result.setId(9);
		result.setPoints(9001);
		
		return result;
	}
	
}
