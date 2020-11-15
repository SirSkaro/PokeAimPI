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
import skaro.pokeaimpi.sdk.resource.DiscordConnection;
import skaro.pokeaimpi.sdk.resource.SocialProfile;
import skaro.pokeaimpi.sdk.resource.TwitchConnection;
import skaro.pokeaimpi.sdk.resource.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= { PokeAimPIConfig.class} )
public class ModelMapperTest {
	
	@Autowired
	private ModelMapper userModelMapper;
	
	@Test
	public void userEntityShouldDeepMapToUserDTO() {
		
		UserEntity entity = EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setId, 1)
				.with(UserEntity::setDiscordId, "1234")
				.with(UserEntity::setPoints, 100)
				.with(UserEntity::setTwitchUserName, "pokeaim")
				.build();
		
		User dto = userModelMapper.map(entity, User.class);
		
		compareEntityAndDTO(entity, dto);
	}
	
	@Test
	public void userDTOShouldDeepMapToUserEntity()  {
		User dto = createUserDTO();
		UserEntity entity = userModelMapper.map(dto, UserEntity.class);
		
		compareEntityAndDTO(entity, dto);
	}
	
	private void compareEntityAndDTO(UserEntity entity, User dto) {
		assertEquals(entity.getId(), dto.getId());
		assertEquals(entity.getPoints(), dto.getPoints());
		assertEquals(entity.getTwitchUserName(), dto.getSocialProfile().getTwitchConnection().getUserName());
		assertEquals(entity.getDiscordId(), dto.getSocialProfile().getDiscordConnection().getDiscordId());
	}
	
	private User createUserDTO() {
		User result = new User();
		SocialProfile profile = new SocialProfile();
		DiscordConnection discordConnection = new DiscordConnection();
		TwitchConnection twitchConnection = new TwitchConnection();
		discordConnection.setDiscordId("1");
		twitchConnection.setUserName("axe");
		profile.setDiscordConnection(discordConnection);
		profile.setTwitchConnection(twitchConnection);
		result.setSocialProfile(profile);
		result.setId(9);
		result.setPoints(9001);
		
		return result;
	}
	
}
