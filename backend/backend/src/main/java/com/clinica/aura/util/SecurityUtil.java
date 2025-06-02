package com.clinica.aura.util;

import com.clinica.aura.exceptions.ProfessionalNotFoundException;
import com.clinica.aura.exceptions.UnauthorizedAccessException;
import com.clinica.aura.modules.person.model.PersonModel;
import com.clinica.aura.modules.professional.model.ProfessionalModel;
import com.clinica.aura.modules.professional.repository.ProfessionalRepository;
import com.clinica.aura.modules.user_account.models.UserModel;
import com.clinica.aura.modules.user_account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final UserRepository userRepository;
    private final ProfessionalRepository professionalRepository;

    public ProfessionalModel getAuthenticatedProfessional() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if ("anonymousUser".equals(email)) {
            throw new UnauthorizedAccessException("Debe iniciar sesión un profesional para realizar esta operación");
        }

        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con el email " + email + " no existe"));

        PersonModel person = user.getPerson();

        return professionalRepository.findByPerson(person)
                .orElseThrow(() -> new ProfessionalNotFoundException("No se encontró un profesional asociado a la persona con id: " + person.getId()));
    }
}
