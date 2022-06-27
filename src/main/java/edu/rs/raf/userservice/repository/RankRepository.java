package edu.rs.raf.userservice.repository;

import edu.rs.raf.userservice.domain.Rank;
import edu.rs.raf.userservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface RankRepository extends JpaRepository<Rank, Long> {
    Optional<Rank> findRankByName(String name);
    Optional<Rank> findRankById(Long id);
    ArrayList<Rank> findAll();
}
