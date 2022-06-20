package sk.posam.service;

import com.machinezoo.sourceafis.FingerprintTemplate;
import sk.posam.domain.Patient;
import sk.posam.domain.User;

import java.io.IOException;

import java.util.List;

public interface UserStorageService {
    void saveUser(User user, String filepath);
    String addFingerprintToUser(User user, String filepath);
    Integer findUser (FingerprintTemplate fingerprint) throws IOException;
    boolean patientDuplicateCheck (List<Patient> ps, long patient_number);
    User findUserByFingerprintId(Integer id);
    boolean testNameRegistration(String username);
    boolean testNameForAdding(User user);
}
