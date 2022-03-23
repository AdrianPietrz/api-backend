package api.backend.Models;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@RequiredArgsConstructor
public class UserModel {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id; // autoincremented
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String password; // Should be encoded
    @NonNull
    private String role;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<CommentModel> comments;

    public void addComment(CommentModel comment){
        comments.add(comment);
    }
}
