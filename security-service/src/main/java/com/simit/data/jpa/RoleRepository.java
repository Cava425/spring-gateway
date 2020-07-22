package com.simit.data.jpa;

import com.simit.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
