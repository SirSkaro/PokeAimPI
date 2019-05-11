package skaro.pokeaimpi.web.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import skaro.pokeaimpi.repository.entity.SocialProfile;

@Data
@Getter
@Setter
public class UserDTO {
	private Integer points;
	private SocialProfile socialProfile;
}
