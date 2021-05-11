package be.vdab.fietsen.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

//@NamedQuery(name = "Docent.findByWeddeBetween",
//        query = "select d from Docent d where d.wedde between :van and :tot order by d.wedde,d.id")
@Entity
@Table(name = "docenten")
public class Docent {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private String voornaam;
    private String familienaam;
    private BigDecimal wedde;
    private String emailAdres;
    @Enumerated(EnumType.STRING)
    private Geslacht geslacht;

    public Docent( String voornaam, String familienaam,
                  BigDecimal wedde, String emailAdres, Geslacht geslacht) {
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.wedde = wedde;
        this.emailAdres = emailAdres;
        this.geslacht = geslacht;
    }

    protected Docent() {

    }


    public void opslag(BigDecimal percentage) {
        if (percentage.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
        var factor = (BigDecimal.valueOf(100).add(percentage)).divide(BigDecimal.valueOf(100));
        this.wedde = wedde.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    public Geslacht getGeslacht() {
        return geslacht;
    }

    public long getId() {
        return id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public BigDecimal getWedde() {
        return wedde;
    }

    public String getEmailAdres() {
        return emailAdres;
    }
}
