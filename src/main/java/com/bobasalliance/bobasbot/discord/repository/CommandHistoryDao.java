package com.bobasalliance.bobasbot.discord.repository;

import java.sql.Timestamp;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("commandHistory")
public class CommandHistoryDao {
	@Column("command")
	private String command;

	@Column("ts")
	private Timestamp timestamp;

	@Column("userID")
	private String userId;

	@Column("userName")
	private String userName;

	@Column("serverID")
	private String serverId;

	@Column("serverName")
	private String serverName;

	@Column("serverRegion")
	private String serverRegion;

	@Column("executionTime")
	private long executionTime;

	public String getCommand() {
		return command;
	}

	public void setCommand(final String command) {
		this.command = command;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(final String serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(final String serverName) {
		this.serverName = serverName;
	}

	public String getServerRegion() {
		return serverRegion;
	}

	public void setServerRegion(final String serverRegion) {
		this.serverRegion = serverRegion;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(final long executionTime) {
		this.executionTime = executionTime;
	}
}
