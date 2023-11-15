package gdsc.speacher.video.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gdsc.speacher.converter.JsonToCvDtoConverter;
import gdsc.speacher.cv.repository.CvRepository;
import gdsc.speacher.domain.Member;
import gdsc.speacher.domain.NLP;
import gdsc.speacher.domain.Video;
import gdsc.speacher.domain.CV;
import gdsc.speacher.cv.dto.CvDto;
import gdsc.speacher.member.repository.MemberRepository;
import gdsc.speacher.nlp.dto.NlpDto;
import gdsc.speacher.nlp.repository.NLPRepository;
import gdsc.speacher.video.dto.VideoRes;
import gdsc.speacher.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class VideoService {

    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;
    private final CvRepository cvRepository;
    private final NLPRepository nlpRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Transactional
    public VideoRes generatePreSignUrl(String filePath,
                                     String bucketName,
                                     HttpMethod httpMethod, String title, Long id) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10); // 10분의 유효 기간

        String generatedUrl = amazonS3.generatePresignedUrl(bucket, filePath, calendar.getTime(), httpMethod).toString();


        int indexOfMp4 = generatedUrl.lastIndexOf(".mp4");
        String fileName = (indexOfMp4 != -1) ? generatedUrl.substring(0, indexOfMp4 + 4) : generatedUrl;

        Member member = memberRepository.findById(id).get();
        Video video = new Video(member, fileName, title);
        log.info("비디오 생성 후 디비 저장 {}", generatedUrl);
        Video saveVideo = videoRepository.save(video);
        VideoRes videoRes = new VideoRes(saveVideo.getId(), generatedUrl);

        return videoRes;
    }

    @Transactional
    public CvDto analyzeCv(MultipartFile file) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        log.info("flask 서버 API 호출");
        // Flask 서버 API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                "http://127.0.0.1:5000/api/predict", org.springframework.http.HttpMethod.POST, requestEntity, String.class);
        log.info("분석 완료 - 분석 결과 : {}", response.getBody());
        JsonToCvDtoConverter converter = new JsonToCvDtoConverter();
        CvDto cvDto = null;
        try {
            cvDto = converter.convert(response.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CV cv = new CV(cvDto.getCrossing_arms_count(),
                cvDto.getHands_in_pockets_count(),
                cvDto.getWalking_actions(),
                cvDto.getHand_to_face_actions(),
                cvDto.getHands_behind_back_actions());

        cvRepository.save(cv);
        log.info("Cv 분석 결과 저장 성공 {}", cv.getId());
        return cvDto;
    }

    public NlpDto analyzeNlp(MultipartFile file, Long videoId) {

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1) : null;

        if (!"mp4".equalsIgnoreCase(extension) && !"mp3".equalsIgnoreCase(extension)) {
            throw new IllegalArgumentException("Invalid file format. Please upload mp3 or mp4 file.");
        }

        // 2. 파일을 서버에 저장
        File sourceFile = new File("temp." + extension);
        try(OutputStream os = new FileOutputStream(sourceFile)) {
            os.write(file.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 3. mp4 파일일 경우 mp3로 변환
        String targetFileName = "temp.mp3";
        if ("mp4".equalsIgnoreCase(extension)) {
            convertMp4ToMp3(sourceFile.getAbsolutePath(), targetFileName);
        } else {
            targetFileName = sourceFile.getAbsolutePath();
        }
        log.info("mp3로 변환 완료");
        // 4. mp3 파일을 플라스크 서버로 전송
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(new File(targetFileName)));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        log.info("flask 서버 API 호출");
        // Flask 서버 API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                "http://127.0.0.1:5000/api/predict2", org.springframework.http.HttpMethod.POST, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> analyzeResult = null;
        try {
            analyzeResult = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 필러 워드를 JSON 형태의 문자열로 변환하여 저장
        Map<String, Integer> fillerWordMap = (Map<String, Integer>) analyzeResult.get("filler_word");
        String fillerWordJson = null;
        try {
            fillerWordJson = objectMapper.writeValueAsString(fillerWordMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        NlpDto nlpDto = new NlpDto((String)analyzeResult.get("script"), (Double) analyzeResult.get("time"), (Double) analyzeResult.get("speed"), fillerWordJson);
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new IllegalArgumentException("비디오 못찾음"));
        NLP nlp = new NLP(nlpDto.getScript(), nlpDto.getTime(), nlpDto.getSpeed(), nlpDto.getFillerWord(), video);
        nlpRepository.save(nlp);
        log.info("nlp 분석 결과 저장 성공 {}", nlp.getId());

        // 5. 임시 파일 삭제
        sourceFile.delete();
        if (!sourceFile.getAbsolutePath().equals(targetFileName)) {
            new File(targetFileName).delete();
        }

        return nlpDto;
    }

    public void convertMp4ToMp3(String mp4Source, String mp3Target) {
        try {
            ProcessBuilder pb = new
                    ProcessBuilder(
                    "ffmpeg", "-i", mp4Source, "-vn", "-ar", "44100", "-ac", "2", "-b:a", "192k", mp3Target);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }




    public List<Video> findAll(Long memberId) {
        log.info("모든 영상 최신순으로 모두 조회");
        return videoRepository.findAllByMemberIdOrderByCreateDateDesc(memberId);
    }

    public Video findById(Long videoId) {
        Video findVideo = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("영상 조회 시 오류 발생"));
        log.info("{} 비디오 영상 조회", findVideo.getTitle());
        return findVideo;
    }


}
