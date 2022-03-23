package api.backend.Repositories;

import api.backend.Models.Video;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository{
    Optional<Video> findById(Long id);
}
