package com.tcit.vms.vms.service;

import com.tcit.vms.vms.exception.UserNotFoundException;
import com.tcit.vms.vms.model.Role;
import com.tcit.vms.vms.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    public Role getRoleById(Integer roleId) throws UserNotFoundException {
        Optional<Role> userOptional = roleRepository.findById(roleId);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        throw new UserNotFoundException("Role not found with id "+roleId);
    }
}
