package skaro.pokeaimpi.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import skaro.pokeaimpi.TestUtility;
import skaro.pokeaimpi.repository.entities.UserEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@After
	public void teardown() {
		userRepository.flush();
	}
	
	@Test
	public void getByDiscordId_shouldGetUserWithDiscordId_whenUserExists() {
		Long discordId = 1L;
		UserEntity testUser = TestUtility.createEmptyValidUserEntity();
		testUser.setDiscordId(discordId);
		
		userRepository.save(testUser);
		
		Optional<UserEntity> foundUser = userRepository.getByDiscordId(discordId);
		assertTrue(foundUser.isPresent());
		assertEquals(discordId, foundUser.get().getDiscordId());
		assertNotNull(foundUser.get().getId());
		
		userRepository.delete(foundUser.get());
	}
	
	@Test
	public void getByDiscordId_shouldNotFindUser_whenUserDoesNotExist() {
		Optional<UserEntity> foundUser = userRepository.getByDiscordId(0L);
		assertFalse(foundUser.isPresent());
	}
	
}
