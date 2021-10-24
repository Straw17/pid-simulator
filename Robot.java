public class Robot {
	final double maxA; //maximum acceleration [m/s^2]
	final double minA; //minimum acceleration [m/s^2]
	final double maxV; //maximum velocity [m/s]
	final PIDLoop PID;
	
	double position; //starting position [m]
	double target; //target position [m]
	double currentV = 0; //current velocity [m/s]
	
	public Robot(double maxA, double minA, double v, double start, PIDLoop PID) {
		this.maxA = maxA;
		this.minA = minA;
		this.maxV = v;
		this.position = start;
		this.target = start;
		this.PID = PID;
	}
	
	public void setTarget(double target) {
		this.target = target;
	}
	
	public void update() {
		//System.out.println("Position: " + position);
		
		double v = PID.calculate(position - target);
		//System.out.println("Output: " + v);
		if(Math.abs(v) > maxV) {
			if(v > 0) {
				v = maxV;
			} else {
				v = -maxV;
			}
		}
		
		double changeV = (v - currentV) / (Simulator.interval);
		if(Math.abs(changeV) < 0.001) {
		} else if(Math.abs(changeV) > maxA) {
			if(changeV > 0) {
				currentV += maxA * Simulator.interval;
			} else {
				currentV -= maxA * Simulator.interval;
			}
		} else if(Math.abs(changeV) < minA) {
			if(changeV > 0) {
				currentV += minA * Simulator.interval;
			} else {
				currentV -= minA * Simulator.interval;
			}
		} else {
			currentV = v;
		}
		
		//System.out.println("Velocity: " + currentV);
		
		position += currentV * Simulator.interval;
	}
}