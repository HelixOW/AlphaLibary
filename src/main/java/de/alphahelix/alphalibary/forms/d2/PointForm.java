package de.alphahelix.alphalibary.forms.d2;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PointForm extends Form {

    private final double distance;
    private final double slope;
    private FormAction action;

    public PointForm(Location location, String axis, double dense, FormAction action, Point p1, Point p2) {
        super(location, axis, dense);
        this.action = action;

        double dSquared = (p2.x - p1.x) * (p2.x - p1.x) - (p2.y - p1.y) * (p2.y - p1.y);

        distance = Math.sqrt(Math.abs(dSquared));
        slope = p1.y / p1.x;
    }

    public double getDistance() {
        return distance;
    }

    public FormAction getAction() {
        return action;
    }

    public PointForm setAction(FormAction action) {
        this.action = action;
        return this;
    }

    @Override
    public void send(Player p) {
        new RayForm(getLocation(), getAxis(), getDense(), distance, getAction(),
                x -> slope * x[0]).send(p);
    }

    public static class Point {
        private final double x;
        private final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return Double.compare(point.x, x) == 0 &&
                    Double.compare(point.y, y) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(x, y);
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
