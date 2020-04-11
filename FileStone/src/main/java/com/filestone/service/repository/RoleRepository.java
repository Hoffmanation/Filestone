package com.filestone.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.filestone.entity.Role;

/**
 * A Jpa-Repository-Interface for the {@link Role} DAO-Layer service
 * @author Hoffman
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long>{
}
