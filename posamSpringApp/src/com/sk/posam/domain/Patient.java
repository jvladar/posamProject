package sk.posam.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="patient", schema = "public")
@Access(AccessType.FIELD)
public class Patient implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "patient_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "id_number")
    private long id_number;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "id_patient",referencedColumnName = "patient_id")
    private List<Visit> visits = new ArrayList<>();

    @ManyToMany(mappedBy = "patients", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    public Patient() {
    }

    public Patient(String name, Integer id_number) {
        this.name = name;
        this.id_number = id_number;
    }

    public Patient(Integer id, String name, Integer id_number,Visit v) {
        this.id = id;
        this.name = name;
        this.id_number = id_number;
        this.visits.add(v);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId_number() {
        return id_number;
    }

    public void setId_number(long id_number) {
        this.id_number = id_number;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
