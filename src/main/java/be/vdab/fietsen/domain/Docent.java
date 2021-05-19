package be.vdab.fietsen.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

//@NamedQuery(name = "Docent.findByWeddeBetween",
//        query = "select d from Docent d where d.wedde between :van and :tot order by d.wedde,d.id")
@Entity
@Table(name = "docenten")
@NamedEntityGraph(name = Docent.MET_CAMPUS,
        attributeNodes = @NamedAttributeNode("campus"))
/*@NamedEntityGraph(name = "Docent.metCampusEnVerantwoordelikjheden",
        attributeNodes = {@NamedAttributeNode("campus"), @NamedAttributeNode("verantwoordelijkheden")})*/
/*@NamedEntityGraph(
        name = "Docent.metCampusEnManager",
        attributeNodes = @NamedAttributeNode(value = "campus", subgraph = "metManager"),
        subgraphs = @NamedSubgraph(name = "metManager", attributeNodes = @NamedAttributeNode("manager")))*/
public class Docent {
    public static final String MET_CAMPUS = "Docent.metCampus";
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private String voornaam;
    private String familienaam;
    private BigDecimal wedde;
    private String emailAdres;
    @Enumerated(EnumType.STRING)
    private Geslacht geslacht;

    @ElementCollection
    @CollectionTable(name = "docentenbijnamen",
            joinColumns = @JoinColumn(name = "docentId"))
    @Column(name = "bijnaam")
    private Set<String> bijnamen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campusId")
    private Campus campus;

    @ManyToMany
    @JoinTable(
            name = "docentenverantwoordelijkheden",
            joinColumns = @JoinColumn(name = "docentId"),
            inverseJoinColumns = @JoinColumn(name = "verantwoordelijkheidId"))
    private Set<Verantwoordelijkheid> verantwoordelijkheden = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Docent)) return false;
        Docent docent = (Docent) o;
        return emailAdres.equalsIgnoreCase(docent.emailAdres);
    }

    @Override
    public int hashCode() {
        return emailAdres == null ? 0 : emailAdres.toLowerCase().hashCode();
    }

    public Docent(String voornaam, String familienaam,
                  BigDecimal wedde, String emailAdres, Geslacht geslacht, Campus campus) {
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.wedde = wedde;
        this.emailAdres = emailAdres;
        this.geslacht = geslacht;
        this.bijnamen = new LinkedHashSet<>();
        setCampus(campus);
    }

    protected Docent() {

    }

    public boolean add(Verantwoordelijkheid verantwoordelijkheid) {
        var toegevogd = verantwoordelijkheden.add(verantwoordelijkheid);
        if (!verantwoordelijkheid.getDocenten().contains(this)) {
            verantwoordelijkheid.add(this);
        }
        return toegevogd;
    }

    public boolean remove(Verantwoordelijkheid verantwoordelijkheid) {
        var verwijderd = verantwoordelijkheden.remove(verantwoordelijkheid);
        if (verantwoordelijkheid.getDocenten().contains(this)) {
            verantwoordelijkheid.remove(this);
        }
        return verwijderd;
    }

    public Set<Verantwoordelijkheid> getVerantwoordelijkheden() {
        return Collections.unmodifiableSet(verantwoordelijkheden);
    }

    public Set<String> getBijnamen() {
        return Collections.unmodifiableSet(bijnamen);
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        if (!campus.getDocenten().contains(this)) {
            campus.add(this);
        }
        this.campus = campus;
    }

    public boolean addBijnaam(String bijnaam) {
        if (bijnaam.isBlank()) {
            throw new IllegalArgumentException();
        }
        return this.bijnamen.add(bijnaam);
    }

    public boolean removeBijnaam(String bijnaam) {
        return bijnamen.remove(bijnaam);
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
