package soniaandrianow.orthguideserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Style.
 */
@Entity
@Table(name = "style")
public class Style implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "church_style")
    @JsonIgnore
    private Set<Church> style_churches = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Style name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Church> getStyle_churches() {
        return style_churches;
    }

    public Style style_churches(Set<Church> churches) {
        this.style_churches = churches;
        return this;
    }

    public Style addStyle_church(Church church) {
        style_churches.add(church);
        church.setChurch_style(this);
        return this;
    }

    public Style removeStyle_church(Church church) {
        style_churches.remove(church);
        church.setChurch_style(null);
        return this;
    }

    public void setStyle_churches(Set<Church> churches) {
        this.style_churches = churches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Style style = (Style) o;
        if(style.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, style.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Style{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
