package skaro.pokeaimpi;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.web.dtos.UserDTO;

@Configuration
public class PokeAimPIConfig {
	
	@Bean("modelMapper")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public ModelMapper getUserModelMapper() {
		
		PropertyMap<UserEntity, UserDTO> userEntityToDTOMapping = new PropertyMap<UserEntity, UserDTO>() {
			@Override
			protected void configure()
			{
				map().getSocialProfile().getDiscordConnection().setDiscordId(source.getDiscordId());
				map().getSocialProfile().getTwitchConnection().setUserName(source.getTwitchUserName());
			}
		};
		
		PropertyMap<UserDTO, UserEntity> userDTOToEntityMapping = new PropertyMap<UserDTO, UserEntity>() {
			@Override
			protected void configure()
			{
				map().setDiscordId(source.getSocialProfile().getDiscordConnection().getDiscordId());
				map().setTwitchUserName(source.getSocialProfile().getTwitchConnection().getUserName());
			}
		};
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(userEntityToDTOMapping);
		modelMapper.addMappings(userDTOToEntityMapping);
		
		return modelMapper;
	}
	
}
