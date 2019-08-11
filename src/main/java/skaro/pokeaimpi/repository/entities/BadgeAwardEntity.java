package skaro.pokeaimpi.repository.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "badge_award")
public class BadgeAwardEntity implements PokeAimPIEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	private UserEntity user;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	private BadgeEntity badge;

	@Column(name = "awarded_on", nullable = false)
	private Date awardDate;
	
	public BadgeAwardEntity() {
		
	}
	
	public BadgeAwardEntity(UserEntity user, BadgeEntity badge) {
		this.user = user;
		this.badge = badge;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public BadgeEntity getBadge() {
		return badge;
	}
	public void setBadge(BadgeEntity badge) {
		this.badge = badge;
	}
	public Date getRewardDate() {
		return awardDate;
	}
	
	@PrePersist
	void createRewardDate() {
		awardDate = new Date();
	}
}
