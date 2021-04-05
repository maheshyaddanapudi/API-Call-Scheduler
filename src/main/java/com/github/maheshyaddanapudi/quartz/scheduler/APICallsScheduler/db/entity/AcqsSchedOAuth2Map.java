package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity;

import lombok.*;

import javax.persistence.Entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity(name = "ACQS_SCHED_OAUTH2_MAP")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AcqsSchedOAuth2Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_mapping_id", updatable = false, nullable = false)
    private Long authMappingId;

    @OneToOne
    @JoinColumn(name = "mappingId")
    @Fetch(FetchMode.SELECT)
    private AcqsSchedMap acqsSchedMap;

    @Column(name = "oauth2_token_url", nullable = false)
    private String oauth2TokenUrl;

    @Column(name = "oauth2_user_info_url")
    private String oauth2UserInfoUrl;

    @Column(name = "oauth2_grant_type")
    private String oauth2GrantType;

    @Column(name = "oauth2_client_id", nullable = false, length = 150)
    private String oauth2ClientId;

    @Column(name = "oauth2_client_secret_encrypted", nullable = false, length = 500)
    private String oauth2ClientSecretEncrypted;

    @Column(name = "oauth2_username", nullable = false)
    private String oauth2Username;

    @Column(name = "oauth2_password_encrypted", nullable = false, length = 500)
    private String oauth2PasswordEncrypted;

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
}
