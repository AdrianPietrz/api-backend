package api.backend.Models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DirectorResponse {

    private String name;
    private List<Video> videos;


}
