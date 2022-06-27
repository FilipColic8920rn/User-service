package edu.rs.raf.userservice.repository;

import edu.rs.raf.userservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(String name);
    Optional<Role> findRoleById(Long id);
}
