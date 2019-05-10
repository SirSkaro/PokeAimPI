package skaro.pokeaimpi.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDTO {
	private Integer points;
	private SocialProfile socialProfile;
}
