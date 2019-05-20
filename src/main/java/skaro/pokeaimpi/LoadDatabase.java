package skaro.pokeaimpi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;

@Configuration
public class LoadDatabase {
	
	@Bean
	CommandLineRunner initDatabase(UserRepository repository) {
		return args -> {
			
			UserEntity user1 = EntityBuilder.of(UserEntity::new)
					.with(UserEntity::setDiscordId, 1234L)
					.with(UserEntity::setPoints, 100)
					.with(UserEntity::setTwitchUserName, "pokeaim")
					.build();
			
			UserEntity user2 = EntityBuilder.of(UserEntity::new)
					.with(UserEntity::setDiscordId, 4321L)
					.with(UserEntity::setPoints, 200)
					.with(UserEntity::setTwitchUserName, "sirskaro")
					.build();
			
			repository.save(user1);
			repository.save(user2);
		};
	}
	
}
