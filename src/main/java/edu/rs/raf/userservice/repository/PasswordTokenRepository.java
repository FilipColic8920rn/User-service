package edu.rs.raf.userservice.repository;

import edu.rs.raf.userservice.domain.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {

    Optional<PasswordToken> findPasswordTokenByToken(String token);

}
