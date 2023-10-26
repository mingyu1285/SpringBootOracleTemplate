package kopo.poly.controller;


import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequestMapping(value = "/user")  // /user로 시작되는 URL은 모두 접근 !
@RequiredArgsConstructor
@Controller
public class UserInfoController {

    private final IUserInfoService userInfoService;

    @GetMapping(value = "userRegForm")
    public String userRegForm() {
        log.info(this.getClass().getName() + ".user/userRegForm Start!");

        return "/user/userRegForm";
    }

    @ResponseBody
    @PostMapping(value = "getUserIdExists")
    public UserInfoDTO getUserExists(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".getUserIdExists Start! ");

        String userId = CmmUtil.nvl(request.getParameter("userId")); // 회원아이디
        log.info("userId : " + userId);
        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(pDTO)).orElseGet(UserInfoDTO::new);
        log.info(this.getClass().getName() + ".getUserIdExists End!");

        return rDTO;

    }

    @ResponseBody
    @PostMapping(value = "getEmailExists")
    public UserInfoDTO getEmailExists(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".getEmailExists Start!");

        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("email: " + email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setEmail(EncryptUtil.encAES128CBC(email));

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getEmailExists(pDTO)).orElseGet(UserInfoDTO::new);

        log.info(this.getClass().getName() + ".getEmailExists End!");

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "insertUserInfo")
    public MsgDTO insertUserInfo(HttpServletRequest request, HttpSession session) throws Exception {
        log.info(this.getClass().getName() + ".insertUserInfo start!");

        int res = 0;
        String msg = "";
        MsgDTO dto = null;

        UserInfoDTO pDTO = null;

        try {

            String userId = CmmUtil.nvl(request.getParameter("userId"));
            String userName = CmmUtil.nvl(request.getParameter("userName"));
            String password = CmmUtil.nvl(request.getParameter("password"));
            String email = CmmUtil.nvl(request.getParameter("email"));
            String addr1 = CmmUtil.nvl(request.getParameter("addr1"));
            String addr2 = CmmUtil.nvl(request.getParameter("addr2"));
            log.info("userId: " + userId);
            log.info("userName: " + userName);
            log.info("password:" + password);
            log.info("email: " + email);
            log.info("addr1:" + addr1);
            log.info("addr2: " + addr2);

            pDTO = new UserInfoDTO();

            pDTO.setUserId(userId);
            pDTO.setUserName(userName);

            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            pDTO.setEmail(EncryptUtil.encAES128CBC(email));
            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);

            res = userInfoService.insertUserInfo(pDTO);

            log.info("회원가입 결과(res) : " + res);

            if (res == 1) {
                msg = "회원가입되었습니다.";
            } else if (res == 2) {
                msg = "이미 가입된 아이디입니다.";
            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";
            }
        } catch (Exception e) {

            msg = "실패하였습니다. : " + e;
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);

            log.info(this.getClass().getName() + "insertUserInfo End!");
        }
        return dto;
    }

    @GetMapping(value = "userInfoList")
    public String UserInfoList(HttpSession session, ModelMap model)

            throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".userInfoList Start!");

        // 로그인된 사용자 아이디는 Session에 저장함
        // 교육용으로 아직 로그인을 구현하지 않았기 때문에 Session에 데이터를 저장하지 않았음
        // 추후 로그인을 구현할 것으로 가정하고, 공지사항 리스트 출력하는 함수에서 로그인 한 것처럼 Session 값을 생성함
//        session.setAttribute("SESSION_USER_ID", "USER01");

        // 공지사항 리스트 조회하기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        List<UserInfoDTO> rList = Optional.ofNullable(userInfoService.getUserInfoList())
                .orElseGet(ArrayList::new);


//        List<NoticeDTO> rList = noticeService.getNoticeList();
//
//        if (rList == null) {
//            rList = new ArrayList<>();
//        }

        // 조회된 리스트 결과값 넣어주기


        model.addAttribute("rList", rList);

        // 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".userInfoList End!");

        // 함수 처리가 끝나고 보여줄 JSP 파일명
        // webapp/WEB-INF/views/notice/noticeList.jsp
        return "user/userInfoList";

    }

    /**
     * @param request 가 하는게 뭐야
     * @param model
     * @return
     * @throws Exception userId 를 담아서 서비스에 넘겨주면
     */
    @GetMapping(value = "userInfo")
    public String userInfo(HttpServletRequest request, ModelMap model) throws Exception {


        log.info(this.getClass().getName() + ".UserInfo Start!");


        String userId = request.getParameter(new String("userId"));


        log.info(getClass().getName() + ".userId Start!");

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        UserInfoDTO pDTO = new UserInfoDTO();

        pDTO.setUserId(userId);


        // 공지사항 상세정보 가져오기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(pDTO))
                .orElseGet(UserInfoDTO::new);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".userInfo End!");

        // 함수 처리가 끝나고 보여줄 JSP 파일명
        // webapp/WEB-INF/views/notice/noticeInfo.jsp
        return "user/userInfo";
    }

    @GetMapping(value = "login")
    public String login() {
        log.info(this.getClass().getName() + ".user/login Start!");
        log.info(this.getClass().getName() + ".user/login End!");
        return "/user/login";
    }


    @ResponseBody
    @PostMapping(value = "loginProc")
    public MsgDTO loginProc(HttpServletRequest request, HttpSession session) {
        log.info(this.getClass().getName() + ".loginProc Start!");

        int res = 0;
        String msg = "";
        MsgDTO dto = null;

        UserInfoDTO pDTO = null;
        try {

            String userId = CmmUtil.nvl(request.getParameter("userId"));
            String password = CmmUtil.nvl(request.getParameter("password"));

            log.info("userId : " + userId);
            log.info("password: " + password);

            pDTO = new UserInfoDTO();

            pDTO.setUserId(userId);

            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            UserInfoDTO rDTO = userInfoService.getLogin(pDTO);
            if (CmmUtil.nvl(rDTO.getUserId()).length() > 0) {
                res = 1;

                msg = "로그인 성공했습니다.";
                session.setAttribute("SS_USER_ID", userId);
                session.setAttribute("SS_USER_NAME", CmmUtil.nvl(rDTO.getUserName()));


            } else {
                msg = "아이디와 비밀번호가 올바르지 않습니다.";
            }
        } catch (Exception e) {
            msg = "시스템 문제로 로그인이 실패했습니다.";
            res = 2;
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);
            log.info(this.getClass().getName() + ".loginProc End!");
        }
        return dto;
    }

    @GetMapping(value = "loginResult")
    public String loginResult() throws Exception {

        log.info(this.getClass().getName() + ".user/loginResult Start!");


        log.info(this.getClass().getName() + ".user/loginResult End!");
        return "/user/loginResult";
    }

    @GetMapping(value = "loginuser")
    public String loginuser(HttpSession session, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + "loginuser Start!");


        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        UserInfoDTO pDTO = new UserInfoDTO();

        pDTO.setUserId(userId);
//        model.addAttribute("pDTO", pDTO);
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(pDTO))
                .orElseGet(UserInfoDTO::new);


        rDTO.setUserId(userId);




        model.addAttribute("rDTO", rDTO);


        log.info(this.getClass().getName() + "loginuser End!");
        return "/user/loginuser";
    }

    @GetMapping(value = "searchUserId")
    public String searchUserId() {
        log.info(this.getClass().getName() + ".user/searchUserId Start!");

        log.info(this.getClass().getName() + ".user/searchUserId End!");
        return "user/searchUserId";
    }

    @PostMapping(value = "searchUserIdProc")
    public String searchUserIdProc(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".user/searchUserIdProc Start!");

        String userName = CmmUtil.nvl(request.getParameter("userName"));
        String email = CmmUtil.nvl(request.getParameter("email"));
        log.info("userName : " + userName);
        log.info("email : " + email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserName(userName);

        pDTO.setEmail(EncryptUtil.encAES128CBC(email));
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.searchUserIdOrPasswordProc(pDTO))
                .orElseGet(UserInfoDTO :: new);

        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".user/searchUserProc End! ");
        return "user/searchUserIdResult";
    }

    @GetMapping(value = "searchPassword")
    public String searchPassword(HttpSession session, HttpServletRequest request) {
        log.info(this.getClass().getName() + ".user/searchPassword Start!");

        session.setAttribute("NEW_PASSWORD", "");
        session.removeAttribute("NEW_PASSWORD");

        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("email: " + email);

        log.info(this.getClass().getName() + ".user/searchPassword End!");
        return "user/searchPassword";
    }

    @PostMapping(value = "searchPasswordProc")
    public String searchPasswordProc(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {
        log.info(this.getClass().getName() + ".user/searchPasswordProc Start!");


        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String userName = CmmUtil.nvl(request.getParameter("userName"));
        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("email : " + email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);
        pDTO.setUserName(userName);
        pDTO.setEmail(EncryptUtil.encAES128CBC(email));

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.searchUserIdOrPasswordProc(pDTO))
                .orElseGet(UserInfoDTO :: new);
        model.addAttribute("rDTO", rDTO);
        session.setAttribute("NEW_PASSWORD", userId);
        log.info(this.getClass().getName() + ".user/searchPasswordProc End!");
        return "user/newPassword";
    }
    @PostMapping(value = "newPasswordProc")
    public String newPasswordProc(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {
        log.info(this.getClass().getName() + ".user/newPasswordProc Start!");

        String msg = "";
        String newPassword = CmmUtil.nvl((String) session.getAttribute("NEW_PASSWORD"));

        if (newPassword.length() > 0) {
            String password = CmmUtil.nvl(request.getParameter("password"));

            log.info("password : " + password);

            UserInfoDTO pDTO = new UserInfoDTO();
            pDTO.setUserId(newPassword);
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));
            userInfoService.newPasswordProc(pDTO);

            session.setAttribute("NEW_PASSWORD", "");
            session.removeAttribute("NEW_PASSWORD");
            msg = "비밀번호가 재설정되었습니다.";
        } else {
            msg = "비정상 접근입니다.";

        }
        model.addAttribute("msg", msg);
        log.info(this.getClass().getName() + ".user/newPasswordProc End!");

        return "user/newPasswordResult";
    }
    @ResponseBody
    @PostMapping(value = "getEmailExistsT")
    public UserInfoDTO getEmailExistsT(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".getEmailExistsT Start!");

        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("email: " + email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setEmail(EncryptUtil.encAES128CBC(email));

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getEmailExistsT(pDTO)).orElseGet(UserInfoDTO::new);

        log.info(this.getClass().getName() + ".getEmailExistsT End!");

        return rDTO;
    }
}