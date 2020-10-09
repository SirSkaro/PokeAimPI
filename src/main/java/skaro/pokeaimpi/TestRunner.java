package skaro.pokeaimpi;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import skaro.pokeaimpi.messaging.BadgeEventMessage;
import skaro.pokeaimpi.messaging.BadgeEventType;
import skaro.pokeaimpi.web.dtos.BadgeDTO;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class TestRunner implements CommandLineRunner {

	@Autowired
	private RabbitTemplate template;
	@Autowired
	private FanoutExchange exchange;
	
	@Override
	public void run(String... args) throws Exception {
		BadgeEventMessage event = new BadgeEventMessage();
		event.setBadge(new BadgeDTO());
		event.setEventType(BadgeEventType.DELETE);
		template.convertAndSend(exchange.getName(), "", event);
	}
	

}
