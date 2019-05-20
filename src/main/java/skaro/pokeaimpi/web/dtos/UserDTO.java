package skaro.pokeaimpi.web.dtos;

import lombok.Data;
import skaro.pokeaimpi.repository.entities.SocialProfile;

@Data
public class UserDTO {
	private Integer id;
	private Integer points;
	private SocialProfile socialProfile;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	public SocialProfile getSocialProfile() {
		return socialProfile;
	}
	public void setSocialProfile(SocialProfile socialProfile) {
		this.socialProfile = socialProfile;
	}
	
}
