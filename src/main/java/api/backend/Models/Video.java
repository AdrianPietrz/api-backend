package api.backend.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table
public class Video {
    @Id
    @SequenceGenerator(
            name = "video_sequence",
            sequenceName = "video_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "video_sequence"
    )
    private Long id; // autoincremented
    private String title;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "video_id")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name="director_id", nullable=false)
    @JsonIgnore
    private Director director;

    public void addComment(Comment comment){
        comments.add(comment);
    }

}
