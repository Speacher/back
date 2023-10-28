package gdsc.speacher.controller;

import gdsc.speacher.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class VideoController {
    private VideoService videoService;
}
