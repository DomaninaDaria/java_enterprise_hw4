package java_dz_4;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DoctorService {
    private final DoctorRepo doctorRepo;

    public List<Doctor> findAll() {
        return doctorRepo.findAll();
    }

    public Optional<Doctor> findById(Integer id) {
        return doctorRepo.findById(id);
    }

    public List<Doctor> findDoctorsBySpecialization(String specialization) {
        return doctorRepo.findDoctorsBySpecialization(specialization);
    }

    public List<Doctor> findDoctorsByFirstLetter(char letter) {
        return doctorRepo.findDoctorsByFirstLetter(letter);
    }

    public Integer createDoctor(Doctor doctor) {
        return doctorRepo.createDoctor(doctor);
    }

    public void updateDoctor(Doctor doctor) {
        doctorRepo.updateDoctor(doctor);
    }

    public void deleteDoctor(Integer id) {
        doctorRepo.deleteDoctor(id);
    }
}