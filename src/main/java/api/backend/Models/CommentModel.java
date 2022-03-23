package api.backend.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table
public class CommentModel {
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

    public CommentModel(@NonNull int rating, @NonNull String comment, @NonNull Date date) {
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }
}
