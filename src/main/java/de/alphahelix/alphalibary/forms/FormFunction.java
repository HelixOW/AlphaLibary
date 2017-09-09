package de.alphahelix.alphalibary.forms;

public interface FormFunction {

    double f(double... x);

    default double x(double... f) {
        return f(f);
    }

    default double f1(double... x) {
        return f(x);
    }
}
