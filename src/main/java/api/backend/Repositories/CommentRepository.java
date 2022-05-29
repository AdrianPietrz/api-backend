package api.backend.Repositories;

import api.backend.Models.Comment;
import api.backend.Models.Director;
import api.backend.Models.UserModel;
import api.backend.Models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long id);
    Optional<Comment> findByVideo(Video video);
    List<Comment> findAllByVideo(Video video);
    List<Comment> findAllByUser(UserModel user);


}