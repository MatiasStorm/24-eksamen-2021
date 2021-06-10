package eksamen.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.validator.constraints.NotEmpty;

@Entity(name="sogn")
public class Parish {
    @Id
    @Column(name="sogn_kode")
    @Min(1)
    private Integer parishCode;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="kommune_kode")
    private Commune commune;

    @Column(name="smitte_niveau", nullable=true)
    private Double infectionRate;

    @Column(name="navn", nullable=false, length=255)
    private String name;

    @Column(name="nedlukning", nullable=true)
    private LocalDate closing;

    public Parish(int parishCode, double infectionRate, String name, LocalDate closing ){
        this.parishCode = parishCode;
        this.infectionRate = infectionRate;
        this.name = name;
        this.closing = closing;
    }

    public Parish() {  }

    public long getParishCode() {
        return parishCode;
    }

    public void setParishCode(Integer parishCode) {
        this.parishCode = parishCode;
    }

    public Commune getCommune() {
        return commune;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    public double getInfectionRate() {
        return infectionRate;
    }

    public void setInfectionRate(Double infectionRate) {
        this.infectionRate = infectionRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getClosing() {
        return closing;
    }

    public void setClosing(LocalDate closing) {
        this.closing = closing;
    }
}
