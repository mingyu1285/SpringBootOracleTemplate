package kopo.poly.persistance.mapper;


import kopo.poly.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IUserInfoMapper {

    // 회원가입하기
    int insertUserInfo(UserInfoDTO pDTO) throws Exception;


    // 회원 가입 전 아이디 중복체크하기 (DB 조회하기)
    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;


    // 회원 가입 전 이메일 중복체크하기(DB 조회하기)
    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception;

    List<UserInfoDTO> getUserInfoList() throws Exception;

    UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;


    UserInfoDTO getUserId(UserInfoDTO pDTO) throws Exception;

    int updatePassword(UserInfoDTO pDTO) throws Exception;





}
