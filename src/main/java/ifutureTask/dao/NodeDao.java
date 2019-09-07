package ifutureTask.dao;

import ifutureTask.entity.Node;

import java.util.List;

public interface NodeDao {

    void makeNewTestTree();

    Node getTree();

    void deleteTree();

    void addNode(Node nodeToAdd);

    void deleteNode(Node nodeToDelete);

    void copyNode(Node nodeToCopy, Node parent);

    void cutNode(Node nodeToCut, Node Parent);

}
