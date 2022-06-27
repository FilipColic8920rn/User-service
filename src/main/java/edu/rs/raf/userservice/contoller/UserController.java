package edu.rs.raf.userservice.contoller;

import edu.rs.raf.userservice.dto.*;
import edu.rs.raf.userservice.security.CheckSecurity;
import edu.rs.raf.userservice.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get all users")
    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<AllUsersDto> getAllUsers(@RequestHeader("Authorization") String authorization) {
        return new ResponseEntity<AllUsersDto>(userService.getAllUsers(), HttpStatus.OK);
    }

    @ApiOperation(value = "Register user")
    @PostMapping("/register")
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return new ResponseEntity<>(userService.add(userCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Register client")
    @PostMapping("/register/client")
    public ResponseEntity<Void> registerClient(@RequestBody @Valid UserCreateDto userCreateDto) {
        userService.addClient(userCreateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiOperation(value = "Register manager")
    @PostMapping("/register/manager")
    public ResponseEntity<Void> registerManager(@RequestBody @Valid UserCreateDto userCreateDto) {
        userService.addManager(userCreateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        TokenResponseDto token = userService.login(tokenRequestDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @ApiOperation(value = "Change user parametars")
    @PostMapping("/change")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_CLIENT", "ROLE_MANAGER"})
    public ResponseEntity<Void> changeUser(@RequestHeader("Authorization") String authorization, @RequestBody @Valid UserCreateDto userCreateDto) {
        System.out.println("-----usao u kontoler za change user");
        userService.changeUser(userCreateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiOperation(value = "Block or unblock a user from logging in")
    @PostMapping("/block")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Void> blockUser(@RequestHeader("Authorization") String authorization, @RequestBody @Valid Long userId) {
        userService.blockUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiOperation(value = "Get user discount")
    @GetMapping("/discount/{userId}")
    public ResponseEntity<Integer> getDiscount(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userService.getDiscount(userId), HttpStatus.OK);
    }
    @ApiOperation(value = "Get userDto")
    @GetMapping("/get/{userId}")
    public ResponseEntity<UserDto> getUserDto(@PathVariable("userId") Long userId){
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @ApiOperation(value = "Gat all ranks")
    @GetMapping("/rank")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<AllRanksDto> getAllRanks(@RequestHeader("Authorization") String authorization){
        return new ResponseEntity<AllRanksDto>(userService.getAllRanks(), HttpStatus.OK);
    }

    @ApiOperation(value = "Change rank")
    @PostMapping("/changeRank")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<RankDto> changeRank(@RequestHeader("Authorization") String authorization, @RequestBody @Valid RankDto rankDto){
        return new ResponseEntity<>(userService.changeRank(rankDto), HttpStatus.OK);
    }
    @ApiOperation(value = "Increment number of reservations")
    @PostMapping("/incrementRez/{userId}")
    public ResponseEntity<Void> incrementRez(@PathVariable("userId") Long userId){
        userService.incrementRez(userId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @ApiOperation(value = "Decrement number of reservations")
    @PostMapping("/decrementRez/{userId}")
    public ResponseEntity<Void> decrementRez(@PathVariable("userId") Long userId){
        userService.decrementRez(userId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @ApiOperation(value = "Activate account")
    @GetMapping("/activation/{userId}")
    public ResponseEntity<Void> activateAccount(@PathVariable("userId") Long userId){
        userService.activate(userId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @ApiOperation(value = "Activate account")
    @GetMapping("/passwordChange/request/{email}")
    public ResponseEntity<Void> passwordChangeRequest(@PathVariable("email") String email){
        userService.generatePasswordCode(email);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @ApiOperation(value = "Activate account")
    @PostMapping("/passwordChange")
    public ResponseEntity<Void> passwordChange(@RequestBody @Valid ChangePasswordRequestDto dto){
        userService.changePassword(dto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
