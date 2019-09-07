package ifutureTask.service;

import ifutureTask.dao.NodeDao;
import ifutureTask.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@Service
public class NodeServiceImpl implements NodeService {

    @Autowired
    public NodeDao nodeDao;

    @Override
    public void makeNewTestTree() {
        nodeDao.makeNewTestTree();
    }

    @Override
    public Node getTree() {
       return nodeDao.getTree();
    }

    @Override
    public void deleteTree() {
        nodeDao.deleteTree();
    }

    @Override
    public void deleteNode(String nodeToDeleteId) {
        Node tree = nodeDao.getTree();
        Node nodeToDelete = findNodeById(tree, nodeToDeleteId);
        nodeDao.deleteNode(nodeToDelete);
    }

    @Override
    public void addNode(String destinationParentId, String name) {
        Node tree = nodeDao.getTree();
        Node parent = findNodeById(tree, destinationParentId);
        Node child = parent.addChild(name);
        nodeDao.addNode(child);
    }

    @Override
    public void copyNode(String destinationParentId, String nodeToCopyId) {
        Node tree = nodeDao.getTree();
        Node parent = findNodeById(tree, destinationParentId);
        Node nodeToCopy = findNodeById(tree, nodeToCopyId);
        nodeDao.copyNode(nodeToCopy, parent);
    }

    @Override
    public void cutNode(String destinationParentId, String nodeToCutId) throws CutException {
        Node tree = nodeDao.getTree();
        Node parent = findNodeById(tree, destinationParentId);
        Node nodeToCut = findNodeById(tree, nodeToCutId);
        String parentId = "" + parent.getId();
        int test;
        if((test = findNodeById(nodeToCut, parentId).getId()) != Integer.parseInt(destinationParentId)) {
            nodeDao.cutNode(nodeToCut, parent);
        } else {
            System.out.println(test);
            throw new CutException("Перемещение в дочерний каталог невозможно");
        }

    }

    private Node findNodeById(Node root, String id) {
        if(!root.getChildren().isEmpty()) {
            for(Node node : root.getChildren()) {
                if(root.getId() == Integer.parseInt(id)) {
                    return root;
                } else {
                    root =  findNodeById(node, id);
                }
            }
        }
        return root;
    }



}
