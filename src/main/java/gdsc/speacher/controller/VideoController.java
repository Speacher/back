package gdsc.speacher.controller;

import gdsc.speacher.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class VideoController {

    private VideoService videoService;
}
