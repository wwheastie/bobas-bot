package com.bobasalliance.bobasbot.commands.database;

import java.sql.Time;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("payoutTime")
public class PayoutTimeDao {
	@Id
	@Column("userName")
	private String userName;

	@Column("channelID")
	private String channelId;

	@Column("payoutTime")
	private Time payoutTime;

	@Column("flag")
	private String flag;

	@Column("swgohggLink")
	private String swgohggLink;

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(final String channelId) {
		this.channelId = channelId;
	}

	public Time getPayoutTime() {
		return payoutTime;
	}

	public void setPayoutTime(final Time payoutTime) {
		this.payoutTime = payoutTime;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(final String flag) {
		this.flag = flag;
	}

	public String getSwgohggLink() {
		return swgohggLink;
	}

	public void setSwgohggLink(final String swgohggLink) {
		this.swgohggLink = swgohggLink;
	}
}
