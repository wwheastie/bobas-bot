package com.bobasalliance.bobasbot.commands.api.mods;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

@Component
public class CharacterModsMapper extends ConfigurableMapper {
	@Override
	protected void configure(final MapperFactory mapperFactory) {
		mapperFactory.classMap(CharacterModsDao.class, CharacterModsDto.class)
				.byDefault()
				.register();
	}
}
