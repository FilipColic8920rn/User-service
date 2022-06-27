package edu.rs.raf.userservice.service.impl;

import edu.rs.raf.userservice.domain.PasswordToken;
import edu.rs.raf.userservice.domain.Rank;
import edu.rs.raf.userservice.domain.User;

import edu.rs.raf.userservice.dto.*;
import edu.rs.raf.userservice.exception.*;
import edu.rs.raf.userservice.mapper.UserMapper;
import edu.rs.raf.userservice.messageHelper.MessageHelper;
import edu.rs.raf.userservice.repository.PasswordTokenRepository;
import edu.rs.raf.userservice.repository.RankRepository;
import edu.rs.raf.userservice.repository.RoleRepository;
import edu.rs.raf.userservice.repository.UserRepository;
import edu.rs.raf.userservice.security.service.TokenService;
import edu.rs.raf.userservice.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private TokenService tokenService;
    private UserRepository userRepository;
    private RankRepository rankRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;
    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    private String emailDestination;
    private PasswordTokenRepository passwordTokenRepository;


    public UserServiceImpl(TokenService tokenService, UserRepository userRepository, RankRepository rankRepository,UserMapper userMapper,
                           @Value("${destination.sendMail}") String emailDestination, JmsTemplate jmsTemplate, MessageHelper messageHelper,
                           PasswordTokenRepository passwordTokenRepository, RoleRepository roleRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
        this.userMapper = userMapper;
        this.emailDestination = emailDestination;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.passwordTokenRepository = passwordTokenRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserDto);
    }

    @Override
    public UserDto add(UserCreateDto userCreateDto) {
        User user = userMapper.userCreateDtoToUser(userCreateDto);
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }
    @Override
    public UserDto addClient(UserCreateDto userCreateDto){
        if(userCreateDto.getDateOfBirth() == null || userCreateDto.getDateOfBirth().equals("")
                || LocalDate.parse(userCreateDto.getDateOfBirth()).isAfter(LocalDate.now()))
            throw new InvalidDateException();

        User user = userMapper.userCreateDtoToClient(userCreateDto);

        if(userRepository.findUserByUsername(user.getUsername()).isPresent())
            throw new UsernameExistsException();
        else if(userRepository.findUserByEmail(user.getEmail()).isPresent())
            throw new EmailInUseException();
        userRepository.save(user);
        sendEmail(userCreateDto, "account_activation", "http://localhost:8081/api/user/activation/"+user.getId());
        return userMapper.userToUserDto(user);
    }
    @Override
    public UserDto addManager(UserCreateDto userCreateDto){
        if(userCreateDto.getDateOfBirth() == null || userCreateDto.getDateOfBirth().equals("") ||
                LocalDate.parse(userCreateDto.getDateOfBirth()).isAfter(LocalDate.now()))
            throw new InvalidDateException();

        if(userCreateDto.getDateOfEmployment() == null || userCreateDto.getDateOfEmployment().equals("") ||
                LocalDate.parse(userCreateDto.getDateOfEmployment()).isAfter(LocalDate.now()))
            throw new InvalidDateException();

        User user = userMapper.userCreateDtoToManager(userCreateDto);

        if(userRepository.findUserByUsername(user.getUsername()).isPresent())
            throw new UsernameExistsException();
        else if(userRepository.findUserByEmail(user.getEmail()).isPresent())
            throw new EmailInUseException();

        userRepository.save(user);
        sendEmail(userCreateDto, "account_activation", "http://localhost:8081/api/user/activation/"+user.getId());
        return userMapper.userToUserDto(user);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        //ako stavimo da se pri login funkciji bira da li
        //korinik pokusava da se prijavi kao neki role
        //mozemo da imamo u zahtevu role i onda znamo koji tacno repo
        //da pretrazujemo, a ne da pretrazujemo sve repo
        User user = userRepository
                .findUserByEmailAndPassword(tokenRequestDto.getEmail(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with email: %s and password: %s not found.", tokenRequestDto.getEmail(),
                                tokenRequestDto.getPassword())));
        if(user.getActivated() == 0 || user.getBlocked() == 1)
            throw new NotActivatedException("user blocked or not activated");
        //Create token payload
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("role", user.getRole().getName());
        claims.put("email", user.getEmail());
        //Generate token
        return new TokenResponseDto(tokenService.generate(claims));
    }

    //proveravamo da li vec postoji korisnik za prosledjenim username-om i mejlom
    @Override
    public boolean checkUser(UserCreateDto userCreateDto) {
        if(userRepository.findUserByUsername(userCreateDto.getUsername()).isPresent())
            return true;
        else if(userRepository.findUserByEmail(userCreateDto.getEmail()).isPresent())
            return true;

        return false;
    }

    @Override
    public void changeUser(UserCreateDto userCreateDto) {

        System.out.println("++++++++change user");
        User user = userRepository.findUserById(userCreateDto.getId()).orElseThrow(() -> new NotFoundException(String
                .format("User with id: %d not found.", userCreateDto.getId())));

        if(userCreateDto.getEmail() != null && userRepository.findUserByEmail(userCreateDto.getEmail()).isPresent())
            throw new EmailInUseException();
        else if(userCreateDto.getUsername() != null && userRepository.findUserByUsername(userCreateDto.getUsername()).isPresent())
            throw new UsernameExistsException();

        if(userCreateDto.getPassportNumber() != null && !userCreateDto.getPassportNumber().equals(""))
            user.setPassportNumber(userCreateDto.getPassportNumber());

        if(userCreateDto.getDateOfBirth() != null && !userCreateDto.getDateOfBirth().equals("")) {
            if (LocalDate.parse(userCreateDto.getDateOfBirth()).isAfter(LocalDate.now()))
                throw new InvalidDateException();
            user.setDateOfBirth(Date.valueOf(userCreateDto.getDateOfBirth()));
        }

        if(userCreateDto.getFirstName() != null && !userCreateDto.getFirstName().equals(""))
            user.setFirstName(userCreateDto.getFirstName());

        if(userCreateDto.getLastName() != null &&!userCreateDto.getLastName().equals(""))
            user.setLastName(userCreateDto.getLastName());

        if(userCreateDto.getPhoneNumber() != null && !userCreateDto.getPhoneNumber().equals(""))
            user.setPhoneNumber(userCreateDto.getPhoneNumber());

        if(userCreateDto.getUsername() != null && !userCreateDto.getUsername().equals(""))
            user.setUsername(userCreateDto.getUsername());

        if(userCreateDto.getPassword() != null && !userCreateDto.getPassword().equals(""))
            user.setPassword(userCreateDto.getPassword());

        if(userCreateDto.getEmail() != null && !userCreateDto.getEmail().equals(""))
            user.setEmail(userCreateDto.getEmail());
        if(userCreateDto.getHotelName() != null && !userCreateDto.getHotelName().equals(""))
            user.setHotelName(userCreateDto.getHotelName());

        if(userCreateDto.getDateOfEmployment() != null && !userCreateDto.getDateOfEmployment().equals("")) {
            if (LocalDate.parse(userCreateDto.getDateOfEmployment()).isAfter(LocalDate.now()))
                throw new InvalidDateException();
            user.setDateOfEmployment(Date.valueOf(userCreateDto.getDateOfEmployment()));
        }
        userRepository.save(user);

    }

    @Override
    public void blockUser(Long id) {
        User user = userRepository.findUserById(id).orElseThrow(() -> new NotFoundException(String
                .format("User with id: %d not found.", id)));;
        if(user.getRole().getName().equals("ROLE_ADMIN"))
            throw new BanAdminException();
        if(user.getBlocked() == 0)
            user.setBlocked(1);
        else
            user.setBlocked(0);
        userRepository.save(user);
    }

    @Override
    public int getDiscount(Long userId) {

        int rez = 0;
        int min = -1;
        User user = userRepository.findUserById(userId).orElseThrow(() -> new NotFoundException(String
                .format("User with id: %d not found.", userId)));
        //User user = userRepository.findUserById(userId).get();
        ArrayList<Rank> ranks = rankRepository.findAll();
        for(int i = 0; i < ranks.size(); i++){
            if(user.getNumberOfReservations() >= ranks.get(i).getReservationNumMin() && ranks.get(i).getReservationNumMin() > min){
                rez = ranks.get(i).getDiscount();
                min = ranks.get(i).getReservationNumMin();
            }
        }

        return rez;
    }



    @Override
    public void sendEmail(UserCreateDto userCreateDto, String email_type, String link) {
        MessageDto m = new MessageDto();

        m.setHotelName(userCreateDto.getHotelName());
        m.setLastname(userCreateDto.getLastName());
        m.setEmail(userCreateDto.getEmail());
        m.setLink(link);
        m.setName(userCreateDto.getFirstName());
        m.setType(email_type);
        m.setRezStart("");

        String toSend = messageHelper.createTextMessage(m);
        System.out.println(toSend);
        //System.out.println("+++++++pre nego sto se poslalo na emali servis");
        jmsTemplate.convertAndSend(emailDestination, toSend);
        //System.out.println("+++++++nako sto se poslalo na emali servis");
    }

    @Override
    public UserDto getUser(Long userId) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new NotFoundException(String
                .format("User with user id: %d not found.", userId)));
        UserDto  dto = userMapper.userToUserDto(user);
        return dto;
    }

    @Override
    public RankDto changeRank(RankDto rankDto) {
        Rank rank = rankRepository.findRankById(rankDto.getId()).orElseThrow(() -> new NotFoundException(String
                .format("Rank with rank id: %d not found.", rankDto.getId())));
        if(rankDto.getDiscount() != -1)
            rank.setDiscount(rankDto.getDiscount());
        if(rankDto.getReservationNumMin() != -1)
            rank.setReservationNum(rankDto.getReservationNumMin());
        rankRepository.save(rank);
        return rankDto;
    }

    @Override
    public void incrementRez(Long userId) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new NotFoundException(String
                .format("User with user id: %d not found.", userId)));
        user.setNumberOfReservations(user.getNumberOfReservations()+1);
        userRepository.save(user);
    }

    @Override
    public void decrementRez(Long userId) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new NotFoundException(String
                .format("User with user id: %d not found.", userId)));
        user.setNumberOfReservations(user.getNumberOfReservations()-1);
        userRepository.save(user);
    }

    @Override
    public void activate(Long userId) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new NotFoundException(String
                .format("User with user id: %d not found.", userId)));
        user.setActivated(1);
        userRepository.save(user);
    }

    @Override
    public void generatePasswordCode(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String
                .format("User with user email: %s not found.", email)));

        UUID u = UUID.randomUUID();
        PasswordToken pt = new PasswordToken();
        pt.setToken(u.toString());

        passwordTokenRepository.save(pt);
        sendEmail(userMapper.userToUserCreateDto(user), "change_password", u.toString());
    }

    @Override
    public void changePassword(ChangePasswordRequestDto dto) {
        System.out.println("++++++here " + dto.getEmail() + " " + dto.getToken() + " " + dto.getNewPassword());
        User user = userRepository.findUserByEmail(dto.getEmail()).orElseThrow(() -> new NotFoundException(String
                .format("User with user email: %s not found.", dto.getEmail())));
        PasswordToken pt = passwordTokenRepository.findPasswordTokenByToken(dto.getToken()).orElseThrow(() -> new NotFoundException(String
                .format("Token with string : %s not found.", dto.getToken())));
        user.setPassword(dto.getNewPassword());
        userRepository.save(user);
        passwordTokenRepository.delete(pt);
    }

    @Override
    public AllUsersDto getAllUsers() {

        List<User> users = userRepository.findAll();
        AllUsersDto dto = new AllUsersDto();
        for(User user: users){
            String role = user.getRole().getName();
            dto.getUsers().add(new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getBlocked(), role));
        }

        return dto;
    }

    @Override
    public AllRanksDto getAllRanks() {

        AllRanksDto dto = new AllRanksDto();
        for(Rank rank : rankRepository.findAll()){
            dto.getRanks().add(new RankDto(rank.getId(), rank.getName(), rank.getReservationNumMin(), rank.getDiscount()));
        }

        return dto;
    }
}
