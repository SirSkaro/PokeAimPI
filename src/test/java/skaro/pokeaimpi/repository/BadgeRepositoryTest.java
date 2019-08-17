package skaro.pokeaimpi.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import skaro.pokeaimpi.TestUtility;
import skaro.pokeaimpi.repository.entities.BadgeEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class BadgeRepositoryTest {

	@Autowired
	private BadgeRepository badgeRepository;
	
	@Test
	public void getByCanBeEarnedWithPointsTrueAndPointThresholdBetween_shouldGetBadgesWithInclusiveRange_whenBadgesExist() {
		int lowerBound = 100;
		int upperBound = 150;
		BadgeEntity lowerBadge = persistBadge(lowerBound, true);
		BadgeEntity upperBadge = persistBadge(upperBound, true);
		
		List<BadgeEntity> badges = badgeRepository.getByCanBeEarnedWithPointsTrueAndPointThresholdBetween(lowerBound, upperBound);
		
		assertEquals(2, badges.size());
		
		deleteBadge(lowerBadge);
		deleteBadge(upperBadge);
	}
	
	@Test
	public void getByCanBeEarnedWithPointsTrueAndPointThresholdBetween_shouldNotGetUnearnableBadges() {
		int threshold = 20;
		BadgeEntity badge = persistBadge(threshold, false);
		
		List<BadgeEntity> badges = badgeRepository.getByCanBeEarnedWithPointsTrueAndPointThresholdBetween(threshold - 1, threshold + 1);
		
		assertEquals(0, badges.size());
		
		deleteBadge(badge);
	}
	
	@Test
	public void getFirstByCanBeEarnedWithPointsTrueAndPointThresholdGreaterThanOrderByPointThreshold_shouldGetNextBadgeWithThresholdClosestToValue() {
		int threshold = 20;
		BadgeEntity earnableBadge = persistBadge(threshold, true);
		BadgeEntity nonEarnableBadge = persistBadge(threshold + 1, false);
		Optional<BadgeEntity> badge = badgeRepository.getFirstByCanBeEarnedWithPointsTrueAndPointThresholdGreaterThanOrderByPointThreshold(threshold - 1);
		
		assertTrue(badge.isPresent());
		assertEquals(threshold, badge.get().getPointThreshold().intValue());
		
		deleteBadge(nonEarnableBadge);
		deleteBadge(earnableBadge);
	}
	
	private BadgeEntity persistBadge(int threshold, boolean earnable) {
		BadgeEntity badge = TestUtility.createEmptyValidBadgeEntity();
		badge.setPointThreshold(threshold);
		badge.setCanBeEarnedWithPoints(earnable);
		badge.setDiscordRoleId((long)threshold);
		
		return badgeRepository.save(badge);
	}
	
	private void deleteBadge(BadgeEntity badge) {
		badgeRepository.delete(badge);
	}
}
