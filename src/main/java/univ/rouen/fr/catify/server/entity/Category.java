package univ.rouen.fr.catify.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Category {

    // ATTRIBUTES

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;
    private final Date dateOfCreation;

    @ManyToOne
    @JsonBackReference
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private final List<Category> children;

    // CONSTRUCTORS

    public Category() {
        this.dateOfCreation = new Date();
        this.children = new ArrayList<>();
    }

    public Category(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.dateOfCreation = new Date();
        this.children = new ArrayList<>();
        this.name = name;
        this.parent = null;
    }

    public Category(String name, Category parent) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.dateOfCreation = new Date();
        this.children = new ArrayList<>();
        this.name = name;
        setParent(parent);
    }

    // REQUESTS

    public Integer getParentId() {
        return this.parent == null ? null : this.parent.getId();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public Category getParent() {
        return parent;
    }

    public Boolean isRoot() {
        return parent == null;
    }

    public Integer getNumberChildren() {
        return children.size();
    }

    @JsonIgnore
    public List<Category> getAllChildren() {
        return new ArrayList<>(children);
    }

    public Category getChildrenAtIndex(int index) {
        return children.get(index);
    }

    public Category getChild(String name) {
        for (Category child : children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    // COMMANDS

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setParent(Category parent) {
        if (parent != null && parent.equals(this)) {
            throw new IllegalArgumentException("A category cannot be its own parent");
        }
        if (this.parent != null) {
            this.parent.children.remove(this);
        }
        this.parent = parent;
        if (parent != null && !parent.children.contains(this)) {
            parent.children.add(this);
        }
    }

    public void addChild(Category child) {
        if (child == null) {
            throw new IllegalArgumentException("Child cannot be null");
        }
        if (!this.children.contains(child)) {
            this.children.add(child);
            child.setParent(this);
        }
    }

    public void removeChild(Category child) {
        if (child == null) {
            throw new IllegalArgumentException("Child cannot be null");
        }
        if (children.remove(child)) {
            child.setParent(null);
        }
    }

    public void setAllChild(List<Category> children) {
        this.removeAllChild();
        for (Category c : children) {
            this.addChild(c);
        }
    }

    public void removeAllChild() {
        for (Category c : this.children) {
            this.removeChild(c);
        }
    }

    public void clear() {
        this.children.clear();
    }

    public void copyFrom(Category source) {
        this.name = source.name;
        this.parent = source.parent;
        this.children.clear();
        this.children.addAll(source.getAllChildren());
    }
}