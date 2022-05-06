package api.backend.Models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class VideoResponse {

    private String title;
    private String description;
    private List<Comment> comments;
    private Director director;
    private String url;
}
