package gdsc.speacher.service;

import gdsc.speacher.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoService {

    private VideoRepository videoRepository;
}
