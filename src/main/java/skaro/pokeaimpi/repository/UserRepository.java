package skaro.pokeaimpi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import skaro.pokeaimpi.repository.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}
