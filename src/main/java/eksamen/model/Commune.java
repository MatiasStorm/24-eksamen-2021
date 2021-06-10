package eksamen.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name="kommune")
public class Commune {
    @Id
    @Column(name="kommune_kode")
    private int communeCode;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commune")
    private Set<Parish> parishes;

    @Column(name="navn", nullable=false, length=255)
    private String name;

    public Commune(int communeCode, String name){
        this.communeCode = communeCode;
        this.name = name;
    }

    public Commune(int communeCode){
        this.communeCode = communeCode;
    }

    public Commune(){ }

    public int getCommuneCode() {
        return communeCode;
    }

    public void setCommuneCode(int communeCode) {
        this.communeCode = communeCode;
    }

    public Set<Parish> getParishes() {
        return parishes;
    }

    public void setParishes(Set<Parish> parishes) {
        this.parishes = parishes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
