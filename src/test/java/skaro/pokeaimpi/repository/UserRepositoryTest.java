package skaro.pokeaimpi.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

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
import skaro.pokeaimpi.repository.entities.UserEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration(classes= { PokeAimPIConfig.class} )
@EnableAutoConfiguration(exclude = { JpaRepositoriesAutoConfiguration.class })
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class UserRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void getByDiscordId_shouldGetUserWithDiscordId_whenUserExists() {
		UserEntity user = new UserEntity();
		Long discordId = 1L;
		user.setDiscordId(discordId);
		entityManager.persist(user);
		entityManager.flush();
		
		Optional<UserEntity> foundUser = userRepository.getByDiscordId(discordId);
		assertTrue(foundUser.isPresent());
		assertEquals(discordId, foundUser.get().getDiscordId());
	}
	
	@Test
	public void getByDiscordId_shouldNotFindUser_whenUserDoesNotExist() {
		Optional<UserEntity> foundUser = userRepository.getByDiscordId(0L);
		assertFalse(foundUser.isPresent());
	}
	
}
