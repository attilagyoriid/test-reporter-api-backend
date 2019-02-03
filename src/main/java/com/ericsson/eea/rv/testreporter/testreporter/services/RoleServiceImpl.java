package com.ericsson.eea.rv.testreporter.testreporter.services;

import com.ericsson.eea.rv.testreporter.testreporter.domain.Role;
import com.ericsson.eea.rv.testreporter.testreporter.domain.RoleType;
import com.ericsson.eea.rv.testreporter.testreporter.exceptions.NotFoundException;
import com.ericsson.eea.rv.testreporter.testreporter.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean isRoleTypeExist(RoleType roleType) {
        return this.roleRepository.findByRoleType(roleType).isPresent();
    }

    @Override
    public Role findByRoleType(RoleType roleType) {
        return this.roleRepository.findByRoleType(roleType).orElseThrow(() -> new NotFoundException("Role not found: " + roleType));

    }



}
