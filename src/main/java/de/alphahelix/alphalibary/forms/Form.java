package de.alphahelix.alphalibary.forms;

import com.google.common.base.Objects;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.Arrays;

public abstract class Form implements Serializable {

    private FormFunction[] formFunctions;
    private Location location;
    private String axis;
    private double dense;

    public Form(Location location, String axis, double dense, FormFunction... formFunctions) {
        this.formFunctions = formFunctions;
        this.location = location;
        this.axis = axis;
        this.dense = dense;
    }

    public FormFunction[] getFormFunctions() {
        return formFunctions;
    }

    public Form setParticleFunctions(FormFunction... functions) {
        this.formFunctions = functions;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public Form setLocation(Location location) {
        this.location = location;
        return this;
    }

    public String getAxis() {
        return axis;
    }

    public Form setAxis(String axis) {
        this.axis = axis;
        return this;
    }

    public double getDense() {
        return dense;
    }

    public Form setDense(double dense) {
        this.dense = dense;
        return this;
    }

    public abstract void send(Player p);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Form form = (Form) o;
        return Double.compare(form.getDense(), getDense()) == 0 &&
                Objects.equal(getFormFunctions(), form.getFormFunctions()) &&
                Objects.equal(getLocation(), form.getLocation()) &&
                Objects.equal(getAxis(), form.getAxis());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getFormFunctions(), getLocation(), getAxis(), getDense());
    }

    @Override
    public String toString() {
        return "Form{" +
                "formFunctions=" + Arrays.toString(formFunctions) +
                ", location=" + location +
                ", axis='" + axis + '\'' +
                ", dense=" + dense +
                '}';
    }
}
