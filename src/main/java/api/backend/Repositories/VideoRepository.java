package api.backend.Repositories;

import api.backend.Models.VideoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository {
    Optional<VideoModel> findById(Long id);
}
