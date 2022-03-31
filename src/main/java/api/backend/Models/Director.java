package api.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Director {
    @Id
    @SequenceGenerator(
            name = "direcor_sequence",
            sequenceName = "direcor_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "direcor_sequence"
    )
    private Long id; // autoincremented

    @NonNull
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "director",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Video> videos;


    public void addDirectedVideo(Video video){
        this.videos.add(video);
    }

    public Director(@NonNull String name) {
        this.name = name;
    }
}
