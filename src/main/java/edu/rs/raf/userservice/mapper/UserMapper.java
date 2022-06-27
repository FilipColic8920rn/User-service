package edu.rs.raf.userservice.mapper;

import edu.rs.raf.userservice.domain.User;
import edu.rs.raf.userservice.dto.UserCreateDto;
import edu.rs.raf.userservice.dto.UserDto;
import edu.rs.raf.userservice.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class UserMapper {

    private RoleRepository roleRepository;

    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserDto userToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        if(user.getDateOfBirth() != null)
            userDto.setDateOfBirth(user.getDateOfBirth().toString());
        userDto.setPassword(user.getPassword());
        userDto.setPassportNumber(user.getPassportNumber());
        userDto.setBlocked(user.getBlocked());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setHotelName(user.getHotelName());
        userDto.setRole(user.getRole().getName());
        userDto.setNumberOfReservations(user.getNumberOfReservations()+"");
        if(user.getDateOfEmployment() != null)
            userDto.setDateOfEmployment(user.getDateOfEmployment().toString());
        return userDto;
    }

    public UserCreateDto userToUserCreateDto(User user){
        UserCreateDto dto = new UserCreateDto();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setHotelName(user.getHotelName());
        dto.setId(user.getId());
        return dto;
    }

    public User userCreateDtoToUser(UserCreateDto userCreateDto){
        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());
        user.setDateOfBirth(Date.valueOf(userCreateDto.getDateOfBirth()));

        return user;
    }

    public User userCreateDtoToClient(UserCreateDto userCreateDto) {
        User user = new User();

        user.setEmail(userCreateDto.getEmail());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());
        if(userCreateDto.getDateOfBirth() != null && !userCreateDto.getDateOfBirth().equals(""))
            user.setDateOfBirth(Date.valueOf(userCreateDto.getDateOfBirth()));
        user.setPassportNumber(userCreateDto.getPassportNumber());

        user.setRole(roleRepository.findRoleByName("ROLE_CLIENT").get());
        user.setNumberOfReservations(0);
        user.setHotelName(null);
        user.setDateOfEmployment(null);

        return user;
    }

    public User userCreateDtoToManager(UserCreateDto userCreateDto){
        User user = new User();

        user.setEmail(userCreateDto.getEmail());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());
        if(userCreateDto.getDateOfBirth() != null && !userCreateDto.getDateOfBirth().equals(""))
            user.setDateOfBirth(Date.valueOf(userCreateDto.getDateOfBirth()));
        user.setPassportNumber(null);

        user.setRole(roleRepository.findRoleByName("ROLE_MANAGER").get());
        user.setNumberOfReservations(0);
        user.setHotelName(userCreateDto.getHotelName());
        if(userCreateDto.getDateOfEmployment() != null && !userCreateDto.getDateOfEmployment().equals(""))
            user.setDateOfEmployment(Date.valueOf(userCreateDto.getDateOfEmployment()));

        return user;
    }
}
