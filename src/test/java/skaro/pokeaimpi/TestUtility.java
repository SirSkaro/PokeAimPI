package skaro.pokeaimpi;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    
}
