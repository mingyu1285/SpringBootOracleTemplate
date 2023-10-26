package kopo.poly.service.impl;

import kopo.poly.dto.MailDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.persistance.mapper.IUserInfoMapper;
import kopo.poly.service.IMailService;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoService implements IUserInfoService {


    private final IUserInfoMapper userInfoMapper;

    private final IMailService mailService;

    @Override
    public UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + "getUserIdExists start !");

        UserInfoDTO rDTO = userInfoMapper.getUserIdExists(pDTO);

        log.info(this.getClass().getName() + ".getUserIdExists End!");
        return rDTO;
    }

    @Override
    public UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".emailAuth Start!");

        // DB 이메일이 존재하는지 SQL 쿼리 실행
        // SQL 쿼리에 COUNT()를 사용하기 때문에 반드시 조회 결과는 존재함.
        UserInfoDTO rDTO = userInfoMapper.getEmailExists(pDTO); // 이메일 존재하는지 조회

        String existsYn = CmmUtil.nvl(rDTO.getExistsYn());
        log.info("existsYn :" + existsYn);

        if (existsYn.equals("N")) {
            int authNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
            log.info("authNumber : " + authNumber);

            MailDTO dto = new MailDTO();

            dto.setTitle("이메일 중복 확인 인증번호 발송 메일");
            dto.setContents("인증번호는  " + authNumber + "입니다.");
            dto.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

            mailService.doSendMail(dto); // 이메일 발송

            dto = null;

            rDTO.setAuthNumber(authNumber); // 인증번호를 결과값에 넣어주기
        }

        log.info(this.getClass().getName() + ".emailAuth End!");
        return rDTO;
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertUserInfo Start!");

        int res = 0;
        int success = userInfoMapper.insertUserInfo(pDTO);

        if(success > 0) {
            res = 1;

            MailDTO mDTO = new MailDTO();

            mDTO.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

            mDTO.setTitle("회원가입을 축하드립니다.");

            mDTO.setContents(CmmUtil.nvl(pDTO.getUserName()));

            mailService.doSendMail(mDTO);
        } else {
            res =0;
        }
        log.info(this.getClass().getName() +".insertUserInfo End!");

        return res;



        }
    @Override
    public List<UserInfoDTO> getUserInfoList() throws Exception {
        log.info(this.getClass().getName() + ".getUserInfoList start!");

        return userInfoMapper.getUserInfoList();
    }


    @Override
    public UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() +".getUserInfo start!");
        return userInfoMapper.getUserInfo(pDTO);

    }

    @Override
    public UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + "getLogin Start ! ");

        UserInfoDTO rDTO = Optional.ofNullable(userInfoMapper.getLogin(pDTO)).orElseGet(UserInfoDTO::new);

        if(CmmUtil.nvl(rDTO.getUserId()).length() > 0 ) {
            MailDTO mDTO = new MailDTO();

            mDTO.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(rDTO.getEmail())));

            mDTO.setTitle("로그인 알림!"); // 제목

            mDTO.setContents(DateUtil.getDateTime("yyyy.MM.dd hh:mm:ss") + "에 "
            + CmmUtil.nvl(rDTO.getUserName()) + "님이 로그인하였습니다.");

            mailService.doSendMail(mDTO);
        }
    log.info(this.getClass().getName() + ".getLogin End ! ");

        return rDTO;
    }

    @Override
    public UserInfoDTO searchUserIdOrPasswordProc(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".searchUserIdOrPasswordProc Start!");

        UserInfoDTO rDTO = userInfoMapper.getUserId(pDTO);


        int authNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
        log.info("authNumber : " + authNumber);


        MailDTO dto = new MailDTO();

        dto.setTitle("이메일 중복 확인 인증번호 발송 메일");
        dto.setContents("인증번호는  " + authNumber + "입니다.");
        dto.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

        mailService.doSendMail(dto); // 이메일 발송

        dto = null;

        rDTO.setAuthNumber(authNumber);

        log.info(this.getClass().getName() + ".searchUserIdOrPasswordProc End!");


        return rDTO;
    }

    @Override
    public int newPasswordProc(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".newPasswordProc Start!");

        int success = userInfoMapper.updatePassword(pDTO);


        log.info(this.getClass().getName() + ".newPasswordProc End!");

        return success;
    }

    @Override
    public UserInfoDTO getEmailExistsT(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".emailAuth Start!");

        // DB 이메일이 존재하는지 SQL 쿼리 실행
        // SQL 쿼리에 COUNT()를 사용하기 때문에 반드시 조회 결과는 존재함.
        UserInfoDTO rDTO = userInfoMapper.getEmailExists(pDTO); // 이메일 존재하는지 조회

        String existsYn = CmmUtil.nvl(rDTO.getExistsYn());
        log.info("existsYn :" + existsYn);

        if (existsYn.equals("Y")) {
            int authNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
            log.info("authNumber : " + authNumber);

            MailDTO dto = new MailDTO();

            dto.setTitle("이메일 확인 인증번호 발송 메일");
            dto.setContents("인증번호는  " + authNumber + "입니다.");
            dto.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

            mailService.doSendMail(dto); // 이메일 발송

            dto = null;

            rDTO.setAuthNumber(authNumber); // 인증번호를 결과값에 넣어주기
        }

        log.info(this.getClass().getName() + ".emailAuth End!");
        return rDTO;
    }

}
