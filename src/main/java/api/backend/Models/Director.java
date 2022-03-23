package api.backend.Models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "director_id")
    private List<Video> videos;

}
