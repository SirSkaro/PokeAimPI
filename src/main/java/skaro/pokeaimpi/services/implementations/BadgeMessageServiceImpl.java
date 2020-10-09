package skaro.pokeaimpi.services.implementations;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import skaro.pokeaimpi.messaging.BadgeEventMessage;
import skaro.pokeaimpi.messaging.BadgeEventType;
import skaro.pokeaimpi.services.BadgeMessageService;
import skaro.pokeaimpi.web.dtos.BadgeDTO;

public class BadgeMessageServiceImpl implements BadgeMessageService {

	@Autowired
	private RabbitTemplate template;
	@Autowired
	private FanoutExchange exchange;
	
	@Override
	public void sendBadgeMessage(BadgeDTO badge, BadgeEventType type) {
		BadgeEventMessage message = new BadgeEventMessage();
		message.setBadge(badge);
		message.setEventType(type);
		
		template.convertAndSend(exchange.getName(), message);
	}

}
