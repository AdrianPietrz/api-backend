package api.backend.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class VideoController {


    @RequestMapping(value = "/api/video/add", method = RequestMethod.POST)
    public ResponseEntity<?> uploadVideo(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam("file") MultipartFile file) {

        String path = "src/main/resources/" + "video" + ".mp4";
        File appFile = new File(path);
        try {
            appFile.createNewFile();
            FileOutputStream fout = new FileOutputStream(appFile);
            fout.write(file.getBytes());
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Video added!", HttpStatus.CREATED);
    }
}