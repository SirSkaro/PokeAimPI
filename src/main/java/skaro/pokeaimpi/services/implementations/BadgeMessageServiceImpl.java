package skaro.pokeaimpi.services.implementations;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import skaro.pokeaimpi.messaging.BadgeEventMessage;
import skaro.pokeaimpi.messaging.BadgeEventType;
import skaro.pokeaimpi.sdk.resource.Badge;
import skaro.pokeaimpi.services.BadgeMessageService;

public class BadgeMessageServiceImpl implements BadgeMessageService {

	@Autowired
	private RabbitTemplate template;
	@Autowired
	private FanoutExchange exchange;
	
	@Override
	public void sendBadgeMessage(Badge badge, BadgeEventType type) {
		BadgeEventMessage message = new BadgeEventMessage();
		message.setBadge(badge);
		message.setEventType(type);
		
		template.convertAndSend(exchange.getName(), message);
	}

}
