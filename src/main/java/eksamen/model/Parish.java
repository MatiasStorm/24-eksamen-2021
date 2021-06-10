package eksamen.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity(name="sogn")
public class Parish {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

    @Column(name="sogn_kode", unique = true)
    private int parishCode;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="kommune_kode")
    private Commune commune;

    @Column(name="smitte_niveau", nullable=true)
    private int infectionRate;

    @Column(name="navn", nullable=false, length=255)
    private String name;

    @Column(name="nedlukning", nullable=true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date closing;

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

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

    public int getInfectionRate() {
        return infectionRate;
    }

    public void setInfectionRate(int infectionRate) {
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
