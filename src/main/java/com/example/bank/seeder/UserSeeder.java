package com.example.bank.seeder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.bank.security.entity.User;
import com.example.bank.security.entity.UserRole;
import com.example.bank.security.repository.UserRepository;
import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// @Profile("dev")
public class UserSeeder implements CommandLineRunner {
  private final UserRepository userRepo;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    if (userRepo.count() == 0) {
      System.out.println("🔧 Seeding users...");
      Faker faker = new Faker();
      List<User> users = new ArrayList<>();

      for (int i = 1; i <= 10; i++) {
        User user = User.builder()
            .username(faker.name().username())
            .email(faker.internet().emailAddress())
            .fullName(faker.name().fullName())
            .password(passwordEncoder.encode("Password123")) // default password
            .role(UserRole.TELLER)
            .branchCode("BR00" + (i % 3 + 1))
            .isActive(true)
            .isPasswordChanged(i % 2 == 0)
            .phoneNumber(faker.phoneNumber().cellPhone())
            .address(faker.address().fullAddress())
            .bio(faker.lorem().sentence())
            .avatarUrl("https://i.pravatar.cc/150?img=" + i)
            .build();

        users.add(user);
      }

      userRepo.saveAll(users);
      System.out.println("✅ 10 fake users seeded using Java Faker");
    }
  }
}
