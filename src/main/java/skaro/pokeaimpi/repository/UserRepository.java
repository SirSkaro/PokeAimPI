package skaro.pokeaimpi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import skaro.pokeaimpi.repository.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}
