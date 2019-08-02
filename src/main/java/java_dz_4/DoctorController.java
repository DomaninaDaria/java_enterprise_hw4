package java_dz_4;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("/doctors/{id}")
    public Doctor findById(@PathVariable Integer id) {
        Optional<Doctor> mayBeDoctor = doctorService.findById(id);
        return mayBeDoctor.orElseThrow(DoctorNotFoundException::new);
    }

    @GetMapping("/doctors")
    public List<Doctor> findAll() {
        return doctorService.findAll();
    }

    @GetMapping("/doctors/specialization={specialization}")
    public List<Doctor> findDoctorsBySpecialization(@PathVariable String specialization) {
        return doctorService.findDoctorsBySpecialization(specialization);
    }


    @GetMapping("/doctors/name={letter}")
    public List<Doctor> findDoctorsByFirstLetter(@PathVariable char letter) {
        return doctorService.findDoctorsByFirstLetter(letter);
    }

    @PostMapping("/doctors")
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        if (doctor.getId() == null) {
            Integer id = doctorService.createDoctor(doctor);
            try {
                return ResponseEntity.created(new URI("http://localhost:8080//doctors/" + id)).build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().body("id was detected, try again without id");
    }


    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@RequestBody Doctor doctor,
                                          @PathVariable Integer id) {
        if (!doctor.getId().equals(id)) {
            return ResponseEntity.badRequest().body("identifiers do not match!");
        }
        try {
            doctorService.updateDoctor(doctor);
            return ResponseEntity.noContent().build();
        } catch (DoctorNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Integer id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.noContent().build();
        } catch (DoctorNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}