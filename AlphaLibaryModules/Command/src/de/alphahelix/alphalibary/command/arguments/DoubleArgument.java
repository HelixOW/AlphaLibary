package de.alphahelix.alphalibary.command.arguments;

public class DoubleArgument extends Argument<Double> {
	@Override
	public boolean matches() {
		try {
			Double.parseDouble(getEnteredArgument());
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	@Override
	public Double fromArgument() {
		if(matches())
			return Double.parseDouble(getEnteredArgument());
		return 0.0;
	}
}
