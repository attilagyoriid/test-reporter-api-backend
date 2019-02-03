package com.ericsson.eea.rv.testreporter.testreporter.services;

import com.ericsson.eea.rv.testreporter.testreporter.domain.Role;
import com.ericsson.eea.rv.testreporter.testreporter.domain.RoleType;

public interface RoleService {

    boolean isRoleTypeExist(RoleType roleType);

    Role findByRoleType(RoleType roleType);
}
