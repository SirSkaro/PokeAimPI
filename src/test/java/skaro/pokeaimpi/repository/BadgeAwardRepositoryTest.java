package skaro.pokeaimpi.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import skaro.pokeaimpi.TestUtility;
import skaro.pokeaimpi.repository.entities.BadgeAwardEntity;
import skaro.pokeaimpi.repository.entities.BadgeEntity;
import skaro.pokeaimpi.repository.entities.EntityBuilder;
import skaro.pokeaimpi.repository.entities.UserEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class BadgeAwardRepositoryTest {

	@Autowired
	private BadgeAwardRepository awardRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BadgeRepository badgeRepository;
	
	private Long discordId;
	private UserEntity testUser;
	
	@Before
	public void setup() {
		discordId = 119876786L;
		testUser = TestUtility.createEmptyValidUserEntity();
		testUser.setDiscordId(discordId);
		testUser = userRepository.save(testUser);
	}
	
	@After
	public void teardown() {
		userRepository.delete(testUser);
	}
	
	@Test
	public void findByUserDiscordIdSortThresholdDesc_shouldGetAwardsForUserInDecendingOrderOfThreshold() {
		int largestThreshold = 50;
		int middleThreshold = 40;
		int lowestThreshold = 30;
		
		BadgeAwardEntity lowestAward = persistAward(lowestThreshold, discordId);
		BadgeAwardEntity largestAward = persistAward(largestThreshold, discordId);
		BadgeAwardEntity middleAward = persistAward(middleThreshold, discordId);

		List<BadgeAwardEntity> awards = awardRepository.findByUserDiscordIdSortThresholdDesc(discordId);
		
		assertEquals(3, awards.size());
		assertEquals(largestThreshold, awards.get(0).getBadge().getPointThreshold().intValue());
		assertEquals(middleThreshold, awards.get(1).getBadge().getPointThreshold().intValue());
		assertEquals(lowestThreshold, awards.get(2).getBadge().getPointThreshold().intValue());
		
		deleteAward(lowestAward);
		deleteAward(largestAward);
		deleteAward(middleAward);
	}
	
	private BadgeAwardEntity persistAward(int threshold, long discordId) {
		BadgeEntity badge = TestUtility.createEmptyValidBadgeEntity();
		badge.setPointThreshold(threshold);
		badge.setDiscordRoleId((long)threshold);
		
		BadgeAwardEntity award = EntityBuilder.of(BadgeAwardEntity::new)
				.with(BadgeAwardEntity::setBadge, badge)
				.with(BadgeAwardEntity::setUser, testUser)
				.build();
		
		return awardRepository.save(award);
	}
	
	private void deleteAward(BadgeAwardEntity award) {
		BadgeEntity badge = award.getBadge();
		awardRepository.delete(award);
		badgeRepository.delete(badge);
	}
	
}
