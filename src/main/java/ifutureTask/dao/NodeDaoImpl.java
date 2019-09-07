package ifutureTask.dao;

import ifutureTask.entity.Node;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;

//@Repository
public class NodeDaoImpl implements NodeDao {

    public final SessionFactory factory;

    @Autowired
    public NodeDaoImpl(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void makeNewTestTree() {
        deleteTree();
        Session session;
        boolean committed;
        org.hibernate.Transaction transaction;
        Node root, child1, child2, grandchild11, grandchild12, grandchild13, grandchild21, grandchild22, greatGrandchild121;
        session = factory.openSession();
        try {
            committed = false;
            transaction = session.beginTransaction();
            try {
                root = Node.createRoot("MyRootData");
                child1 = root.addChild("MyChild1Data ");
                grandchild11 = child1.addChild("MyGrandchild11Data");
                grandchild12 = child1.addChild("MyGrandchild12Data");
                child2 = root.addChild("MyChild2Data");
                grandchild21 = child2.addChild("MyGrandchild21Data");
                grandchild22 = child2.addChild("MyGrandchild22Data");
                grandchild13 = child1.addChild("MyGrandchild13Data");
                greatGrandchild121 = grandchild12.addChild("MyGreatGrandchild121Data");
                session.save(root);
                transaction.commit();
                committed = true;
            } finally {
                if (!committed) {
                    transaction.rollback();
                }
            }
        } finally {
            session.close();
        }
        return;
    }

    @Override
    public Node getTree() {
        Session session = factory.openSession();
        Node root = null;
        try {
            root = session.createQuery("from Node where parent = null", Node.class)
                .getResultList().stream().findFirst().get();
        } catch(NoSuchElementException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return root;
    }

    @Override
    public void deleteTree() {
        boolean committed;
        Node root;
        Session session = factory.openSession();
        try {
            committed = false;
            Transaction transaction = session.beginTransaction();
            try {
                root = session.createQuery("from Node where parent = null", Node.class)
                        .getResultList().stream().findFirst().get();

                session.delete(root);
                transaction.commit();
                committed = true;
            } catch(NoSuchElementException e) {
                e.printStackTrace();
            }
            finally {
                if(!committed) {
                    transaction.rollback();
                }
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void addNode (Node nodeToAdd) {
        boolean committed;
        Session session = factory.openSession();
        try {
            committed = false;
            Transaction transaction = session.beginTransaction();
            try {
                session.save(nodeToAdd);
                transaction.commit();
                committed = true;
            }
            finally {
                if(!committed) {
                    transaction.rollback();
                }
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteNode(Node nodeToDelete) {
        boolean committed;
        Session session = factory.openSession();
        try {
            committed = false;
            Transaction transaction = session.beginTransaction();
            try {
                session.delete(nodeToDelete);
                transaction.commit();
                committed = true;
            } catch(NoSuchElementException e) {
                e.printStackTrace();
            }
            finally {
                if(!committed) {
                    transaction.rollback();
                }
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void copyNode(Node nodeToAdd, Node parent) {
        boolean committed;
        Node nodeInSession;
        Session session = factory.openSession();
        try {
            committed = false;
            Transaction transaction = session.beginTransaction();
            try {
                nodeInSession = session.createQuery("from Node where id = :par", Node.class)
                        .setParameter("par", parent.getId())
                        .getResultList().stream().findFirst().get();
                Node child = nodeInSession.addChild(nodeToAdd.getName());
                nodeInSession = recursiveNodeRepeater(nodeToAdd, child);
                session.save(nodeInSession);
                transaction.commit();
                committed = true;
            }
            finally {
                if(!committed) {
                    transaction.rollback();
                }
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void cutNode(Node nodeToCut, Node parent) {
        deleteNode(nodeToCut);
        boolean committed;
        Node nodeInSession;
        Session session = factory.openSession();
        try {
            committed = false;
            Transaction transaction = session.beginTransaction();
            try {
                nodeInSession = session.createQuery("from Node where id = :par", Node.class)
                        .setParameter("par", parent.getId())
                        .getResultList().stream().findFirst().get();

                Node child = nodeInSession.addChild(nodeToCut.getName());
                nodeInSession = recursiveNodeRepeater(nodeToCut, child);
                session.save(nodeInSession);
                transaction.commit();
                committed = true;
            }
            finally {
                if(!committed) {
                    transaction.rollback();
                }
            }
        } finally {
            session.close();
        }
    }

    private Node recursiveNodeRepeater(Node root, Node newNode) {
        if(!root.getChildren().isEmpty()) {
            for(Node node : root.getChildren()) {
                Node child = newNode.addChild(node.getName());
                newNode = recursiveNodeRepeater(node, child);
            }
        }
        return newNode.getParent();
    }

}
