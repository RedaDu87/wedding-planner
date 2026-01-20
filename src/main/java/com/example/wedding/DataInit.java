package com.example.wedding;

import com.example.wedding.domain.Prestation;
import com.example.wedding.service.WeddingService;
import com.example.wedding.repo.PrestationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataInit {

    @Bean
    CommandLineRunner init(PrestationRepository prestationRepo, WeddingService service) {
        return args -> {
            if (prestationRepo.count() == 0) {
                prestationRepo.save(Prestation.builder().titre("Salle + déco").prix(new BigDecimal("3500.00")).build());
                prestationRepo.save(Prestation.builder().titre("Traiteur (par personne)").prix(new BigDecimal("95.00")).build());
                prestationRepo.save(Prestation.builder().titre("Photographe").prix(new BigDecimal("1200.00")).build());
                prestationRepo.save(Prestation.builder().titre("DJ").prix(new BigDecimal("800.00")).build());
            }
            if (service.scenarios().isEmpty()) {
                service.createScenario("Scénario A");
                service.createScenario("Scénario B");
            }
        };
    }
}
