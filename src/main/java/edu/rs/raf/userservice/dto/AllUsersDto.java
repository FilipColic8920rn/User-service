package edu.rs.raf.userservice.dto;

import java.util.ArrayList;
import java.util.List;

public class AllUsersDto {

    private List<UserDto> users = new ArrayList<>();

    public AllUsersDto() {
    }

    public AllUsersDto(List<UserDto> users) {
        this.users = users;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}
