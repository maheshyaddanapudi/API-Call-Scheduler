package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.repository.embedded.oauth2;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.constants.Constants;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.embedded.oauth2.OAuthRole;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile(Constants.EMBEDDED_OAUTH2)
public interface OAuthRoleRepository extends JpaRepository<OAuthRole, Integer> {
	
	OAuthRole findByName(String name);

}
