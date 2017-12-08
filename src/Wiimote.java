/* Wiimote.java
 * Created by Stejara Dinulescu
 * Class containing all properties to be used for organization of data coming from each controller. 
 */


import java.util.ArrayList;
import java.util.List;

import oscP5.*;

public class Wiimote implements OscEventListener {
	private String ID; //controller name
	private int counter = 0; //counter variable
	//stores previous and current accelerometer measurements
	private float prevX = 0;
	private float rPrevX = 0;
	private float prevY = 0;
	private float rPrevY = 0;
	private float prevZ = 0;
	private float rPrevZ = 0;
	private float currX = 0;
	private float rCurrX = 0;
	private float currY = 0;
	private float rCurrY = 0;
	private float currZ = 0;
	private float rCurrZ = 0;
	//arrayLists storing data	
	private ArrayList<Float> x = new ArrayList<Float>(); //stores the current frame's X value
	private ArrayList<Float> y = new ArrayList<Float>(); //stores the current frame's Y value
	private ArrayList<Float> z = new ArrayList<Float>(); //stores the current frame's Z value
	private ArrayList<Float> accelerationValues = new ArrayList<Float>(); //stores calculated acceleration
	//create MarkovGenerator objects
	public MarkovGenerator<Float> thickness; //train on thicknesses of line using markov chain and acceleration data
	public MarkovGenerator<Float> transparency; //train on transparency of line using markov chain and z accelerometer data
	public MarkovGenerator<Float> positionX; //train on x positions of line using markov chain and x and y accelerometer data
	public MarkovGenerator<Float> positionY;//train on y positions of line using markov chain and x and y accelerometer data

	//getters and setters for all properties
	Wiimote (String ID) {
	   this.ID = ID;
	}
	
	public String getID() {
		return ID;
	}
	
	public float getPrevX() {
		return prevX;
	}
	
	public float getPrevY() {
		return prevY;
	}
	
	public float getCurrX() {
		return currX;
	}
	
	public float getCurrY() {
		return currY;
	}
	
	public float getCurrZ() {
		return currZ;
	}
	
	public void setX(List<Float> x) {
		this.x.addAll(x);
	}
	
	public ArrayList<Float> getX() {
		return x;
	}
	
	public void setY(List<Float> y) {
		this.y.addAll(y);
	}
	public ArrayList<Float> getY() {
		return y;
	}
	
	public void setZ(List<Float> z) {
		this.z.addAll(z);
	}
	
	public ArrayList<Float> getZ() {
		return z;
	}
	
	public void setAccVals(float value) {
		accelerationValues.add(value);
	}
	
	public ArrayList<Float> getAccVals() {
		return accelerationValues;
	}
	
	public void setupMarkov() { //instantiates all markov objects
		thickness = new MarkovGenerator<Float>();
		transparency = new MarkovGenerator<Float>();
		positionX = new MarkovGenerator<Float>();
		positionY = new MarkovGenerator<Float>();
	}
	
	public void oscStatus(OscStatus theStatus) {
		
	}
	
	public void oscEvent(OscMessage theOscMessage) { // incoming osc message are forwarded to the oscEvent method
		//System.out.println("received an osc message @ listener " + ID);
		//System.out.println(" addrpattern: "+theOscMessage.addrPattern());
		//System.out.println(" typetag: "+theOscMessage.typetag());
		
		counter++;
		incomingMessage(theOscMessage);
	}

	public void incomingMessage(OscMessage message) { //deals with incoming messages
		for (int i = 0; i < message.arguments().length; i++) {
			setAllValues(message, i);
		}
	}
	
	public void setAllValues(OscMessage message, int i) { //organizes data into variables and arrayLists
		if (i % 3 == 0) {
			if (counter % 2 == 0) {
				prevX = (float) message.get(i).floatValue();
				rPrevX = (float) (Math.round(currX * 100.0) / 100.0); //round to two decimal places
				//System.out.println("prevX: " + prevX);
			} else {
				currX = (float) message.get(i).floatValue();
				rCurrX = (float) (Math.round(currX * 100.0) / 100.0); //round to two decimal places
				addToList(x, rCurrX);
				//System.out.println("currX: " + currX);
			}
		} else if (i % 3 == 1) {
			if (counter % 2 == 0) {
				prevY = (float) message.get(i).floatValue();
				rPrevY = (float) (Math.round(currX * 100.0) / 100.0); //round to two decimal places
				//System.out.println("prevY: " + prevY);
			} else {
				currY = (float) message.get(i).floatValue();
				rCurrY = (float) (Math.round(currY * 100.0) / 100.0); //round to two decimal places
				addToList(y, rCurrY);
				//System.out.println("currY: " + currY);
			}
		} else {
			if (counter % 2 == 0) {
				prevZ = (float) message.get(i).floatValue();
				rPrevZ = (float) (Math.round(currX * 100.0) / 100.0); //round to two decimal places
				//System.out.println("prevZ: " + prevZ);
			} else {
				currZ = (float) message.get(i).floatValue();
				rCurrZ = (float) (Math.round(currZ * 100.0) / 100.0); //round to two decimal places
				addToList(z, rCurrZ);
				//System.out.println("currZ: " + currZ);
			}
		}
	}
	
	public void addToList(ArrayList<Float> list, float value) { //adds values to arrayList
		if (list.contains(value)) {//do nothing
		} else {
			list.add(value);
		}
	}
	
}
