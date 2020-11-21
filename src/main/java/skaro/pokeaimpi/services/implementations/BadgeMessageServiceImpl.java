package skaro.pokeaimpi.services.implementations;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import skaro.pokeaimpi.messaging.MessagingConfig;
import skaro.pokeaimpi.sdk.messaging.BadgeEventMessage;
import skaro.pokeaimpi.sdk.messaging.BadgeEventType;
import skaro.pokeaimpi.sdk.resource.Badge;
import skaro.pokeaimpi.services.BadgeMessageService;

@Service
@Profile(MessagingConfig.MESSAGING_PROFILE)
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
