package skaro.pokeaimpi;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.web.dtos.DiscordConnection;
import skaro.pokeaimpi.web.dtos.SocialProfile;
import skaro.pokeaimpi.web.dtos.TwitchConnection;
import skaro.pokeaimpi.web.dtos.UserDTO;

public class TestUtility {
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	 
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
    
    public static UserDTO createEmptyUserDTO() {
    	UserDTO result = new UserDTO();
		SocialProfile profile = new SocialProfile();
		DiscordConnection discordConnection = new DiscordConnection();
		TwitchConnection twitchConnection = new TwitchConnection();
		profile.setDiscordConnection(discordConnection);
		profile.setTwitchConnection(twitchConnection);
		result.setSocialProfile(profile);
		result.setPoints(0);
		
		return result;
    }
    
    public static UserEntity createEmptyValidUserEntity() {
    	return EntityBuilder.of(UserEntity::new)
    			.with(UserEntity::setPoints, 0)
    			.build();
    }
    
    public static BadgeEntity createEmptyValidBadgeEntity() {
    	return EntityBuilder.of(BadgeEntity::new)
				.with(BadgeEntity::setCanBeEarnedWithPoints, false)
				.with(BadgeEntity::setPointThreshold, 0)
				.with(BadgeEntity::setDescription, "test description")
				.with(BadgeEntity::setImageUri, "/dummypath/test.png")
				.with(BadgeEntity::setTitle, "test badge")
				.with(BadgeEntity::setDiscordRoleId, 0L)
				.build();
    }
    
}
