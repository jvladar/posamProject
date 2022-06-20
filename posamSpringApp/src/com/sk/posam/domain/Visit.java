package sk.posam.domain;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="visit", schema = "public")
@Access(AccessType.FIELD)
public class Visit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "visit_id")
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "datetime")
    private Timestamp datetime;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    @JsonBackReference
    private Patient patient;

    public Visit(){
    }

    public Visit(String description, Timestamp datetime) {
        this.description = description;
        this.datetime = datetime;
    }


    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDateandtime() {
        return datetime;
    }

    public void setDateandtime(Timestamp datetime) {
        this.datetime = datetime;
    }


}




