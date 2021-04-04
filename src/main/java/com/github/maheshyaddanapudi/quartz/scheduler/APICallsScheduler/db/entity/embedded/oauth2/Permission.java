package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.embedded.oauth2;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.constants.Constants;
import org.springframework.context.annotation.Profile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="oauth_permission")
@Profile(Constants.EMBEDDED_OAUTH2)
public class Permission extends BaseIdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2343317760539905195L;

	private String name;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "insert_timestamp", nullable = false)
	private Date insertTimestamp;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_timestamp", nullable = false)
	private Date updateTimestamp;

	@PrePersist
	protected void onCreate() {
		updateTimestamp = insertTimestamp = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updateTimestamp = new Date();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getInsertTimestamp() {
		return insertTimestamp;
	}

	public void setInsertTimestamp(Date insertTimestamp) {
		this.insertTimestamp = insertTimestamp;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
}
