package com.pointofsales.services;

import com.pointofsales.entity.Admin;
import com.pointofsales.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    public AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public void addAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    public void removeAdmin(Admin admin) {
        adminRepository.delete(admin);
    }

    public void updateAdmin(Admin admin) {
        admin.setId(admin.getId());
        adminRepository.save(admin);
    }
}
