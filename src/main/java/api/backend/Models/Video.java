package api.backend.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
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
    private String url;
    private String category;
    private float rating;
    private int rates;

    @ManyToOne
    @JoinColumn(name="director_id", nullable=false)
    @JsonIgnore
    private Director director;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "video",cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "videoList")
    private List<UserModel> users;


}

