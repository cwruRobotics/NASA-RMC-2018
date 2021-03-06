package com.cwrubotix.glennifer.robot_state;

import java.time.Instant;
import java.util.EnumMap;

/**
 * A ExcavationState object encapsulates the current state of the robot's
 * excavation subsystem. It has update methods to give it sensor data, and
 * getter methods to query the state. Its update methods can raise fault
 * exceptions for all kinds of reasons. These faults can be responded to using
 * the adjustment method.
 * 
 * This class does not deal with messages or wire formats. It works purely at
 * the logical level.
 */
public class ExcavationState {

    public enum Side {
        LEFT,  //port
        RIGHT  //starboard
    }

    /* Data members */
	private float conveyorRpm;
	private float translationDisplacement;
	private float armPos;
    private float conveyorCurrent; 
    private EnumMap <Side, Boolean> armSideExtended;
    private Boolean translationRetracted;
    private EnumMap <Side, Boolean> translationSideExtended;

    // TODO: Store the time most recently updated, either for the whole system
    // or for each sensor. If you want to handle out of order updates, you'll
    // need to do it for each sensor I think.
    
    /* Constructor */
    
    public ExcavationState() {
        /* Implementation note: In this constructor, all data members are
         * initialized to 0 because this class does not currently consider the
         * case where it has never received input from a particular sensor. In
         * order to handle that case, initialization would need to be done
         * differently.
         */
        
        // TODO: handle no input from sensor
        conveyorRpm = 0;
        translationDisplacement = 0;
		armPos = 0;
        conveyorCurrent = 0;

        armSideExtended = new EnumMap<>(Side.class);
        armSideExtended.put(Side.LEFT, false);
        armSideExtended.put(Side.RIGHT, false);

        translationRetracted = false;

        translationSideExtended = new EnumMap<>(Side.class);
        translationSideExtended.put(Side.LEFT, false);
        translationSideExtended.put(Side.RIGHT, false);

    }
    
    /* Update methods */
    
    public void updateConveyorRpm (float rpm, Instant time) throws RobotFaultException {
        // TODO: use timestamp to validate data
        // TODO: detect impossibly sudden changes
        // TODO: consider updating stored conveyor speed here
        conveyorRpm = rpm;
    }
    
    public void updateArmPos (float pos, Instant time) throws RobotFaultException {
        // TODO: use timestamp to validate data
        // TODO: detect impossibly sudden changes
        // TODO: consider updating stored configuration
        armPos = pos;
    }
	
	public void updateTranslationDisplacement(float displacement, Instant time) throws RobotFaultException {
        // TODO: use timestamp to validate data
        // TODO: detect impossibly sudden changes
        // TODO: consider updating stored configuration
        translationDisplacement = displacement;
    }

    public void updateConveyorCurrent(float current, Instant time) throws RobotFaultException{
        conveyorCurrent = current;
    }
    
    public void updateArmLimitExtended (Side side, boolean pressed, Instant time) throws RobotFaultException {
        // TODO: use limit switches
        armSideExtended.put(side, pressed);
    }
	
	public void updateTranslationLimitExtended(Side side, boolean pressed, Instant time) throws RobotFaultException {
        translationSideExtended.put(side, pressed);
    }
    
    public void updateTranslationLimitRetracted(boolean pressed, Instant time) throws RobotFaultException {
        translationRetracted = pressed;
    }
    
    /* State getter methods */
    
    public boolean isClearOfStoredHopper() {
        // TODO: Formula for excavation being clear of stored hopper
        return false;
    }
    public boolean isClearOfDumpingHopper() {
        // TODO: Formula for excavation being clear of dumping hopper
        return false;
    }
    public boolean isInGround() {
        // TODO: Formula for excavation being clear of stored hopper
        return false;
    }
    
    public float getTranslationDisplacement() {
		return translationDisplacement;
	}
        
    public float getConveyorRpm() {
        return conveyorRpm;
    }
    
    public float getArmPos() {
        return armPos;
    }

    public float getConveyorCurrent(){
        return conveyorCurrent;
    }

    public boolean getArmExtended(Side side) { 
        return armSideExtended.get(side);
    }

    public boolean getTranslationExtended(Side side) { 
        return translationSideExtended.get(side); 
    }

    public boolean getArmExtended() { 
        return armSideExtended.get(Side.LEFT) || armSideExtended.get(Side.RIGHT); 
    }

    public boolean getTranslationRetracted() { 
        return translationRetracted;
    }

    public boolean getTranslationExtended() { 
        return translationSideExtended.get(Side.LEFT) || translationSideExtended.get(Side.RIGHT); 
    }
}	