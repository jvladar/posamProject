package sk.posam.controller;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;

import sk.posam.domain.MessageDetail;
import com.Futronic.ScanApiHelper.Scanner;
import sk.posam.service.UserStorageService;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintImageOptions;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import sk.posam.domain.Patient;
import sk.posam.domain.User;
import sk.posam.domain.Visit;
import sk.posam.repositories.PatientCrudRepository;
import sk.posam.repositories.UserCrudRepository;
import sk.posam.repositories.VisitCrudRepository;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;


@RestController
@EnableAutoConfiguration
/*@CrossOrigin*/
@RequestMapping("api")
public class DocController {

	@Autowired
	private UserStorageService usersStorageService;

	@Autowired
	private UserCrudRepository userRepository;

	@Autowired
	private PatientCrudRepository patientRepository;

	@Autowired
	private VisitCrudRepository visitRepository;




	@PostMapping("/registration")
	public MessageDetail scanRegistr(@RequestBody User user){
		Scanner scanner = new Scanner();
		scanner.SetOptions(1, scanner.FTR_OPTIONS_INVERT_IMAGE);
		MessageDetail s = new MessageDetail();
		if(scanner.OpenDevice() && scanner.IsFingerPresent()){
			byte[] data = new byte[320*480];
			scanner.GetImage2(4,data);
			System.out.println(user.getUsername());
			if (!usersStorageService.testNameRegistration(user.getUsername())){
				s.setMessage("Prihlasovacie meno " + user.getUsername() + " sa už používa, zvoľ iné !");
			}
			else {
				try {
					BufferedImage image1 = new BufferedImage(320, 480, BufferedImage.TYPE_BYTE_GRAY);
					byte[] array = ((DataBufferByte) image1.getRaster().getDataBuffer()).getData();
					System.arraycopy(data, 0, array, 0, array.length);
					Date date = new Date();
					Timestamp timeStamp = new Timestamp(date.getTime());
					String uniqueTime = timeStamp.toString();
					String filepath = "/Users/user/Desktop/PoSam/posamSpring/posamSpringApp/fingerprints/" + user.getUsername() + uniqueTime + ".png";
					File outputfile = new File(filepath);
					ImageIO.write(image1, "png", outputfile);
					usersStorageService.saveUser(user,filepath);
				}catch (IOException e){
					System.out.println(e);
				}

				s.setMessage("Registrácia užívateľa " + user.getUsername() + " a pridanie odtlačku prebehla úspešne !");
			}
			}
			scanner.CloseDevice();

		return s;
	}


	@PostMapping("/addfingerprint")
	public MessageDetail scanadd(@RequestBody User user) throws IOException {
		Scanner scanner = new Scanner();
		scanner.SetOptions(1, scanner.FTR_OPTIONS_INVERT_IMAGE);
		MessageDetail s = new MessageDetail();
		if(scanner.OpenDevice() && scanner.IsFingerPresent()){
			byte[] data = new byte[320*480];
			scanner.GetImage2(4,data);
			if (usersStorageService.testNameForAdding(user)){
				s.setMessage("Nesprávne meno alebo heslo !");;
			}
			else {
				try {
					BufferedImage image1 = new BufferedImage(320, 480, BufferedImage.TYPE_BYTE_GRAY);
					byte[] array = ((DataBufferByte) image1.getRaster().getDataBuffer()).getData();
					System.arraycopy(data, 0, array, 0, array.length);
					Date date = new Date();
					Timestamp timeStamp = new Timestamp(date.getTime());
					String uniqueTime = timeStamp.toString();
					String filepath = "/Users/user/Desktop/PoSam/posamSpring/posamSpringApp/fingerprints/" + user.getUsername() + uniqueTime + ".png";
					File outputfile = new File(filepath);
					ImageIO.write(image1, "png", outputfile);
					String b = usersStorageService.addFingerprintToUser(user,filepath);
					s.setMessage(b);
				}catch (IOException e){
					System.out.println(e);
				}
			}
		}
		scanner.CloseDevice();
		return s;
	}

	/*@PostMapping(value="/login",produces = "text/plain")
	public String scanlogin(@RequestBody() MessageDetail message) throws IOException {
		System.out.println("message.getMessage()");
		Scanner scanner = new Scanner();
		scanner.SetOptions(1, scanner.FTR_OPTIONS_INVERT_IMAGE);
		if(scanner.OpenDevice()){
			byte[] data = new byte[320*480];
			scanner.GetImage2(4,data);
			BufferedImage image1 = new BufferedImage(320, 480, BufferedImage.TYPE_BYTE_GRAY);
			byte[] array = ((DataBufferByte) image1.getRaster().getDataBuffer()).getData();
			System.arraycopy(data, 0, array, 0, array.length);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			ImageIO.write(image1 , "bmp", baos);
			byte[] bytes = baos.toByteArray();
			FingerprintTemplate probe = new FingerprintTemplate(
						new FingerprintImage((bytes), new FingerprintImageOptions().dpi(500)));
			scanner.CloseDevice();
			User doc = usersStorageService.findPerson(probe);
			if(doc != null && doc.getId()>0 ) {
				String sss = usersStorageService.getJWTToken(doc.getId());
				System.out.println(sss);
				return sss;
			}
		}
		return "" ;
	}*/

	@PostMapping("/home")
	public Integer scanlogin(@RequestBody MessageDetail message) throws IOException {
		System.out.println(message.getMessage());
		Scanner scanner = new Scanner();
		scanner.SetOptions(1, scanner.FTR_OPTIONS_INVERT_IMAGE);
		if(scanner.OpenDevice()&& scanner.IsFingerPresent()){
			byte[] data = new byte[320*480];
			scanner.GetImage2(4,data);
			BufferedImage image1 = new BufferedImage(320, 480, BufferedImage.TYPE_BYTE_GRAY);
			byte[] array = ((DataBufferByte) image1.getRaster().getDataBuffer()).getData();
			System.arraycopy(data, 0, array, 0, array.length);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			ImageIO.write(image1 , "bmp", baos);
			byte[] bytes = baos.toByteArray();
			FingerprintTemplate probe = new FingerprintTemplate(
					new FingerprintImage((bytes), new FingerprintImageOptions().dpi(500)));
			Integer docId = usersStorageService.findUser(probe);
			scanner.CloseDevice();
			System.out.println(docId);
			System.out.println("opene");
			/*if(doc != null && doc.getId()>0 ) {
				String sss = usersStorageService.getJWTToken(doc.getId());
				System.out.println(sss);
			}*/
			return docId;
		}
		System.out.println("ssss");
		scanner.CloseDevice();
		return 0;
	}


	// Zoznam pacientov daneho doktora, vzdy s nimi pracujem
	@GetMapping("/patients/{id}")
	public List<Patient> listOfPacients (@PathVariable Integer id){
		Optional<User> doctor = userRepository.findById(id);
		return (doctor.orElseThrow(EntityNotFoundException::new)).getPatients();
	}

	// Otvorenie detailu 1 pacienta, zobrazenie udajov
	@GetMapping ("/patient/{id}")
	public Patient showOne (@PathVariable Integer id){
		Optional<Patient> p = patientRepository.findById(id);
		return p.orElseThrow(EntityNotFoundException::new);
	}

	// NAVSTEVY, UPDATE & VYTVORENIE navstevy

	@PostMapping("/calendar/{id}/edit")
	public Visit updateVisit(@PathVariable Integer id, @RequestBody Visit visit) throws IOException {
 		Optional<Visit> v = visitRepository.findById(id);
		if(v.isPresent()){
			v.get().setDescription(visit.getDescription());
			v.get().setDateandtime(visit.getDateandtime());
			System.out.println("sss  "+v.get().getDateandtime());
			return visitRepository.save(v.get());
		}
 		return null;
	}

	@PostMapping("/calendar/{id}/add")
	public Patient addVisitToPatient(@PathVariable Integer id,@RequestBody Visit visit) throws IOException {
		Optional<Patient> p = patientRepository.findById(id);
		Visit newVisit = new Visit(visit.getDescription(),visit.getDateandtime());
		System.out.println(visit.getDateandtime());
		p.get().getVisits().add(newVisit);
		return patientRepository.save(p.get());
	}

	// PACIENT PROFILE, UPDATE & PRIDANIE pacienta

	@PostMapping("/patients/{id}/edit")
	public Patient updatePatient(@PathVariable Integer id,@RequestBody Patient patient) throws IOException {
		Optional<Patient> p = patientRepository.findById(id);
		p.get().setName(patient.getName());
		p.get().setId_number(patient.getId_number());
		return patientRepository.save(p.get());
	}

	@PostMapping("/patients/{id}/add")
	public Patient addPatient (@PathVariable Integer id,@RequestBody Patient patient){
		Optional <User> doctor = userRepository.findById(id);
		if(doctor.isPresent()) {
			if (usersStorageService.patientDuplicateCheck(doctor.get().getPatients(),patient.getId_number())) {
				doctor.get().getPatients().add(patient);
				Patient p = patientRepository.save(patient);
				userRepository.save(doctor.get());
				return p;
			}
		}
		return null;
	}

	// DOCTOR PROFILE, LOADING & UPDATE PROFILU

	@GetMapping("/doctors/{id}")
	public User doctor (@PathVariable Integer id){
		Optional<User> doctor = userRepository.findById(id);
		return (doctor.orElseThrow(EntityNotFoundException::new));
	}

	@GetMapping("/calendar/{id}/edit")
	public Visit getVisitById (@PathVariable Integer id){
		Optional<Visit> visit = visitRepository.findById(id);
		return visit.get();
	}

	@GetMapping("/calendar/{id}")
	public List<Patient> visits (@PathVariable Integer id){
		Optional<User> doctor = userRepository.findById(id);
		List<Patient> patients = doctor.get().getPatients();
		List<Patient> ps = new ArrayList<>();
		for (Patient p : patients){
			p.getVisits().removeIf(v -> v.getDateandtime().before(new Date()));
			ps.add(p);
		}
		return ps;
	}

	@PostMapping("/doctors/{id}/edit")
	public User updateDoctor(@PathVariable Integer id,@RequestBody User d) throws IOException {
		Optional<User> doctor = userRepository.findById(id);
		doctor.get().setName(d.getName());
		doctor.get().setUsername(d.getUsername());
		doctor.get().setDepartment(d.getDepartment());
		doctor.get().setPassword(d.getPassword());
		return userRepository.save(doctor.get());
	}


	@DeleteMapping("/doctor/{id}")
	void deleteAccount(@PathVariable Integer id) {
		userRepository.deleteById(id);
	}

	@DeleteMapping("/patient/{id}")
	void deletePatient(@PathVariable Integer id) {
		patientRepository.deleteById(id);
	}

	@DeleteMapping("/visit/{id}")
	void deleteVisit(@PathVariable Integer id) {
		visitRepository.deleteById(id);
	}


}