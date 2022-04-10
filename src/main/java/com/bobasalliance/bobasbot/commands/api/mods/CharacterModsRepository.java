package com.bobasalliance.bobasbot.commands.api.mods;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterModsRepository extends CrudRepository<CharacterModsDao, String> {
	@Query("SELECT * FROM charactermods c WHERE c.baseID = :id")
	List<CharacterModsDao> findAllById(@Param("id") String id);
}
