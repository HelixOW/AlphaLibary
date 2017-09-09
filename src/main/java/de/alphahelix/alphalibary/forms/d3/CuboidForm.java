package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import de.alphahelix.alphalibary.forms.d2.RectangleForm;
import org.bukkit.entity.Player;

public class CuboidForm extends Form {

    private RectangleForm rectangleForm;
    private double width;
    private FormAction action;

    public CuboidForm(RectangleForm rectangleForm, double width, FormAction action) {
        super(rectangleForm.getLocation(), rectangleForm.getAxis(), rectangleForm.getDense());
        this.width = width;
        this.rectangleForm = rectangleForm;
        this.action = action;
    }

    public RectangleForm getRectangleForm() {
        return rectangleForm;
    }

    public CuboidForm setRectangleForm(RectangleForm rectangleForm) {
        this.rectangleForm = rectangleForm;
        return this;
    }

    public double getWidth() {
        return width;
    }

    public CuboidForm setWidth(double width) {
        this.width = width;
        return this;
    }

    public FormAction getAction() {
        return action;
    }

    public CuboidForm setAction(FormAction action) {
        this.action = action;
        return this;
    }

    @Override
    public void send(Player p) {
        new RectangleForm(getLocation(), getAxis(), getDense(), getRectangleForm().getLenght(), getRectangleForm().getHeight(), getRectangleForm().isFilled(), getAction()).send(p);
        new RectangleForm(getLocation().clone().add(0, 0, getWidth()), getAxis(), getDense(), getRectangleForm().getLenght(), getRectangleForm().getHeight(), getRectangleForm().isFilled(), getAction()).send(p);

        for (double w = getDense(); w < (getWidth() - getDense()); w += getDense()) {
            new RectangleForm(getLocation().clone().add(0, 0, w), getAxis(), getDense(), getRectangleForm().getLenght(), getRectangleForm().getHeight(), false, getAction()).send(p);
        }
    }
}
