package com.clinica.aura.models.receptionist.mapper;

import com.clinica.aura.models.person.model.PersonModel;
import com.clinica.aura.models.receptionist.dtoRequest.ReceptionistRequestDto;
import com.clinica.aura.models.receptionist.dtoRequest.ReceptionistRequestUpdateDto;
import com.clinica.aura.models.receptionist.dtoResponse.ReceptionistResponseDto;
import com.clinica.aura.models.receptionist.model.ReceptionistModel;
import com.clinica.aura.models.user_account.models.UserModel;
import com.clinica.aura.models.user_account.repository.UserRepository;
import com.clinica.aura.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceptionistMapper {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public ReceptionistModel toReceptionistModel(ReceptionistRequestDto receptionistRequestDto) {
        return modelMapper.map(receptionistRequestDto, ReceptionistModel.class);
    }

    public ReceptionistResponseDto toReceptionistResponseDto(ReceptionistModel receptionistModel) {
        PersonModel person = receptionistModel.getPerson();

        return ReceptionistResponseDto.builder()
                .id(receptionistModel.getId())
                .name(person.getName())
                .lastName(person.getLastName())
                .dni(person.getDni())
                .phoneNumber(person.getPhoneNumber())
                .email(userRepository.findByPerson(person).orElseThrow(() -> new UserNotFoundException("No se encontro el usuario")).getEmail())
                .address(person.getAddress())
                .locality(person.getLocality())
                .cuil(person.getCuil())
                .birthDate(person.getBirthDate())
                .build();
    }


    public void updateReceptionist(ReceptionistRequestUpdateDto dto, ReceptionistModel receptionist) {
        PersonModel person = receptionist.getPerson();

        if (dto.getName() != null) {
            person.setName(dto.getName());
        }
        if (dto.getLastName() != null) {
            person.setLastName(dto.getLastName());
        }
        if (dto.getDni() != null) {
            person.setDni(dto.getDni());
        }
        if (dto.getPhoneNumber() != null) {
            person.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getAddress() != null) {
            person.setAddress(dto.getAddress());
        }
        if (dto.getLocality() != null) {
            person.setLocality(dto.getLocality());
        }
        if (dto.getCuil() != null) {
            person.setCuil(dto.getCuil());
        }
        if (dto.getBirthDate() != null) {
            person.setBirthDate(dto.getBirthDate());
        }
        if (dto.getEmail() != null) {
            UserModel user = userRepository.findByPersonId(receptionist.getPerson().getId())
                    .orElseThrow(() -> new UserNotFoundException("No se encontró usuario para esta persona."));
            user.setEmail(dto.getEmail());
            userRepository.save(user);
        }


    }
}
