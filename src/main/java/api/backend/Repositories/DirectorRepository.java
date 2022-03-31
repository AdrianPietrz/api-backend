package api.backend.Repositories;

import api.backend.Models.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {
    Optional<Director> findById(Long id);
    Optional<Director> findByName(String name);




}