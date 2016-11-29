package soniaandrianow.orthguideserver.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Church.
 */
@Entity
@Table(name = "church")
public class Church implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @NotNull
    @Column(name = "fete", nullable = false)
    private String fete;

    @NotNull
    @Column(name = "dedication", nullable = false)
    private String dedication;

    @NotNull
    @Column(name = "parson", nullable = false)
    private String parson;

    @NotNull
    @Column(name = "century", nullable = false)
    private Integer century;

    @NotNull
    @Column(name = "wooden", nullable = false)
    private Boolean wooden;

    @Column(name = "services")
    private String services;

    @NotNull
    @Column(name = "short_history", nullable = false)
    private String short_history;

    @Column(name = "photos")
    private String photos;

    @ManyToOne
    @NotNull
    private Diocese diocese_church;

    @ManyToOne
    @NotNull
    private Style church_style;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public Church address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Church longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Church latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getFete() {
        return fete;
    }

    public Church fete(String fete) {
        this.fete = fete;
        return this;
    }

    public void setFete(String fete) {
        this.fete = fete;
    }

    public String getDedication() {
        return dedication;
    }

    public Church dedication(String dedication) {
        this.dedication = dedication;
        return this;
    }

    public void setDedication(String dedication) {
        this.dedication = dedication;
    }

    public String getParson() {
        return parson;
    }

    public Church parson(String parson) {
        this.parson = parson;
        return this;
    }

    public void setParson(String parson) {
        this.parson = parson;
    }

    public Integer getCentury() {
        return century;
    }

    public Church century(Integer century) {
        this.century = century;
        return this;
    }

    public void setCentury(Integer century) {
        this.century = century;
    }

    public Boolean isWooden() {
        return wooden;
    }

    public Church wooden(Boolean wooden) {
        this.wooden = wooden;
        return this;
    }

    public void setWooden(Boolean wooden) {
        this.wooden = wooden;
    }

    public String getServices() {
        return services;
    }

    public Church services(String services) {
        this.services = services;
        return this;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getShort_history() {
        return short_history;
    }

    public Church short_history(String short_history) {
        this.short_history = short_history;
        return this;
    }

    public void setShort_history(String short_history) {
        this.short_history = short_history;
    }

    public String getPhotos() {
        return photos;
    }

    public Church photos(String photos) {
        this.photos = photos;
        return this;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public Diocese getDiocese_church() {
        return diocese_church;
    }

    public Church diocese_church(Diocese diocese) {
        this.diocese_church = diocese;
        return this;
    }

    public void setDiocese_church(Diocese diocese) {
        this.diocese_church = diocese;
    }

    public Style getChurch_style() {
        return church_style;
    }

    public Church church_style(Style style) {
        this.church_style = style;
        return this;
    }

    public void setChurch_style(Style style) {
        this.church_style = style;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Church church = (Church) o;
        if(church.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, church.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Church{" +
            "id=" + id +
            ", address='" + address + "'" +
            ", longitude='" + longitude + "'" +
            ", latitude='" + latitude + "'" +
            ", fete='" + fete + "'" +
            ", dedication='" + dedication + "'" +
            ", parson='" + parson + "'" +
            ", century='" + century + "'" +
            ", wooden='" + wooden + "'" +
            ", services='" + services + "'" +
            ", short_history='" + short_history + "'" +
            ", photos='" + photos + "'" +
            '}';
    }
}
