package edu.rs.raf.userservice.service;

import edu.rs.raf.userservice.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<UserDto> findAll(Pageable pageable);

    UserDto add(UserCreateDto userCreateDto);

    UserDto addClient(UserCreateDto userCreateDto);

    UserDto addManager(UserCreateDto userCreateDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    boolean checkUser(UserCreateDto userCreateDto);

    void changeUser(UserCreateDto userCreateDto);

    void blockUser(Long id);

    int getDiscount(Long userId);

    void sendEmail(UserCreateDto userCreateDto, String email_type, String link);

    UserDto getUser(Long userID);

    RankDto changeRank(RankDto rankDto);

    void incrementRez(Long userId);

    void decrementRez(Long userId);

    void activate(Long userId);

    void generatePasswordCode(String email);

    void changePassword(ChangePasswordRequestDto dto);

    AllUsersDto getAllUsers();

    AllRanksDto getAllRanks();

}
