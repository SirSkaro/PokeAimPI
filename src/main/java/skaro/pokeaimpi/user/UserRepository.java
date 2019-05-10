package skaro.pokeaimpi.user;

import org.springframework.data.repository.CrudRepository;

import skaro.pokeaimpi.user.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {

}
