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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import skaro.pokeaimpi.PokeAimPIConfig;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.entities.EntityBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration(classes= { PokeAimPIConfig.class} )
@EnableAutoConfiguration(exclude = { JpaRepositoriesAutoConfiguration.class })
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class BadgeRepositoryTest {

	@Autowired
    private TestEntityManager entityManager;
	@Autowired
	private BadgeRepository badgeRepository;
	
	@Test
	public void getByCanBeEarnedWithPointsTrueAndPointThresholdBetween_shouldGetBadgesWithInclusiveRange_whenBadgesExist() {
		int lowerBound = 100;
		int upperBound = 150;
		BadgeEntity badge1 = createEarnableBadgeWithThreshold(lowerBound);
		BadgeEntity badge2 = createEarnableBadgeWithThreshold(upperBound);
		entityManager.persist(badge1);
		entityManager.persist(badge2);
		entityManager.flush();
		
		List<BadgeEntity> badges = badgeRepository.getByCanBeEarnedWithPointsTrueAndPointThresholdBetween(100, 150);
		
		assertEquals(2, badges.size());
	}
	
	@Test
	public void getByCanBeEarnedWithPointsTrueAndPointThresholdBetween_shouldNotGetUnearnableBadges() {
		int threshold = 100;
		BadgeEntity badge = new BadgeEntity();
		badge.setPointThreshold(threshold);
		entityManager.persist(badge);
		entityManager.flush();
		
		List<BadgeEntity> badges = badgeRepository.getByCanBeEarnedWithPointsTrueAndPointThresholdBetween(threshold - 1, threshold + 1);
		
		assertEquals(0, badges.size());
	}
	
	private BadgeEntity createEarnableBadgeWithThreshold(int threshold) {
		return EntityBuilder.of(BadgeEntity::new)
				.with(BadgeEntity::setCanBeEarnedWithPoints, true)
				.with(BadgeEntity::setPointThreshold, threshold)
				.build();
	}
	
}
