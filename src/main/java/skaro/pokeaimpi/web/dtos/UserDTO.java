package skaro.pokeaimpi.web.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import skaro.pokeaimpi.repository.entities.SocialProfile;

@Data
@Getter
@Setter
public class UserDTO {
	private Integer points;
	private SocialProfile socialProfile;
}
