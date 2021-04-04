package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.external.oauth;

import java.util.Arrays;

public class RealmAccess {

    private String[] roles;

    public RealmAccess(String[] roles) {
        this.roles = roles;
    }

    public RealmAccess() {
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "RealmAccess{" +
                "roles=" + Arrays.toString(roles) +
                '}';
    }
}
