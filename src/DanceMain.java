import processing.core.*;
import oscP5.*;
import netP5.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DanceMain extends PApplet {
	OscP5 object;
	NetAddress myRemoteLocation;
	
	//two markov generators -> thicness and transparency
	MarkovGenerator thickness; //train on thicknesses of line using markov chain
	MarkovGenerator transparency; //train on transparency of line using markov chain
	
	//arrayLists storing data
	
	ArrayList<Float> x = new ArrayList<Float>(); //stores the current frame's X value
	ArrayList<Float> y = new ArrayList<Float>(); //stores the current frame's Y value
	ArrayList<Float> z = new ArrayList<Float>(); //stores the current frame's Z value
	
	ArrayList<Float> accelerationValues = new ArrayList<Float>();
	//ArrayList<Float> genAccVals = new ArrayList<Float>();
	
	//test stuff
	Float[] testVals = new Float[] { (float)	0.88, (float) 0.89, (float) 0.91, (float) 0.92, (float) 0.94, (float) 0.96, (float) 0.96, (float) 0.98, (float) 0.98, (float) 0.98, (float) 0.98, (float) 0.98, (float) 0.98, (float) 0.98, (float) 0.99, (float) 0.99, (float) 1.0, (float) 0.99, (float) 0.98, (float) 0.97, (float) 0.96, (float) 0.95, (float) 0.94, (float) 0.94, (float) 0.93, (float) 0.93, (float) 0.93, (float) 0.92, (float) 0.91, (float) 0.90, (float) 0.90, (float) 0.89, (float) 0.89, (float) 0.89, (float) 0.90, (float) 0.90, (float) 0.91, (float) 0.92,(float) 0.93,(float) 0.93, (float) 0.93, (float) 0.93, (float) 0.93, (float) 0.93, (float) 0.93, (float) 0.93, (float) 0.93, (float) 0.93, (float) 0.94, (float) 0.94, (float) 0.95, (float) 0.95, (float) 0.95, (float) 0.95 };
	ArrayList<Float> test = new ArrayList<Float>();
	
	//stores previous and current accelerometer measurements
	float prevX = 0;
	float prevY = 0;
	float prevZ = 0;
	float currX = 0;
	float currY = 0;
	float currZ = 0;

	//counter variables for number of frames
	int counter = 0;
	int frameCounter = 0;
	
	//factors for drawing on screen
	int multFactor = 2000;
	int subFactor = 600;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("DanceMain");
	}

	public void settings() { //sets up project
		size(1200, 800); 
		
	//sets up OSC message
		object = new OscP5(this, 6448); // creates new OscP5 object, listening port 6448
		myRemoteLocation = new NetAddress("127.0.0.1", 12000);
		
	//sets up markov chains for line thicknesses and transparencies
		thickness = new MarkovGenerator<Float>();
		transparency = new MarkovGenerator<Float>();
	}

	public void setup() { //sets up processing background window color
		background(255);
		test.addAll(Arrays.asList(testVals));
	}
	
	public void handleAccVal() { //calculates the acceleration
		float accVal;
		accVal = (float) Math.sqrt((currX*currX) + (currY*currY) + (currZ*currZ));
		accVal = (float) (Math.round(accVal * 100.0) / 100.0); //round to two decimal places
		if (!accelerationValues.contains(accVal)) { //add to the arrayList
			accelerationValues.add(accVal); 
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
	
	public void strokeWeightTraining() {
		handleMarkov(thickness, accelerationValues);
		if (thickness.getGeneratedMarkov().isEmpty()) {
			strokeWeight(1);
		} else {
			float value = (float) thickness.getGeneratedMarkov().get(  thickness.getGeneratedMarkov().size() - 1   );
			float weight = value * value + 5;
			strokeWeight(weight);
			//System.out.println("weight = " + weight);
		}
	}

	public void colorTraining() {
		handleMarkov(transparency, z);
		float alpha;
		float value;
		if (transparency.getGeneratedMarkov().isEmpty()) {
			alpha = 255;
		} else {
			value = (float) transparency.getGeneratedMarkov().get(  transparency.getGeneratedMarkov().size() - 1  ) * 10;
			alpha = value * value * 5;
		}
		stroke(currX * 100, currY * 100, currZ * 100, alpha);
	}
	
	public void draw() { //looping function that draws to the screen
		//drawing stuff on-screen
		//stroke(255);
		strokeWeight(2);
		//deal with actual drawing to the screen
		line((prevX * multFactor) - subFactor, (prevY * multFactor) - subFactor, (currX * multFactor) - subFactor, (currY * multFactor) - subFactor);
		
		handleAccVal(); //calculate acceleration for Markov chain purposes
		
		//Markov stuff -> real-time training and generation
		strokeWeightTraining();
		colorTraining();
		
		
		
		/* TEST DATA
		handleMarkov(thickness, test);
		noLoop();
		 */
		
		
		
	}
	


	public void oscEvent(OscMessage theOscMessage) { // incoming osc message are forwarded to the oscEvent method.
		//println("Received an osc message.");
		// print(" addrpattern: "+theOscMessage.addrPattern());
		// println(" typetag: "+theOscMessage.typetag());
		counter++;
		incomingMessage(theOscMessage);
	}

	public void incomingMessage(OscMessage message) {
		for (int i = 0; i < message.arguments().length; i++) {
			if (i % 3 == 0) {
				if (counter % 2 == 0) {
					prevX = (float) message.get(i).floatValue();
					//println("prevX: " + prevX);
				} else {
					currX = (float) message.get(i).floatValue();
					addToList(x, currX);
					//println("currX: " + currX);
				}
			} else if (i % 3 == 1) {
				if (counter % 2 == 0) {
					prevY = (float) message.get(i).floatValue();
					//println("prevY: " + prevY);
				} else {
					currY = (float) message.get(i).floatValue();
					addToList(y, currY);
					//println("currY: " + currY);
				}
			} else {
				if (counter % 2 == 0) {
					prevZ = (float) message.get(i).floatValue();
					//println("prevZ: " + prevZ);
				} else {
					currZ = (float) message.get(i).floatValue();
					currZ = (float) (Math.round(currZ * 100.0) / 100.0); //round to two decimal places
					addToList(z, currZ);
					//println("currZ: " + currZ);
				}
			}
		}
	}
	
	public void addToList(ArrayList<Float> list, float value) {
		if (list.contains(value)) {//do nothing
		} else {
			list.add(value);
		}
	}
}
