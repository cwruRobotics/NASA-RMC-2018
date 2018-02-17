////////////////////////////////////////////////////////////////////////////////
//
//	PREPROCESSOR INCLUDES
//
////////////////////////////////////////////////////////////////////////////////
//#include <ODriveArduino.h>
#include <math.h>


#include "values_and_types.h"
#include "motor_and_sensor_setup.h"
#include "global_vars.h"
#include "hciRead.h"
#include "hardware_io.h"
#include "hciAnswer.h"


////////////////////////////////////////////////////////////////////////////////
//
//  ARDUINO REQUIRED FUNCTIONS
//
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////  SETUP
////////////////////////////////////////////////////////////////////////////////
void setup(){
	setup_sensors();
	setup_motors();
	SerialUSB.begin(9600);
  Serial.begin(9600);
	stopped = false;
}

////////////////////////////////////////////////////////////////////////////////
////  MAIN LOOP
////////////////////////////////////////////////////////////////////////////////
void loop(){
	byte cmd[DEFAULT_BUF_LEN];				// to store message from client
	byte rpy[DEFAULT_BUF_LEN]; 				// buffer for the response
	bool success = false;
	FAULT_T fault_code = NO_FAULT;
	if(SerialUSB.available()){
		fault_code = hciRead(cmd);	// verify the command

		Serial.print("CMD STATUS:\t");
		Serial.println(fault_code);

		if(fault_code == NO_FAULT){
			success = true;
		}else{ // there was an issue with the command
			success = false;
			//log_fault(fault_code);			// add to log or whatever
		}
	}
	
	maintain_motors(cmd, success);			// keep robot in a stable state
											// we may not need the cmd argument
	

	if(success){
		fault_code = hciAnswer(cmd, rpy);	// reply to the client

		Serial.print("RPY STATUS:\t");
		Serial.println(fault_code);
	}
}









////////////////////////////////////////////////////////////////////////////////
//
//  FUNCTIONS
//
////////////////////////////////////////////////////////////////////////////////

FAULT_T log_fault(FAULT_T fault, byte* rpy){
	//form a reply with relevant data regarding the fault

	if(faultIndex < 255){
		faults[faultIndex++] = fault;
		return NO_FAULT;
	}else{
		faults[faultIndex]   = fault;
		return FAULT_LOG_FULL;
	}
}

void clear_fault_log(){
//	FAULT_T tmp[256] = {}; 	// allocate an empty buffer
//	faults 			 = tmp;
}

// Return the most recent fault
FAULT_T popLastFault(){
	FAULT_T retVal = faults[faultIndex]; 	// stash current fault value to return
	faults[faultIndex] = 0;					// 0 has no meaning

	if(faultIndex > 0){
		faultIndex--;			
	}
	return retVal;

}

////////////////////////////////////////////////////////////////////////////////
// analagous to "execute(cmd)"
// not sure this is necessary
////////////////////////////////////////////////////////////////////////////////
FAULT_T update_motor_infos(byte* cmd){
	return NO_FAULT;
}

////////////////////////////////////////////////////////////////////////////////
// update sensor data requested by client
// not sure this is necessary
////////////////////////////////////////////////////////////////////////////////
FAULT_T update_sensor_infos(byte* cmd){
	return NO_FAULT;
}

