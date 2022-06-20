package sk.posam.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="doctor", schema = "public")
@Access(AccessType.FIELD)
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "user_id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "username")
	private String username;

	@Column(name = "pass")
	private String password;

	@Column(name = "department")
	private Department department;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "doctor_id",referencedColumnName = "user_id")
	private List<Fingerprint> fingerprints = new ArrayList<>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "doctor_patient",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "patient_id")
	)
	private List<Patient> patients = new ArrayList<>();


	public User() {}

	public User(String name, String username, String password, Department department) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.department = department;
	}

	public Integer getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Fingerprint> getFingerprints() {
		return fingerprints;
	}

	public void setFingerprints(List<Fingerprint> fingerprints) {
		this.fingerprints = fingerprints;
	}

	public List<Patient> getPatients() {
		return patients;
	}

	public void setPatients(List<Patient> patients) {
		this.patients = patients;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", department=" + department +
				'}';
	}
}
