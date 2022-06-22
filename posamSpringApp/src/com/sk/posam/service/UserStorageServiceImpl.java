package sk.posam.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import sk.posam.domain.*;
import sk.posam.repositories.FingerCrudRepository;
import sk.posam.repositories.PatientCrudRepository;
import sk.posam.repositories.UserCrudRepository;
import sk.posam.repositories.VisitCrudRepository;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintImageOptions;
import com.machinezoo.sourceafis.FingerprintTemplate;
import com.machinezoo.sourceafis.FingerprintMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStorageServiceImpl implements UserStorageService {

  	@Autowired
  	private UserCrudRepository userRepository;

	@Autowired
	private FingerCrudRepository fingerRepository;

	@Autowired
	private VisitCrudRepository visitRepository;

	@Autowired
	private PatientCrudRepository patientRepository;

	@Override
	public void saveUser(User user, String filepath){
		if(user.getUsername() != null) {
			Fingerprint f = new Fingerprint(filepath);
			user.getFingerprints().add(f);
			userRepository.save(user);
		}
	}

	@Override
	public String addFingerprintToUser(User user, String filepath){
		Iterable<User> users = userRepository.findAll();
		for(User u : users){
			if(u.getUsername().equals(user.getUsername())){
				Fingerprint f = new Fingerprint(filepath);
				u.getFingerprints().add(f);
				userRepository.save(u);
				return "Odtlačok bol úspešne naskenovaný a priradený ku vašému menu: " + user.getUsername();
			}
		}
		return null;
	}
	@Override
	public Integer findUser (FingerprintTemplate fingerprint) throws IOException{
		Iterable<Fingerprint> fingers = fingerRepository.findAll();
		FingerprintMatcher matcher = new FingerprintMatcher(fingerprint);
		Fingerprint fingermatch = null;
		double high = 0;
		for (Fingerprint fprint : fingers) {
			FingerprintTemplate fprintImage = new FingerprintTemplate(new FingerprintImage(Files.readAllBytes(Paths.get(fprint.getData())),new FingerprintImageOptions().dpi(500)));
			double score = matcher.match(fprintImage);
			if (score > high) {
				high = score;
				fingermatch = fprint;
			}
		}
		double threshold = 40;
		if (high > threshold){
			User doctor = findUserByFingerprintId(fingermatch.getId());
			return doctor.getId();
		}
		return 0;

	}
	@Override
	public boolean patientDuplicateCheck (List<Patient> ps, long patient_number){
		for (Patient t : ps){
			if(t.getId_number()==patient_number){
				return false;
			}
		}
		return true;
	}

	@Override
	public User findUserByFingerprintId(Integer id) {
		try{
			Iterable<User> users = userRepository.findAll();
			for(User user : users){
				List<Fingerprint> fprints = user.getFingerprints();
				for(Fingerprint fprint : fprints){
					if(fprint.getId().equals(id)){
						return user;
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean testNameRegistration(String username){
		Iterable<User> users = userRepository.findAll();
		for (User u : users){
			if (u.getUsername().equals(username)) return false;
		}
		return true;
	}

	@Override
	public boolean testNameForAdding(User user){
		Iterable<User> users = userRepository.findAll();
		for (User u : users){
			if (u.getUsername().equals(user.getUsername())){
				if(u.getPassword().equals(user.getPassword()))
					return false;
			}
		}
		return true;
	}

	/*public String getJWTToken(Integer docId) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");

		String token = Jwts
				.builder()
				.setSubject(String.valueOf(docId))
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}*/

/*	version: '2'
	services:
	postgres-standalone:
	image: 'postgres:latest'
	container_name: postgres-standalone
	environment:
			- POSTGRES_DB=biometricapp
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=password
	expose:
			- "1234" # Publishes 5433 to other containers but NOT to host machine
	ports:
			- "1234:1234"
	volumes:
			- ./backups:/home/backups
	command: -p 1234
	springboot-docker-container:
	image: springboot-docker-container
	container_name: springboot-docker-container
	ports:
			- 8080:8080
	build:
	context: ./
	dockerfile: Dockerfile
	depends_on:
			- postgres-standalone*/

	/*spring:
	datasource:
	driver-class-name: org.postgresql.Driver
	url: jdbc:postgresql://postgre-standalone:1234/biometricapp
	username: postgres
	password: password
	hikari:
	initialization-fail-timeout: 0
	jpa:
	database-platform: org.hibernate.dialect.PostgreSQLDialect
	generate-ddl: true
	show-sql: true
	hibernate:
	ddl-auto: update*/

	/*#FROM openjdk:8-jre-alpine3.9
	#ADD target/BlobDemo-0.0.1-SNAPSHOT.jar app.jar
	#ENTRYPOINT ["java","-jar","app.jar"]
	#CMD ["-p", "5433"]*/

}
