package java_dz_4;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("/doctors/{id}")
    public Doctor findById(@PathVariable Integer id) {
        return doctorService.findById(id).orElseThrow(DoctorNotFoundException::new);
    }


    @GetMapping("/doctors")
    public List<Doctor> findAll(@RequestParam Optional<String> specialization, @RequestParam Optional<String> name) {
        Optional<Predicate<Doctor>> mayBeSpecPredicate = specialization.map(this::filterBySpecialization);
        Optional<Predicate<Doctor>> mayBeNamePredicate = name.map(this::filterByFirstLetterOfName);
        Predicate<Doctor> predicate = Stream.of(mayBeNamePredicate, mayBeSpecPredicate)
                .flatMap(Optional::stream)
                .reduce(Predicate::and)
                .orElse(doctor -> true);
        return doctorService.findAll(predicate);

    }

    private Predicate<Doctor> filterBySpecialization(String spec) {
        return doctor -> doctor.getSpecialization().equals(spec);
    }

    private Predicate<Doctor> filterByFirstLetterOfName(String letter) {
        return doctor -> doctor.getName().substring(0, 1).equals(letter);
    }


    @PostMapping("/doctors")
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        if (doctor.getId() == null) {
            Integer id = doctorService.createDoctor(doctor);
            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("HTTP")
                    .host("localhost")
                    .port(8080)
                    .path("/doctors/{id}")
                    .build(id);
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.badRequest().body("id was detected, try again without id!");
    }


    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@RequestBody Doctor doctor,
                                          @PathVariable Integer id) {
        if (!doctor.getId().equals(id)) {
            return ResponseEntity.badRequest().body("identifiers do not match!");
        }
        doctorService.updateDoctor(doctor);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Integer id) {

        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noSuchDoctor(DoctorNotFoundException e) {

    }
}