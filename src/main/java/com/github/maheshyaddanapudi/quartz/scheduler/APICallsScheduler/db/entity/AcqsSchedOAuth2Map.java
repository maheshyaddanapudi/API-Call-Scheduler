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
    @Column(name = "AUTH_MAPPING_ID", updatable = false, nullable = false)
    private Long authMappingId;

    @OneToOne
    @JoinColumn(name = "mappingId")
    @Fetch(FetchMode.SELECT)
    private AcqsSchedMap acqsSchedMap;

    @Column(name = "OAUTH2_TOKEN_URL", nullable = false)
    private String oauth2TokenUrl;

    @Column(name = "OAUTH2_USER_INFO_URL")
    private String oauth2UserInfoUrl;

    @Column(name = "OAUTH2_GRANT_TYPE")
    private String oauth2GrantType;

    @Column(name = "OAUTH2_CLIENT_ID", nullable = false, length = 150)
    private String oauth2ClientId;

    @Column(name = "OAUTH2_CLIENT_SECRET_ENCRYPTED", nullable = false, length = 500)
    private String oauth2ClientSecretEncrypted;

    @Column(name = "OAUTH2_USERNAME", nullable = false)
    private String oauth2Username;

    @Column(name = "OAUTH2_PASSWORD_ENCRYPTED", nullable = false, length = 500)
    private String oauth2PasswordEncrypted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INSERT_TIMESTAMP", nullable = false)
    private Date insertTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIMESTAMP", nullable = false)
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
