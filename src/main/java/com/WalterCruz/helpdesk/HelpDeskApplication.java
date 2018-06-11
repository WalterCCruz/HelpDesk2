package com.WalterCruz.helpdesk;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import javax.jws.soap.SOAPBinding.Use;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.config.authentication.PasswordEncoderParser;

import com.WalterCruz.helpdesk.api.entity.User;
import com.WalterCruz.helpdesk.api.enums.ProfileEnum;
import com.WalterCruz.helpdesk.api.repository.UserRepository;

@SpringBootApplication
public class HelpDeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpDeskApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init (UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			initUsers(userRepository, passwordEncoder);
		};
	}
	
	
	private void initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		User admin = new User();
		admin.setEmail("admin@helpdesk.com");
		admin.setPassword(passwordEncoder.encodePassword(rawPass, salt));
		admin.setProfile(ProfileEnum.ROLE_ADMIN);
		
		User find = UserRepository.findByEmail("admin@helpdesk.com");
		if(find == null) {
			userRepository.save(admin);
		}
		
	}
	
}
