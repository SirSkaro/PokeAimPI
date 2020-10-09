package skaro.pokeaimpi.messaging;

import javax.validation.Valid;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import skaro.pokeaimpi.services.BadgeMessageService;
import skaro.pokeaimpi.services.implementations.BadgeMessageServiceImpl;

@Configuration
@Profile("pub-sub")
public class MessagingConfig {
	private static final String BADGE_QUEUE_BEAN = "badgeQueue";
	private static final String BADGE_BINDING_BEAN = "badgeBinding";
	
	
	@Bean
	@ConfigurationProperties("skaro.pokeaimpi.messaging.queue")
	@Valid
	public MessagingProperties getMessagingProperties() {
		return new MessagingProperties();
	}
	
	@Bean(BADGE_QUEUE_BEAN)
	@Autowired
	public Queue getBadgesQueue(MessagingProperties properties) {
		return new Queue(properties.getBadges());
	}
	
	@Bean
	@Autowired
	public FanoutExchange getFanoutExchange(MessagingProperties properties) {
		return new FanoutExchange(properties.getFanout());
	}
	
	@Bean(BADGE_BINDING_BEAN)
	@Autowired
	public Binding getBadgeBinding(FanoutExchange exchange, @Qualifier(BADGE_QUEUE_BEAN) Queue queue) {
		return BindingBuilder.bind(queue).to(exchange);
	}
	
	@Bean
	public BadgeMessageService getBadgeMessageService() {
		return new BadgeMessageServiceImpl();
	}
	
}
