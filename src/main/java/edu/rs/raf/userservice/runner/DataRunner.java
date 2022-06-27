package edu.rs.raf.userservice.runner;

import edu.rs.raf.userservice.domain.Rank;
import edu.rs.raf.userservice.domain.Role;
import edu.rs.raf.userservice.domain.User;
import edu.rs.raf.userservice.repository.RankRepository;
import edu.rs.raf.userservice.repository.RoleRepository;
import edu.rs.raf.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Profile({"default"})
@Component
public class DataRunner implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private RankRepository rankRepository;

    public DataRunner(RoleRepository roleRepository, UserRepository userRepository, RankRepository rankRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //Insert roles
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleManager = new Role("ROLE_MANAGER");
        Role roleUser = new Role("ROLE_CLIENT");
        roleRepository.save(roleAdmin);
        roleRepository.save(roleManager);
        roleRepository.save(roleUser);
        //Insert admin
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setPhoneNumber("06212334567");
        admin.setDateOfBirth(Date.valueOf("2021-1-1"));
        admin.setFirstName("Admin");
        admin.setLastName("Adminovic");
        admin.setPassportNumber("987654321");
        admin.setRole(roleAdmin);
        admin.setActivated(1);
        userRepository.save(admin);
        //insert test client
        User client = new User();
        client.setEmail("client@gmail.com");
        client.setUsername("client69");
        client.setPassword("client");
        client.setPhoneNumber("123654789");
        client.setDateOfBirth(Date.valueOf("2022-1-1"));
        client.setFirstName("Kor");
        client.setLastName("Isnik");
        client.setPassportNumber("987654321");
        client.setRole(roleUser);
        client.setNumberOfReservations(10);
        client.setActivated(1);
        userRepository.save(client);
        //insert test manager
        User manager = new User();
        manager.setEmail("manager@gmail.com");
        manager.setUsername("manager");
        manager.setPassword("man");
        manager.setPhoneNumber("123654789");
        manager.setDateOfBirth(Date.valueOf("2022-1-1"));
        manager.setDateOfEmployment(Date.valueOf("2020-1-1"));
        manager.setHotelName("Tipton");
        manager.setFirstName("Korr");
        manager.setLastName("Isnikk");
        manager.setPassportNumber("987654321");
        manager.setRole(roleManager);
        manager.setNumberOfReservations(0);
        manager.setActivated(1);
        userRepository.save(manager);
        //Insert ranks
        Rank rankZlato = new Rank("RANK_GOLD", 50, 20);
        Rank rankSrebro = new Rank("RANK_SILVER", 30, 10);
        Rank rankBronza = new Rank("RANK_BRONZE", 15, 5);
        rankRepository.save(rankZlato);
        rankRepository.save(rankSrebro);
        rankRepository.save(rankBronza);
    }
}
