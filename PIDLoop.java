public class PIDLoop {
	final double P, I, D;
	double errorSum = 0;
	double lastError = 0;
	boolean isFirstLoop = true;
	
	public PIDLoop(double proportional, double integral, double derivative) {
		P = proportional;
		I = integral;
		D = derivative;
	}
	
	public double getLastError(double error) {
		if(isFirstLoop) {
			return error;
		} else {
			return lastError;
		}
	}
	
	public double calculate(double error) {
		errorSum += ((getLastError(error) + error)/2) * Simulator.interval; //trapezoidal reimann sum
		double errorChange = (error - getLastError(error)) / Simulator.interval;
		double result = (P * error) + (I * errorSum) + (D * errorChange);
		lastError = error;
		
		if(isFirstLoop) {
			isFirstLoop = false;
		}
		
		return -result;
	}
}
