package skaro.pokeaimpi.web.dtos;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BadgeAwardDTO {

	private UserDTO user;
	private List<BadgeDTO> badges;
	private Date awardDate;
	
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	public List<BadgeDTO> getBadges() {
		return badges;
	}
	public void setBadges(List<BadgeDTO> badges) {
		this.badges = badges;
	}
	public Date getAwardDate() {
		return awardDate;
	}
	public void setAwardDate(Date awardDate) {
		this.awardDate = awardDate;
	}
	
}
