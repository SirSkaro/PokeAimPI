package skaro.pokeaimpi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import skaro.pokeaimpi.repository.BadgeAwardRepository;
import skaro.pokeaimpi.repository.BadgeRepository;
import skaro.pokeaimpi.repository.UserRepository;
import skaro.pokeaimpi.repository.entities.BadgeAwardEntity;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;

@Configuration
public class LoadDatabase {
	
	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository, BadgeRepository badgeRepository, BadgeAwardRepository awardRepository) {
		return args -> {
			
			UserEntity user1 = EntityBuilder.of(UserEntity::new)
					.with(UserEntity::setDiscordId, 1234L)
					.with(UserEntity::setPoints, 0)
					.with(UserEntity::setTwitchUserName, "pokeaim")
					.build();
			
			UserEntity user2 = EntityBuilder.of(UserEntity::new)
					.with(UserEntity::setDiscordId, 4321L)
					.with(UserEntity::setPoints, 0)
					.with(UserEntity::setTwitchUserName, "sirskaro")
					.build();
			
			BadgeEntity badge1 = EntityBuilder.of(BadgeEntity::new)
					.with(BadgeEntity::setPointThreshold, 1000)
					.with(BadgeEntity::setDescription, "for 1000 points")
					.with(BadgeEntity::setTitle, "title 1")
					.with(BadgeEntity::setImageUri, "google.com")
					.with(BadgeEntity::setDiscordRoleId, 1111L)
					.build();
			
			BadgeEntity badge2 = EntityBuilder.of(BadgeEntity::new)
					.with(BadgeEntity::setPointThreshold, 2000)
					.with(BadgeEntity::setDescription, "for 2000 points")
					.with(BadgeEntity::setTitle, "title 2")
					.with(BadgeEntity::setImageUri, "youtube.com")
					.with(BadgeEntity::setDiscordRoleId, 2222L)
					.build();
			
			BadgeAwardEntity award1 = EntityBuilder.of(BadgeAwardEntity::new)
					.with(BadgeAwardEntity::setBadge, badge1)
					.with(BadgeAwardEntity::setUser, user1)
					.build();
			
			BadgeAwardEntity award2 = EntityBuilder.of(BadgeAwardEntity::new)
					.with(BadgeAwardEntity::setBadge, badge1)
					.with(BadgeAwardEntity::setUser, user2)
					.build();
			
			BadgeAwardEntity award3 = EntityBuilder.of(BadgeAwardEntity::new)
					.with(BadgeAwardEntity::setBadge, badge2)
					.with(BadgeAwardEntity::setUser, user2)
					.build();
			
			userRepository.save(user1);
			userRepository.save(user2);
			badgeRepository.save(badge1);
			badgeRepository.save(badge2);
			awardRepository.save(award1);
			awardRepository.save(award2);
			awardRepository.save(award3);
		};
	}
	
}
