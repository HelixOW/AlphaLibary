package de.alphahelix.alphalibary.forms;

public interface FormFunction {
	
	default double x(double... f) {
		return f(f);
	}
	
	double f(double... x);
	
	default double f1(double... x) {
		return f(x);
	}
}
