package api.backend.Models;


import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@RequiredArgsConstructor
public class VideoModel {
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
    @NonNull
    private String title;
    @NonNull
    private String director;
    @NonNull
    private String genere;


}

