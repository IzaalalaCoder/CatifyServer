package univ.rouen.fr.catify.server.entity;

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

    private String name;

    public Category() {

    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /*
    private final Date dateOfCreation;
    private Category parent;
    private final List<Category> children;


    // CONSTRUCTOR

    public Category(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.id = 0;
        this.name = name;
        this.dateOfCreation = new Date();
        this.parent = null;
        this.children = new ArrayList<>();
    }

    // REQUESTS

    public int getId() {
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

    @Override
    public String toString() {
        return "Category [id=" + id + ", name=" + name + ", dateOfCreation="
                + dateOfCreation + ", parent=" + parent + ", children=" + children + "]";
    }

    public int getNumberChildren() {
        return children.size();
    }

    public List<Category> getAllChildren() {
        return children;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Category parent) {
        this.parent = parent;
        if (parent != null)
            parent.addChild(this);
    }

    public void addChild(Category child) {
        this.children.add(child);
        child.setParent(this);
    }

    public void removeChild(Category child) {
        child.delete();
        this.children.remove(child);
        child.setParent(null);
    }

    public void delete() {
        while (!children.isEmpty()) {
            Category c = children.removeFirst();
            c.delete();
            c.setParent(null);
        }
    }*/
}

