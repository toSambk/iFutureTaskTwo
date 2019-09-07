package ifutureTask.service;

import ifutureTask.entity.Node;

import java.util.List;

public interface NodeService {

    void makeNewTestTree();

    Node getTree();

    void deleteTree();

    void deleteNode(String nodeToDeleteId);

    void addNode(String destinationParentId, String nameOfNode);

    void copyNode(String destinationParentId, String nodeToCopyId);

    void cutNode(String destinationParentId, String nodeToCutId) throws CutException;

}
