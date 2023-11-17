package gdsc.speacher.video.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gdsc.speacher.config.exception.handler.FileHandler;
import gdsc.speacher.config.exception.handler.JsonHandler;
import gdsc.speacher.config.exception.handler.VideoHandler;
import gdsc.speacher.converter.CVConverter;
import gdsc.speacher.converter.JsonToCvDtoConverter;
import gdsc.speacher.converter.UrlToFileConvertor;
import gdsc.speacher.cv.repository.CvRepository;
import gdsc.speacher.domain.*;
import gdsc.speacher.cv.dto.CvDto;
import gdsc.speacher.gpt.dto.GptDto;
import gdsc.speacher.gpt.repository.GptRepository;
import gdsc.speacher.member.repository.MemberRepository;
import gdsc.speacher.nlp.dto.NlpDto;
import gdsc.speacher.nlp.repository.NLPRepository;
import gdsc.speacher.video.dto.FeedbackDto;
import gdsc.speacher.video.dto.VideoDto;
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

import java.io.*;
import java.util.*;

import static gdsc.speacher.config.code.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class VideoService {

    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;
    private final CvRepository cvRepository;
    private final NLPRepository nlpRepository;
    private final GptRepository gptRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

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
    public CvDto analyzeCv(Long videoId)  {
        File tempFile = new File("temp.mp3");
        if (tempFile.exists()) {
            tempFile.delete();
            log.info("파일이 존재하여 삭제");
        }
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new VideoHandler(INVALID_VIDEO_ID));
        log.info("{} Video 조회", video.getId());
        File file = null;
        try {
            file = UrlToFileConvertor.getFileFromURL(video.getVideoUrl());
        } catch (IOException e) {
            throw new FileHandler(URL_TO_MP4_ERROR);
        }
        log.info("VideoURL을 통해 file 불러오기 성공 {}", video.getVideoUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
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
            throw new JsonHandler(JSON_TO_CVDto_ERROR);
        }
        CV cv = CVConverter.cvDtoToCV(video, cvDto);
        cvRepository.save(cv);
        log.info("Cv 분석 결과 저장 성공 {}", cv.getId());

        CV cv2 = cvRepository.findByVideoId(videoId).orElseThrow(() -> new VideoHandler(INVALID_VIDEO_ID));
        CvDto cvDto2 = new CvDto(cv2);
        String cvJsonResult = null;
        try {
            cvJsonResult = objectMapper.writeValueAsString(cvDto2);
        } catch (JsonProcessingException e) {
            throw new JsonHandler(JSON_TO_STRING_ERROR);
        }
        NLP nlp = nlpRepository.findByVideoId(videoId).orElseThrow(() -> new VideoHandler(INVALID_VIDEO_ID));
        NlpDto nlpDto = new NlpDto(nlp);
        String nlpJsonResult = null;
        try {
            nlpJsonResult = objectMapper.writeValueAsString(nlpDto);
        } catch (JsonProcessingException e) {
            throw new JsonHandler(JSON_TO_STRING_ERROR);
        }

        Map<String, String> body2 = new HashMap<>();
        body2.put("cv_json_result", cvJsonResult);
        body2.put("nlp_json_result", nlpJsonResult);
        log.info("gpt api 호출");
        ResponseEntity<String> response2 = restTemplate.postForEntity("http://localhost:5000/api/gpt", body2, String.class);
        log.info("gpt api 호출 완료");
        GPT gpt = new GPT(response2.getBody(), video);
        gptRepository.save(gpt);
        return cvDto;
    }

    public NlpDto analyzeNlp(Long videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new VideoHandler(INVALID_VIDEO_ID));
        log.info("{} Video 조회", video.getId());

        File file = null;
        try {
            file = UrlToFileConvertor.getFileFromURL(video.getVideoUrl());
        } catch (IOException e) {
            throw new FileHandler(URL_TO_MP4_ERROR);
        }
        log.info("VideoURL을 통해 file 불러오기 성공 {}", video.getVideoUrl());

        // 1. mp4 파일일 경우 mp3로 변환
        String targetFileName = "temp.mp3";
        convertMp4ToMp3(file.getAbsolutePath(), targetFileName);
        log.info("mp3로 변환 완료");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(new File(targetFileName)));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        // 2. mp3 파일을 플라스크 서버로 전송
        RestTemplate restTemplate = new RestTemplate();
        log.info("flask 서버 API 호출");
        // Flask 서버 API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                "http://127.0.0.1:5000/api/predict2", org.springframework.http.HttpMethod.POST, requestEntity, String.class);
        log.info("flask 서버 API 리턴 완료");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> analyzeResult = null;
        try {
            analyzeResult = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new FileHandler(FILE_IO_EXCEPTION);
        }

        // 필러 워드를 JSON 형태의 문자열로 변환하여 저장
        Map<String, Integer> fillerWordMap = (Map<String, Integer>) analyzeResult.get("filler_word");
        String fillerWordJson = null;
        try {
            fillerWordJson = objectMapper.writeValueAsString(fillerWordMap);
        } catch (JsonProcessingException e) {
            throw new JsonHandler(JSON_TO_STRING_ERROR);
        }
        NlpDto nlpDto = new NlpDto((String)analyzeResult.get("script"), (Double) analyzeResult.get("time"), (Double) analyzeResult.get("speed"), fillerWordJson);
        NLP nlp = new NLP(nlpDto.getScript(), nlpDto.getTime(), nlpDto.getSpeed(), nlpDto.getFillerWord(), video);

        nlpRepository.save(nlp);
        log.info("nlp 분석 결과 저장 성공 {}", nlp.getId());

        // 5. 임시 파일 삭제
        new File(targetFileName).delete();
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
            throw new FileHandler(MP4_TO_MP3_ERROR);
        }
    }


    public List<Video> findAll(Long memberId) {
        log.info("모든 영상 최신순으로 모두 조회");
        return videoRepository.findAllByMemberIdOrderByCreateDateDesc(memberId);
    }

    public Video findById(Long videoId) {
        Video findVideo = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoHandler(VIDEO_INQUIRY_ERROR));
        log.info("{} 비디오 영상 조회", findVideo.getTitle());
        return findVideo;
    }

    public FeedbackDto findVideo(Long videoId) {
        Video findVideo = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoHandler(VIDEO_INQUIRY_ERROR));
        log.info("{} 비디오 영상 조회", findVideo.getTitle());
        VideoDto videoDto = new VideoDto(findVideo);
        CV cv = cvRepository.findByVideoId(videoId)
                .orElseThrow(() -> new VideoHandler(VIDEO_INQUIRY_ERROR));
        CvDto cvDto = new CvDto(cv);

        NLP nlp = nlpRepository.findByVideoId(videoId)
                .orElseThrow(() -> new VideoHandler(VIDEO_INQUIRY_ERROR));
        NlpDto nlpDto = new NlpDto(nlp);

        GPT gpt = gptRepository.findByVideoId(videoId)
                .orElseThrow(() -> new VideoHandler(VIDEO_INQUIRY_ERROR));
        GptDto gptDto = new GptDto(gpt);
        return new FeedbackDto(videoDto, cvDto, nlpDto, gptDto);
    }


}
