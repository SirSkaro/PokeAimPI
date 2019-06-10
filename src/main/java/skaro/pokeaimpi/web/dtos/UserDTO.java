package skaro.pokeaimpi.web.dtos;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
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
