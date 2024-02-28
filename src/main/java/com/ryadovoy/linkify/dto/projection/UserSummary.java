package com.ryadovoy.linkify.dto.projection;

import com.ryadovoy.linkify.model.RoleName;

import java.util.Set;

public interface UserSummary {
    Long getId();
    String getEmail();
    Set<RoleSummary> getRoles();

    interface RoleSummary {
        Long getId();
        RoleName getName();
    }
}
