package ifutureTask.controller;

import ifutureTask.entity.Node;
import ifutureTask.service.CutException;
import ifutureTask.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @Autowired
    public NodeService nodeService;

   @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView main() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        Node tree;
        if (nodeService.getTree() != null) {
            tree = nodeService.getTree();
        } else {
            System.out.println("Дерево отсутствует. Построение нового тестового дерева...");
            nodeService.makeNewTestTree();
            tree = nodeService.getTree();
            System.out.println("Дерево построено");
        }
        tree.display("   ");
        modelAndView.addObject( "node", tree);
        return modelAndView;
    }

    @RequestMapping(value = "/build", method = RequestMethod.GET)
    public ModelAndView buildTree() {
        nodeService.makeNewTestTree();
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView deleteNodeOp(@RequestParam("id") String nodeId) {
        nodeService.deleteNode(nodeId);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView addNodeOp(@RequestParam("id") String parentId, @RequestParam("name") String nameOfNode) {
       nodeService.addNode(parentId, nameOfNode);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/copy", method = RequestMethod.GET)
    public ModelAndView copyNodeOp(@RequestParam("id") String nodeToCopyId, @RequestParam("parent") String parentId) {
        nodeService.copyNode(parentId, nodeToCopyId);
        return new ModelAndView("redirect:/");
    }


    @RequestMapping(value = "/cut", method = RequestMethod.GET)
    public ModelAndView cutNodeOp(@RequestParam("id") String nodeToCutId, @RequestParam("parent") String parentId) {
        System.out.println("Узел для копирования CUT: " + nodeToCutId + " Родитель: " + parentId);
        boolean error = false;
        try {
            nodeService.cutNode(parentId, nodeToCutId);
        } catch (CutException e) {
            error = true;
        }
        return error? new ModelAndView("error") : new ModelAndView("success");
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView getTest() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        return modelAndView;
    }



}