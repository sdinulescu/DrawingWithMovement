/* Drawing with Movement
 * Created by Stejara Dinulescu
 * In this project, I attach wiimotes to points on a ballroom dancing couple.
 * This project then takes in accelerometer data from wii remotes using Osculator and the oscP5 library and translates those values to a drawing on screen.
 * These values are stored in array lists, where 4 markovGenerator object trains on the values and generates their own values, stored in array lists.
 * A line is then drawn, with line thickness, color, and position (x and y) generated by the markov chain.
 */

//import statements
import processing.core.*;
import oscP5.*;
import netP5.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DanceMain extends PApplet {
	OscP5 object; //creates an OscP5 object
	Wiimote cont1 = new Wiimote("one");
	Wiimote cont2 = new Wiimote("two");
	Wiimote cont3 = new Wiimote("three");
//	Wiimote cont4 = new Wiimote("four");
//	Wiimote cont5 = new Wiimote("five");
//	Wiimote cont6 = new Wiimote("six");
	NetAddress myRemoteLocation; //specifies address for osc messages
	
	//test data!
	Float[] testX = new Float[] {(float) 0.52, (float) 0.51, (float) 0.5, (float) (float) 0.49, (float) 0.47, (float) 0.46, (float) 0.45, 
			(float) 0.48, (float) 0.44, (float) 0.43, (float) 0.42, (float) 0.53, (float) 0.54, (float) 0.55, (float) 0.56, (float) 0.36, 
			(float) 0.31, (float) 0.26, (float) 0.23, (float) 0.24, (float) 0.28, (float) 0.32, (float) 0.33, (float) 0.34, (float) 0.35, 
			(float) 0.38, (float) 0.39, (float) 0.40, (float) 0.41, (float) 0.30, (float) 0.37, (float) 0.57, (float) 0.58, (float) 0.60, 
			(float) 0.59, (float) 0.61, (float) 0.63, (float) 0.62, (float) 0.68, (float) 0.71, (float) 0.70, (float) 0.67, (float) 0.65, 
			(float) 0.64, (float) 0.29, (float) 0.27, (float) 0.21, (float) 0.2, (float) 0.22, (float) 0.25, (float) 0.18, (float) 0.66, 
			(float) 0.72, (float) 0.69, (float) 0.73, (float) 0.76, (float) 0.77, (float) 0.41, (float) 0.42, (float) 0.4, (float) 0.39, (float) 0.38, (float) 0.43, (float) 0.44, (float) 0.37, 
			(float) 0.36, (float) 0.45, (float) 0.35, (float) 0.34, (float) 0.33, (float) 0.32, (float) 0.47, (float) 0.48, (float) 0.49,
			(float) 0.5, (float) 0.51, (float) 0.52, (float) 0.53, (float) 0.46, (float) 0.31, (float) 0.29, (float) 0.28, (float) 0.26, 
			(float) 0.25, (float) 0.27, (float) 0.3, (float) 0.54, (float) 0.57, (float) 0.6, (float) 0.62, (float) 0.63, (float) 0.61, 
			(float) 0.59, (float) 0.58, (float) 0.56, (float) 0.55, (float) 0.64, (float) 0.66, (float) 0.67, (float) 0.68, (float) 0.65, 
			(float) 0.69, (float) 0.71, (float) 0.72, (float) 0.73, (float) 0.74, (float) 0.76, (float) 0.77, (float) 0.79, (float) 0.81,
			(float) 0.83, (float) 0.85, (float) 0.87, (float) 0.88, (float) 0.89, (float) 0.9, (float) 0.86,(float)  0.78, (float) 0.75, 
			(float) 0.7, (float) 0.24};
	Float[] testY = new Float[] { (float) 0.53, (float) 0.52, (float) 0.51, (float) 0.54, (float) 0.55, (float) 0.5, (float) 0.49, (float) 0.56,
			(float) 0.58, (float) 0.6, (float) 0.63, (float) 0.65, (float) 0.66, (float) 0.57, (float) 0.59, (float) 0.61, (float) 0.62, 
			(float) 0.67, (float) 0.68, (float) 0.64, (float) 0.69, (float) 0.72, (float) 0.75, (float) 0.78, (float) 0.79, (float) 0.8, 
			(float) 0.82, (float) 0.83, (float) 0.84, (float) 0.85, (float) 0.86, (float) 0.81, (float) 0.77, (float) 0.74,(float)  0.76,
			(float) 0.73, (float) 0.71, (float) 0.47, (float) 0.45, (float) 0.88, (float) 0.89, (float) 0.9, (float) 0.91, (float) 0.92, 
			(float) 0.87, (float) 0.7, (float) 0.48, (float) 0.46, (float) 0.44, (float) 0.43, (float) 0.42, (float) 0.41, (float) 0.4,
			(float) 0.38, (float) 0.37, (float) 0.39, (float) 0.36, (float) 0.34, (float) 0.33, (float) 0.52, (float) 0.51, (float) 0.5, (float) (float) 0.49, (float) 0.47, (float) 0.46, (float) 0.45, 
			(float) 0.48, (float) 0.44, (float) 0.43, (float) 0.42, (float) 0.53, (float) 0.54, (float) 0.55, (float) 0.56, (float) 0.36, 
			(float) 0.31, (float) 0.26, (float) 0.23, (float) 0.24, (float) 0.28, (float) 0.32, (float) 0.33, (float) 0.34, (float) 0.35, 
			(float) 0.38, (float) 0.39, (float) 0.40, (float) 0.41, (float) 0.30, (float) 0.37, (float) 0.57, (float) 0.58, (float) 0.60, 
			(float) 0.59, (float) 0.61, (float) 0.63, (float) 0.62, (float) 0.68, (float) 0.71, (float) 0.70, (float) 0.67, (float) 0.65, 
			(float) 0.64, (float) 0.29, (float) 0.27, (float) 0.21, (float) 0.2, (float) 0.22, (float) 0.25, (float) 0.18, (float) 0.66, 
			(float) 0.72, (float) 0.69, (float) 0.73, (float) 0.76, (float) 0.77, (float) 0.53, (float) 0.52, (float) 0.51, (float) 0.54, (float) 0.24 };
	Float[] testZ = new Float[] { (float) 0.41, (float) 0.42, (float) 0.4, (float) 0.39, (float) 0.38, (float) 0.43, (float) 0.44, (float) 0.37, 
			(float) 0.36, (float) 0.45, (float) 0.35, (float) 0.34, (float) 0.33, (float) 0.32, (float) 0.47, (float) 0.48, (float) 0.49,
			(float) 0.5, (float) 0.51, (float) 0.52, (float) 0.53, (float) 0.46, (float) 0.31, (float) 0.29, (float) 0.28, (float) 0.26, 
			(float) 0.25, (float) 0.27, (float) 0.3, (float) 0.54, (float) 0.57, (float) 0.6, (float) 0.62, (float) 0.63, (float) 0.61, 
			(float) 0.59, (float) 0.58, (float) 0.56, (float) 0.55, (float) 0.64, (float) 0.66, (float) 0.67, (float) 0.68, (float) 0.65, 
			(float) 0.69, (float) 0.71, (float) 0.72, (float) 0.73, (float) 0.74, (float) 0.76, (float) 0.77, (float) 0.79, (float) 0.81,
			(float) 0.83, (float) 0.85, (float) 0.87, (float) 0.88, (float) 0.89, (float) 0.9, (float) 0.86,(float)  0.78, (float) 0.75, 
			(float) 0.7, (float) 0.24, (float) 0.53, (float) 0.52, (float) 0.51, (float) 0.54, (float) 0.55, (float) 0.5, (float) 0.49, (float) 0.56,
			(float) 0.58, (float) 0.6, (float) 0.63, (float) 0.65, (float) 0.66, (float) 0.57, (float) 0.59, (float) 0.61, (float) 0.62, 
			(float) 0.67, (float) 0.68, (float) 0.64, (float) 0.69, (float) 0.72, (float) 0.75, (float) 0.78, (float) 0.79, (float) 0.8, 
			(float) 0.82, (float) 0.83, (float) 0.84, (float) 0.85, (float) 0.86, (float) 0.81, (float) 0.77, (float) 0.74,(float)  0.76,
			(float) 0.73, (float) 0.71, (float) 0.47, (float) 0.45, (float) 0.88, (float) 0.89, (float) 0.9, (float) 0.91, (float) 0.92, 
			(float) 0.87, (float) 0.7, (float) 0.48, (float) 0.46, (float) 0.44, (float) 0.43, (float) 0.42, (float) 0.41, (float) 0.4,
			(float) 0.38, (float) 0.37, (float) 0.39, (float) 0.36, (float) 0.34, (float) 0.33};
	
	//factors for drawing on screen
	int testMultFactor = 500;
	int multFactor = 500; 
	int testSubFactor = -100;
	int subFactor = -300;
	
	int frameCounter = 0; //counts frames of draw

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("DanceMain");
	}

	public void settings() { //sets up project
		fullScreen();
	    //sets up OSC message
		object = new OscP5(this, 6448); // creates new OscP5 object, listening port 6448
		object.addListener(cont1);
		object.addListener(cont2);
		object.addListener(cont3);
//		object.addListener(cont4);
//		object.addListener(cont5);
//		object.addListener(cont6);
		myRemoteLocation = new NetAddress("127.0.0.1", 12000);
	}

	public void setup() { //sets up processing background window color
		background(0);
		//initializeTestData(cont1); //unit test data initialization
		//initializeTestData(cont2); //unit test data initialization
	
		//instantiates MarkovGenerator objects for each controller
		cont1.setupMarkov();
		cont2.setupMarkov();
		cont3.setupMarkov();
//		cont4.setupMarkov();
//		cont5.setupMarkov();
//		cont6.setupMarkov();
	}
	
	public void initializeTestData(Wiimote controller) { //initializes test data into arrayLists
		if (controller == cont1) {
			controller.setX(Arrays.asList(testX));
			controller.setY(Arrays.asList(testY));
			controller.setZ(Arrays.asList(testZ));
		} else {
			controller.setX(Arrays.asList(testY));
			controller.setY(Arrays.asList(testZ));
			controller.setZ(Arrays.asList(testX));
		}
	}
	
	public void printTest(Wiimote controller) { //unit test to make sure all data is printing
		System.out.println("_____________ CONTROLLER: " + controller.getID() + " _______________");
		System.out.println("x accelerometer data: " + controller.getX());
		System.out.println("y accelerometer data: " + controller.getY());
		System.out.println("z accelerometer data: " + controller.getZ());
		System.out.println("acceleration calculations array: " + controller.getAccVals());
		System.out.println("Markov tests: -----------------------------------------------");
		System.out.println("Thickness training (stroke weight)");
		System.out.println("Generated array: " + controller.thickness.getGeneratedMarkov());
		controller.thickness.testMarkov();
		System.out.println("Transparency training (alpha)");
		System.out.println("Generated array: " + controller.transparency.getGeneratedMarkov());
		controller.transparency.testMarkov();
		System.out.println("X position training");
		System.out.println("Generated array: " + controller.positionX.getGeneratedMarkov());
		controller.positionX.testMarkov();
		System.out.println("Y position training");
		System.out.println("Generated array: " + controller.positionY.getGeneratedMarkov());
		controller.positionY.testMarkov();
	}

	public void handleAccVal(Wiimote controller) { //calculates the acceleration
		float accVal = 0;
		//comment this OUT for test data
		accVal = (float) Math.sqrt((controller.getCurrX()*controller.getCurrX()) + (controller.getCurrY()*controller.getCurrY()) + (controller.getCurrZ()*controller.getCurrZ())); 
		
		//uncomment this for test data
		//accVal = (float) Math.sqrt((controller.getX().get(frameCounter) * controller.getX().get(frameCounter) + controller.getY().get(frameCounter) * controller.getY().get(frameCounter) + controller.getZ().get(frameCounter) * controller.getZ().get(frameCounter)));
		
		accVal = (float) (Math.round(accVal * 100.0) / 100.0); //round to two decimal places
		if (!controller.getAccVals().contains(accVal)) { //add to the arrayList
			controller.setAccVals(accVal); 
		}
		
		//System.out.println("accelerationValues: " + accelerationValues);
	}
	
	public void handleMarkov(MarkovGenerator<Float> markov, ArrayList<Float> trainingList) { //handles the Markov chain for training and generation
		markov.train(trainingList);
		float seed;
		if (markov.getGeneratedMarkov().isEmpty()) {
			seed = markov.findSeed();
		} else {
			seed = markov.getGeneratedMarkov().get(markov.getGeneratedMarkov().size() - 1);
		}
		markov.generate(seed);
		//System.out.println("Generated: " + markov.getGeneratedMarkov());
	}
	
	public void strokeWeightTraining(Wiimote controller) { //train thickness markov
		handleMarkov(controller.thickness, controller.getAccVals());
		float weight = 0;
		
		if (controller.thickness.getGeneratedMarkov().isEmpty()) {
			strokeWeight(1);
		} else {
			float value = (float) controller.thickness.getGeneratedMarkov().get(  controller.thickness.getGeneratedMarkov().size() - 1   );
			weight = value * value * 10;
			//System.out.println("weight = " + weight);
		}
		
		//caps on stroke weight
		if ( weight > 20 ) {
			weight = 20;
		} else if ( weight < 1 ) {
			weight = 1;
		}
		
		strokeWeight(weight);
	}

	public void colorTraining(Wiimote controller) { //train transparency markov
		handleMarkov(controller.transparency, controller.getZ());
		float alpha;
		float value;
		
		if (controller.transparency.getGeneratedMarkov().isEmpty()) {
			alpha = 255;
		} else {
			value = (float) controller.transparency.getGeneratedMarkov().get(  controller.transparency.getGeneratedMarkov().size() - 1  ) * 10;
			alpha = value * value * 5;
		}
		
		//caps on alpha value
		if ( alpha < 100 ) { //bottom alpha so that it doesn't get too transparent
			alpha = 100;
		} else if ( alpha > 255 ) { //top alpha cutoff
			alpha = 255;
		}
		if (controller == cont1) {
			stroke(255, 0, 0, alpha);
		} else if (controller == cont2){
			stroke(random(100,controller.getCurrX()), 0, random(100, controller.getCurrY()), alpha);
		} else if (controller == cont3) {
			stroke(0, 0, 255, alpha);
		}
	}
	
	public void positionTraining(Wiimote controller) { //train position markovs
		handleMarkov(controller.positionX, controller.getX());
		handleMarkov(controller.positionY, controller.getY());
	}
	
	public void boundaryCheck(float value) {
		if (value < 0) {
			value = 0;
		} else if (value > width || value > height) {
			value = width;
		}
	}
	
	public void handleDraw(Wiimote controller) { //handles drawing for each controller
		stroke(255);
		strokeWeight(1);
		
		//if (  frameCounter > 1 && (frameCounter < controller.getX().size() - 2 || frameCounter < controller.getY().size() - 2 ) ) { //array bounds
			line((controller.getPrevX() * multFactor) - subFactor + 100, (controller.getPrevY() * multFactor) - subFactor - 100, (controller.getCurrX() * multFactor) - subFactor + 100, (controller.getCurrY() * multFactor) - subFactor - 100);
			//line((float) controller.getX().get(frameCounter-1) * testMultFactor - testSubFactor, controller.getY().get(frameCounter - 1) * testMultFactor - testSubFactor, controller.getX().get(frameCounter) * testMultFactor - testSubFactor, controller.getY().get(frameCounter) * testMultFactor - testSubFactor);
			handleAccVal(controller); //calculate acceleration for Markov chain purposes
			
			//Markov stuff
			strokeWeightTraining(controller); //Markov chain training on acceleration array
			colorTraining(controller); //Markov chain training on z array (alpha value)
			positionTraining(controller); //Markov chain training on x and y arrays (position of Markov line drawing)
			
			if (controller.positionX.getGeneratedMarkov().size() > 2 && controller.positionY.getGeneratedMarkov().size() > 2) {
				float x1 = (float) controller.positionX.getGeneratedMarkov().get(  controller.positionX.getGeneratedMarkov().size() - 2  ) * testMultFactor - testSubFactor + 400;
				float y1 = (float) controller.positionY.getGeneratedMarkov().get(  controller.positionY.getGeneratedMarkov().size() - 2  ) * testMultFactor - testSubFactor + 200;
				float x2 = (float) controller.positionX.getGeneratedMarkov().get(  controller.positionX.getGeneratedMarkov().size() - 1  ) * testMultFactor - testSubFactor + 400;
				float y2 = (float) controller.positionY.getGeneratedMarkov().get(  controller.positionY.getGeneratedMarkov().size() - 1  ) * testMultFactor - testSubFactor + 200;
				line(x1, y1, x2, y2);
			}
		
		//} else if ( frameCounter >= controller.getX().size() || frameCounter >= controller.getY().size() ) {  printTest(controller);  noLoop();  }
	}
	
	public void clearVals(Wiimote controller) {
		if (frameCounter % 500 == 0) {
			controller.getX().clear();
			controller.getY().clear();
			controller.getZ().clear();
			controller.getAccVals().clear();
			controller.positionX.clearVals();
			controller.positionY.clearVals();
			controller.thickness.clearVals();
			controller.transparency.clearVals();
		}
	}
	
	public void clearBackground() {
		if (frameCounter % 16 == 0) { //fade
			noStroke();
		    fill( 0 , 50);
		    rect(0, 0, width, height);
		}
	}
	
	public void draw() { //looping function that draws to the screen
		frameCounter++;
		handleDraw(cont1);
		handleDraw(cont2);
		handleDraw(cont3);
//		handleDraw(cont4);
//		handleDraw(cont5);
//		handleDraw(cont6);
		
		clearVals(cont1);
		clearVals(cont2);
		clearVals(cont3);
//		clearVals(cont4);
//		clearVals(cont5);
//		clearVals(cont6);
		
		clearBackground();
	}
}
