package api.backend.Models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table
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

    @NonNull
    private int rating;

    @NonNull
    private String comment;

    @NonNull
    private Date date;

    @NonNull
    private Long IDofUser;

    public Comment(@NonNull int rating, @NonNull String comment, @NonNull Date date, @NonNull Long IDofUser) {
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.IDofUser = IDofUser;
    }
}
