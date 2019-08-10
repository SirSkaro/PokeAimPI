package skaro.pokeaimpi.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import skaro.pokeaimpi.PokeAimPIConfig;
import skaro.pokeaimpi.repository.entities.BadgeAwardEntity;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration(classes= { PokeAimPIConfig.class} )
@EnableAutoConfiguration(exclude = { JpaRepositoriesAutoConfiguration.class })
@AutoConfigureTestDatabase(replace=Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BadgeAwardRepositoryTest {

	@Autowired
    private TestEntityManager entityManager;
	@Autowired
	private BadgeAwardRepository awardRepository;
	
	@Test
	public void findByUserDiscordIdOrderByBadgePointThresholdDesc_shouldGetAwardsForUserInDecendingOrderOfThreshold() {
		long discordId = 1L;
		int largestThreshold = 50;
		int middleThreshold = 40;
		int lowestThreshold = 30;
		persistAward(lowestThreshold, discordId);
		persistAward(largestThreshold, discordId);
		persistAward(middleThreshold, discordId);
		
		List<BadgeAwardEntity> awards = awardRepository.findByUserDiscordIdOrderByBadgePointThresholdDesc(discordId);
		
		assertEquals(3,awards.size());
		assertEquals(largestThreshold, awards.get(0).getBadge().getPointThreshold().intValue());
		assertEquals(middleThreshold, awards.get(1).getBadge().getPointThreshold().intValue());
		assertEquals(lowestThreshold, awards.get(2).getBadge().getPointThreshold().intValue());
	}
	
	private void persistAward(int threshold, long discordId) {
		UserEntity user = EntityBuilder.of(UserEntity::new)
				.with(UserEntity::setDiscordId, discordId)
				.build();
		
		BadgeEntity badge = EntityBuilder.of(BadgeEntity::new)
				.with(BadgeEntity::setPointThreshold, threshold)
				.build();
		
		BadgeAwardEntity award = EntityBuilder.of(BadgeAwardEntity::new)
				.with(BadgeAwardEntity::setBadge, badge)
				.with(BadgeAwardEntity::setUser, user)
				.build();
		
		entityManager.persist(badge);
		entityManager.persist(user);
		entityManager.persist(award);
		entityManager.flush();
	}
	
}
