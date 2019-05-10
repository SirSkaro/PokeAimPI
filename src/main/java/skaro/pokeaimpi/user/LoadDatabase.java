package skaro.pokeaimpi.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
	
	@Bean
	CommandLineRunner initDatabase(UserRepository repository) {
		return args -> {
			
			UserEntity user1 = new UserEntity();
			UserEntity user2 = new UserEntity();
			SocialProfile profile1 = new SocialProfile();
			SocialProfile profile2 = new SocialProfile();
			
			user1.setPoints(100);
			DiscordConnection discord = new DiscordConnection();
			TwitchConnection twitch = new TwitchConnection();
			discord.setDiscordId(1234L);
			twitch.setUserName("pokeaim");
			profile1.setDiscordConnection(discord);
			profile1.setTwitchConnection(twitch);
			user1.setSocialProfile(profile1);
			
			user2.setPoints(200);
			discord = new DiscordConnection();
			twitch = new TwitchConnection();
			discord.setDiscordId(4321L);
			twitch.setUserName("sirskaro");
			profile2.setDiscordConnection(discord);
			profile2.setTwitchConnection(twitch);
			user2.setSocialProfile(profile2);
			
			repository.save(user1);
			repository.save(user2);
		};
	}
	
}
