package api.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @SequenceGenerator(
            name = "comment_sequence",
            sequenceName = "comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_sequence"
    )
    private Long id; // autoincremented

    private String comment;

    @ManyToOne
    @JoinColumn(name="video_id", nullable=false)
    private Video video;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserModel user;

}
