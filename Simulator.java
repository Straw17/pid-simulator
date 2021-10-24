import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Simulator extends Application {
	public static final double interval = 0.001; //[s]
	public static final int cycles = 4000;
	double[] positions = new double[cycles];
	double[] targets = new double[cycles];
	
	public double getTarget(double time) {
		return 1;
	}
	
	@Override
	public void start(Stage stage) {
		stage.setTitle("PID Display");
        final NumberAxis xAxis = new NumberAxis(0, 4, 0.1);
        final NumberAxis yAxis = new NumberAxis(0, 2, 0.1);        
        final ScatterChart<Number,Number> sc = new ScatterChart<Number,Number>(xAxis,yAxis);
        xAxis.setLabel("Seconds");                
        yAxis.setLabel("Meters");
        sc.setTitle("Position vs Time");
        
        //---------------------------------------------------------------------------
		
		Simulator sim = new Simulator();
		
		double P = 7;
		double I = 0;
		double D = 0.55;
		PIDLoop PID = new PIDLoop(P, I, D);
		
		double maxA = 5; //maximum acceleration [m/s^2]
		double minA = 1; //minimum acceleration [m/s^2]
		double maxV = 5; //maximum velocity [m/s]
		double p = 0; //starting position [m]
		Robot robot = new Robot(maxA, minA, maxV, p, PID);
		
		double target; //[m]
		
		for(int i = 0; i < cycles; i++) {
			target = sim.getTarget(i * interval);
			robot.setTarget(target);
			sim.targets[i] = target;
			sim.positions[i] = robot.position;
			robot.update();
		}
		
		//---------------------------------------------------------------------------
        
        XYChart.Series positionSeries = new XYChart.Series();
        positionSeries.setName("P");
        XYChart.Series targetSeries = new XYChart.Series();
        targetSeries.setName("T");
        for(int i = 0; i < cycles; i++) {
        	positionSeries.getData().add(new XYChart.Data(i * interval, sim.positions[i]));
        	targetSeries.getData().add(new XYChart.Data(i * interval, sim.targets[i]));
        }
        
        sc.getData().addAll(targetSeries, positionSeries);
        Scene scene = new Scene(sc, 1000, 500);
        scene.getStylesheets().add("chart.css");
        stage.setScene(scene);
        stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}