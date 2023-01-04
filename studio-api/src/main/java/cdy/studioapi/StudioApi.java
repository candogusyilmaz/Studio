package cdy.studioapi;

import cdy.studioapi.config.CorsProperties;
import cdy.studioapi.config.RsaKeyProperties;
import cdy.studioapi.infrastructure.jpa.PermissionJpaRepository;
import cdy.studioapi.infrastructure.jpa.RoleJpaRepository;
import cdy.studioapi.infrastructure.jpa.UserJpaRepository;
import cdy.studioapi.services.UserRoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.TimeZone;

@SpringBootApplication
@EnableConfigurationProperties({RsaKeyProperties.class, CorsProperties.class})
public class StudioApi {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(StudioApi.class, args);
    }

    @Bean
    public CommandLineRunner runner(UserJpaRepository userRepo, RoleJpaRepository roleRepo, UserRoleService userRoleService, PasswordEncoder encoder, PermissionJpaRepository perRepo) {
        return args -> {
            /*
            var user = new User("mirakyu", encoder.encode("123"));
            user.setDisplayName("didid");
            user.setEmail("asdgasdg");
            userRepo.save(user);

            var role = new Role("deneme rol");
            roleRepo.save(role);

            var role2 = roleRepo.save(new Role("deneme rol 2"));

            //userRoleService.createRoleMember(user, role);
            //userRoleService.createRoleMember(user, role2);
            //roleMemberService.removeRoleMember(member);

            var permission = new Permission("per 1", "per1");
            perRepo.save(permission);
            */
        };
    }
}
