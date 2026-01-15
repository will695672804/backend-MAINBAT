package com.mainbat.config;

import com.mainbat.model.enums.RoleType;
import com.mainbat.model.user.Permission;
import com.mainbat.model.user.Role;
import com.mainbat.model.user.User;
import com.mainbat.repository.RoleRepository;
import com.mainbat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Initialise les données de base au démarrage de l'application
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        seedRoles();
        seedAdminUser();
    }

    private void seedRoles() {
        for (RoleType roleType : RoleType.values()) {
            if (roleRepository.findByName(roleType.name()).isEmpty()) {
                Role role = Role.builder()
                        .name(roleType.name())
                        .description(getDescription(roleType))
                        .permissions(new HashSet<>())
                        .build();
                roleRepository.save(role);
                log.info("Rôle créé: {}", roleType.name());
            }
        }
    }

    private void seedAdminUser() {
        String adminEmail = "admin@mainbat.com";
        
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            Role superAdminRole = roleRepository.findByName(RoleType.SUPER_ADMIN.name())
                    .orElseThrow(() -> new RuntimeException("Rôle SUPER_ADMIN non trouvé"));

            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("Super")
                    .lastName("Admin")
                    .phoneNumber("+33600000000")
                    .isEmailVerified(true)
                    .roles(Set.of(superAdminRole))
                    .build();

            userRepository.save(admin);
            log.info("Utilisateur admin créé: {} / admin123", adminEmail);
        }
    }

    private String getDescription(RoleType roleType) {
        return switch (roleType) {
            case SUPER_ADMIN -> "Administrateur avec accès complet";
            case GESTIONNAIRE -> "Gestionnaire de bâtiments";
            case SUPERVISEUR -> "Superviseur de maintenance";
            case TECHNICIEN -> "Technicien de maintenance";
            case PRESTATAIRE -> "Prestataire externe";
        };
    }
}
