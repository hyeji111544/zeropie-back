package kr.co.zeroPie.service;


import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import kr.co.zeroPie.dto.PlanDTO;
import kr.co.zeroPie.dto.PlanOrderDTO;
import kr.co.zeroPie.dto.StfDTO;
import kr.co.zeroPie.entity.*;
import kr.co.zeroPie.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@RequiredArgsConstructor
@Service
public class StfService {

    private final PasswordEncoder passwordEncoder;
    private final StfRepository stfRepository;
    private final DptRepository dptRepository;
    private final RnkRepository rnkRepository;
    private final TermsRepository termsRepository;
    private final ModelMapper modelMapper;
    private final PlanRepository planRepository;
    private final PlanOrderRepository planOrderRepository;
    private final PlanStatusRepository planStatusRepository;

    private final JavaMailSender javaMailSender;



    @Value("${file.upload.path}")
    private String fileUploadPath;

    //회원 가입기능
    public Stf register(StfDTO stfDTO) {

        String encoded = passwordEncoder.encode(stfDTO.getStfPass());
        stfDTO.setStfPass(encoded);

        Stf stf = modelMapper.map(stfDTO, Stf.class);

        Map<String, Object> map = new HashMap<>();
        try {
            MultipartFile img1 = stfDTO.getThumbFile();
            if (img1.getOriginalFilename() != null && img1.getOriginalFilename() != "") {
                StfDTO uploadedImage = uploadReviewImage(img1);
                log.info("여기는 첫번째 if.......");

                if (uploadedImage != null) {

                    StfDTO imageDTO = uploadedImage;

                    stfDTO.setSName(uploadedImage.getSName());
                    stfDTO.setStfImg(uploadedImage.getSName());

                    log.info("여기는 2번째 if.......");
                    log.info("imageDTO의 sName : " + stfDTO.getSName());
                    log.info("imageDTO의 stfImg : " + stfDTO.getStfImg());
                }

                Dpt compleDtp = stfRepository.dptSelect(stfDTO);//선택한 부서에 맞는 db에 저장된 부서값

                String idCode = compleDtp.getDptCode();//아이디가 될 코드

                //Rnk compleRnk = stfRepository.rnkSelect(stfDTO);

                String id = idCode+ randomNumber();

                stfDTO.setStfNo(id);


                log.info("stfDTO : " + stfDTO);

                Stf stf1 = modelMapper.map(stfDTO, Stf.class);

                log.info("stf1 : " + stf1);

                stf1.setPlanStatusNo(1);

                stf1.setStfStatus("Active");

                Stf savedUser = stfRepository.save(stf1);

                log.info("save 이후 : " + savedUser);

                StfDTO stfDTO1 = uploadedImage;//파일 저장경로 설정

                return savedUser;
            }
        } catch (Exception e) {
            return stf;
        }
        return stf;
    }


    //아이디 뒤에 붙는 랜덤숫자 출력해주는곳
    public String randomNumber() {
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        return Integer.toString(randomNumber);
    }


    //프로필 이미지 저장
    public StfDTO uploadReviewImage(MultipartFile file) {
        // 파일을 저장할 경로 설정

        String path = new java.io.File(fileUploadPath).getAbsolutePath();

        if (!file.isEmpty()) {
            try {
                // 원본 파일 이름과 확장자 추출
                String originalFileName = file.getOriginalFilename();//원본 파일 네임
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

                log.info("uploadReviewImage - originalFileName :  잘 들어오나요? : " + originalFileName);

                // 저장될 파일 이름 생성
                String sName = UUID.randomUUID().toString() + extension;//변환된 파일 이름


                log.info("파일 변환 후 이름 - sName : " + sName);

                // 파일 저장 경로 설정
                java.io.File dest = new File(path, sName);

                Thumbnails.of(file.getInputStream())
                        .size(140, 140)//여기를 size에서 forceSize로 강제 사이즈 변환
                        .toFile(dest);


                log.info("service - dest : " + dest);

                // 리뷰이미지 정보를 담은 DTO 생성 및 반환
                return StfDTO.builder()
                        .oName(originalFileName)
                        .sName(sName)
                        .build();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null; // 업로드 실패 시 null 반환
    }


    //부서목록 return
    public List<Dpt> findPosition() {


        List<Dpt> findPosition = dptRepository.findAll();//부서목록을 모두 출력

        return findPosition;
    }

    //부서목록 return
    public List<Rnk> findRnk() {

        List<Rnk> findRnk = rnkRepository.findAll();//부서목록을 모두 출력

        return findRnk;
    }

    //email이 중복인지 체크(같은 이메일이 몇개인지 체크)
    public int findStf(String email) {

        return stfRepository.countByStfEmail(email);
    }

    //이메일 보내기 서비스
    @Value("${spring.mail.username}")//이메일 보내는 사람 주소
    private String sender;

    //인증코드를 전송
    public long sendEmailCode(String receiver) {//void->int

        log.info("sender={}", sender);

        //MimeMessage 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        //인증코드 생성 후 세션 저장
        String code12 = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);

        log.info("생성된 code : " + code);


        //redis 사용
        //ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        //valueOperations.set("code1",code12);
        long savedCode = ((long) (code + code) * code) - 1; // long 타입으로 변경(숫자가 너무커서 오버플로우가 일어남(-로 표기))

        log.info("savedCode={}", savedCode);

        String title = "zeroPie 인증코드 입니다.";
        String content = "<h1>인증코드는 " + code + "입니다.<h1>";

        try {
            message.setSubject(title);
            message.setFrom(new InternetAddress(sender, "보내는 사람", "UTF-8"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");

            javaMailSender.send(message);

            return savedCode;

        } catch (Exception e) {
            log.error("error={}", e.getMessage());

            return 0;
        }
    }

    //약관 찾기
    public List<Terms> findTerms(){

        List<Terms> termsList= termsRepository.findAll();

        return termsList;

    }
    
    //아이디 찾기
public String findId(String email,String name){

    log.info("email2 : "+email);
    log.info("name2 : "+name);

    Stf stf =  stfRepository.findIdByStfEmailAndStfName(email,name);



    log.info("아이디 : "+stf.getStfNo());

    return stf.getStfNo();
    
}

//비밀번호 변경
public void updatePass(String id, String pass){

    String encoded = passwordEncoder.encode(pass);

    log.info("비밀번호 변경 id가 들어와? : "+id);
    log.info("비밀번호 변경 pass가 들어와? : "+pass);

    Optional<Stf> stfOpt = stfRepository.findById(id);

    Stf stf= modelMapper.map(stfOpt,Stf.class);

    log.info("비밀번호 수정전에 한번 출력 : "+stf);

    stf.setStfPass(encoded);//비밀번호 변경

    stfRepository.save(stf);

    }

    //플랜 들고오기
    public List<PlanDTO> getPlan(){
        
        log.info("StfService - getPlan 들어옴");

        List<Plan> plans = planRepository.findAll();

        List<PlanDTO> dtoList = plans.stream()
                .map(entity -> {
                    PlanDTO dto = modelMapper.map(entity, PlanDTO.class);
                    return dto;
                })
                .toList();

        return dtoList;

    }
    
    //플랜에 필요한 유저 정보 가져오기
    public StfDTO getUserInfo(String stfNo){

        Optional<Stf> userInfo = stfRepository.findById(stfNo);

        StfDTO stfDTO = modelMapper.map(userInfo,StfDTO.class);

        log.info("stfService - getUserInfo - stfDTO "+stfDTO.toString());

        return stfDTO;
    }

    //유저 수
    public long getCountUser(){

        long count = stfRepository.countByUser();

        return count;

    }

    //플랜 결제
    @Transactional
    public void planOrder(PlanOrderDTO planOrderDTO){

        log.info("stfService - PlanOrderDTO : "+planOrderDTO);

        PlanStatus planStatus = new PlanStatus(planOrderDTO.getPlanNo());

        planStatus.setPlanEdate();

        planStatusRepository.save(planStatus);//먼저 PlanStatus테이블에 저장

        log.info("저장한 planStatus 상태 보기: "+planStatus);

        planOrderDTO.setPlanStatusNo(planStatus.getPlanStatusNo());

        planOrderRepository.save(modelMapper.map(planOrderDTO,PlanOrder.class));//planOrder 테이블에 PlanStatusNo 넣어주고 저장

        log.info("stfService -planOrder- planOrderDTO"+planOrderDTO);

    }

}