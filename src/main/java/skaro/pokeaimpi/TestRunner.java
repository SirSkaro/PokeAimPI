package skaro.pokeaimpi;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import skaro.pokeaimpi.messaging.MessagingConfig;
import skaro.pokeaimpi.sdk.messaging.BadgeEventMessage;
import skaro.pokeaimpi.sdk.messaging.BadgeEventType;
import skaro.pokeaimpi.sdk.resource.Badge;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Profile(MessagingConfig.MESSAGING_PROFILE)
public class TestRunner implements CommandLineRunner {

	@Autowired
	private RabbitTemplate template;
	@Autowired
	private FanoutExchange exchange;
	
	@Override
	public void run(String... args) throws Exception {
		BadgeEventMessage event = new BadgeEventMessage();
		event.setBadge(new Badge());
		event.setEventType(BadgeEventType.DELETE);
		template.convertAndSend(exchange.getName(), "", event);
	}
	

}
