package ifutureTask.entity;

import javax.persistence.*;
import java.util.*;


@Entity
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Node parent;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Node> children = new ArrayList<Node>();

    public Node() {

    }

    private Node(String root) {
        parent = null;
        name = root;
    }

    private Node(Node parent, String name) {
        if(parent == null) {
            throw new IllegalArgumentException("Parent required!");
        }
        this.parent = parent;
        this.name = name;
        registerInParentsChild();
    }

    public Node addChild(String name) {
        return new Node(this, name);
    }

    public void display(String margin)
    {
        System.out.println(margin + name);
        for (Node child : children)
        {
            child.display(margin + "   ");
        }
    }

    private void registerInParentsChild() {
        parent.children.add(this);
    }

    public List<Node> getChildren() {
        return children;
    }

    public static Node createRoot(String name) {
        return new Node(name);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }


}