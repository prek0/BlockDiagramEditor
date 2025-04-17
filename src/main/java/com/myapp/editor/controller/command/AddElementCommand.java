package controller.command;

import model.DiagramElement;
import model.DiagramModel;
import view.DiagramView;

public class AddElementCommand implements Command {
    private DiagramModel model;
    private DiagramElement element;
    private DiagramView view;

    public AddElementCommand(DiagramModel model, DiagramElement element, DiagramView view) {
        this.model = model;
        this.element = element;
        this.view = view;
    }

    @Override
    public void execute() {
        model.addElement(element);
        view.getDiagramPanel().repaint();
    }

    @Override
    public void undo() {
        model.removeElement(element);
        view.getDiagramPanel().repaint();
    }
}
