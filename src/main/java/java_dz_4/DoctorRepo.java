package java_dz_4;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class DoctorRepo {
    private final List<Doctor> doctors = new CopyOnWriteArrayList<>();

    {
        doctors.add(new Doctor(1, "Kirill", "sp1"));
        doctors.add(new Doctor(2, "Vasya", "sp2"));
        doctors.add(new Doctor(3, "Nikita", "sp3"));
    }

    public List<Doctor> findAll() {
        return doctors;
    }

    public Optional<Doctor> findById(Integer id) {
        return doctors.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    synchronized public Integer createDoctor(Doctor doctor) {

        Integer id = doctors.stream().map(it -> it.getId()).max(Integer::compare).get() + 1;
        doctors.add(new Doctor(id, doctor.getName(), doctor.getSpecialization()));
        return id;
    }


    public void updateDoctor(Doctor doctor) {
        findIndexById(doctor.getId()).map(it -> doctors.set(it, doctor))
                .orElseThrow(DoctorNotFoundException::new);
    }

    public void deleteDoctor(Integer id) {
        findIndexById(id).map(it -> doctors.remove(it.intValue()))
                .orElseThrow(DoctorNotFoundException::new);
    }

    private Optional<Integer> findIndexById(Integer id) {
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getId().equals(id)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }
}