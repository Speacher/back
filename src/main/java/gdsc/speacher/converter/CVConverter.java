package gdsc.speacher.converter;

import gdsc.speacher.cv.dto.CvDto;
import gdsc.speacher.domain.CV;
import gdsc.speacher.domain.Video;

public class CVConverter {

    public static CV cvDtoToCV(Video video, CvDto cvDto) {
        return  new CV(video,
                cvDto.getCrossing_arms_count(),
                cvDto.getHands_in_pockets_count(),
                cvDto.getWalking_actions(),
                cvDto.getHand_to_face_actions(),
                cvDto.getHands_behind_back_actions());
    }
}
