package soniaandrianow.orthguideserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Diocese.
 */
@Entity
@Table(name = "diocese")
public class Diocese implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "diocese_church")
    @JsonIgnore
    private Set<Church> church_diocese = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Diocese name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Church> getChurch_diocese() {
        return church_diocese;
    }

    public Diocese church_diocese(Set<Church> churches) {
        this.church_diocese = churches;
        return this;
    }

    public Diocese addChurch_diocese(Church church) {
        church_diocese.add(church);
        church.setDiocese_church(this);
        return this;
    }

    public Diocese removeChurch_diocese(Church church) {
        church_diocese.remove(church);
        church.setDiocese_church(null);
        return this;
    }

    public void setChurch_diocese(Set<Church> churches) {
        this.church_diocese = churches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Diocese diocese = (Diocese) o;
        if(diocese.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, diocese.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Diocese{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
