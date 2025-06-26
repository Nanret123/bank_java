package com.example.bank.seeder;


import com.example.bank.customer.entity.Customer;
import com.example.bank.customer.enums.CustomerStatus;
import com.example.bank.customer.enums.CustomerType;
import com.example.bank.customer.enums.Gender;
import com.example.bank.customer.enums.MaritalStatus;
import com.example.bank.customer.enums.VerificationStatus;
import com.example.bank.customer.repository.CustomerRepository;
import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomerSeeder implements CommandLineRunner {
   private final CustomerRepository customerRepo;

  @Override
    public void run(String... args) {
        if (customerRepo.count() <= 2) {
            System.out.println("🔧 Seeding customers...");
            Faker faker = new Faker();
            List<Customer> customers = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                customers.add(generateCustomer(faker));
            }

            customerRepo.saveAll(customers);
            System.out.println("✅ Customers seeded successfully.");
        }
    }

    private Customer generateCustomer(Faker faker) {
        Gender gender = faker.options().option(Gender.class);
        MaritalStatus maritalStatus = faker.options().option(MaritalStatus.class);
        CustomerType customerType = faker.options().option(CustomerType.class);
        CustomerStatus status = faker.options().option(CustomerStatus.class);

        return Customer.builder()
            .title(faker.options().option("Mr", "Mrs", "Ms", "Dr"))
            .firstName(faker.name().firstName())
            .middleName(faker.name().firstName()) // Faker doesn't provide a true middle name
            .lastName(faker.name().lastName())
            .dateOfBirth(faker.date().birthday(18, 65).toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate())
            .gender(gender)
            .maritalStatus(maritalStatus)
            .nationality(faker.country().name())
            .email(faker.internet().emailAddress())
            .phoneNumber(faker.phoneNumber().cellPhone())
            .alternativePhone(faker.phoneNumber().cellPhone())
            .residentialAddress(faker.address().streetAddress())
            .city(faker.address().city())
            .state(faker.address().state())
            .country(faker.address().country())
            .occupation(faker.job().title())
            .customerType(customerType)
            .status(status)
            .statusChangeReason(faker.lorem().sentence())
            .emergencyContactName(faker.name().fullName())
            .emergencyContactRelationship(faker.options().option("Spouse", "Parent", "Sibling", "Friend"))
            .emergencyContactPhone(faker.phoneNumber().cellPhone())
            .branchCode(faker.number().digits(5))
            .createdBy(UUID.fromString("2096bd97-6aab-486f-a461-8dadde9f5a15"))
            .approvedBy(UUID.fromString("2096bd97-6aab-486f-a461-8dadde9f5a15"))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .isDeleted(false)
            .verificationStatus(VerificationStatus.PENDING)
            .build();
    }
}
