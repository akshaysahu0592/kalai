package com.tcit.vms.vms.service;

import com.tcit.vms.vms.model.Staff;
import com.tcit.vms.vms.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private StaffRepository staffRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<Staff> staffDb = staffRepository.findByEmail(email);

        if (staffDb == null || staffDb.isEmpty()) {

           throw new UsernameNotFoundException("User Not Found with Email : "+email);
        }
        else{


        Staff staff=staffDb.stream().filter(e->e.isActive()).findFirst().get();


        return UserDetailsImpl.build(staff);
    }
    }
}
