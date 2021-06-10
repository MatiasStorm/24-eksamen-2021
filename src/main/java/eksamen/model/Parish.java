package eksamen.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity(name="sogn")
public class Parish {
    @Id
    @Column(name="sogn_kode")
    private int parishCode;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="kommune_kode")
    private Commune commune;

    @Column(name="smitte_niveau", nullable=true)
    private Double infectionRate;

    @Column(name="navn", nullable=false, length=255)
    private String name;

    @Column(name="nedlukning", nullable=true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date closing;

    public Parish(int parishCode, double infectionRate, String name, Date closing ){
        this.parishCode = parishCode;
        this.infectionRate = infectionRate;
        this.name = name;
        this.closing = closing;
    }

    public Parish() {  }

    public int getParishCode() {
        return parishCode;
    }

    public void setParishCode(int parishCode) {
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

    public Date getClosing() {
        return closing;
    }

    public void setClosing(Date closing) {
        this.closing = closing;
    }
}
