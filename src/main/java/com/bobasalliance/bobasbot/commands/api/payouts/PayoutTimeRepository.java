package com.bobasalliance.bobasbot.commands.api.payouts;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PayoutTimeRepository extends CrudRepository<PayoutTimeDao, String> {
	@Query("SELECT * FROM payoutTime p WHERE p.channelId = :channelId ORDER BY p.payoutTime, p.userName")
	List<PayoutTimeDao> findAllByChannelIdOrderByPayoutTimeAndUserName(@Param("channelId") String channelId);

	@Query("SELECT * FROM payoutTime p WHERE p.channelId = :channelId AND p.userName = :userName")
	Optional<PayoutTimeDao> findByChannelIdAndUserName(@Param("channelId") String channelId, @Param("userName") String userName);

	@Modifying
	@Query("INSERT INTO payoutTime VALUES (:channelId, :userName, :payoutTime, :flag, :swgohggLink)")
	void savePayoutTime(String channelId, String userName, Time payoutTime, String flag, String swgohggLink);

	@Modifying
	@Query("DELETE FROM payoutTime p WHERE p.channelId = :channelId AND p.userName = :userName")
	void deleteByChannelIdAndUserName(@Param("channelId") String channelId, @Param("userName") String userName);
}
