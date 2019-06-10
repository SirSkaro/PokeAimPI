package skaro.pokeaimpi.web.dtos;

import javax.validation.constraints.Positive;

public class PointsDTO {

	@Positive(message = "amount must be a positive value")
	private Integer amount;

	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}
