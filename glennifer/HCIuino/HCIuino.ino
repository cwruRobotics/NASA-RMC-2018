////////////////////////////////////////////////////////////////////////////////
//
//	PREPROCESSOR INCLUDES
//
////////////////////////////////////////////////////////////////////////////////
#include <ODriveArduino.h>
#include <SabertoothSimplified.h>
#include <RoboClaw.h>
#include <math.h>

RoboClaw roboclawSerial(&Serial1, 10000);
#include "values_and_types.h"
#include "motor_and_sensor_setup.h"
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
	Serial.begin(HCI_BAUD); 	// Begin communication with computer
	setup_sensors();
	setup_motors(); 
	Serial3.begin(9600); 		// For the FTDI-USB converter debugging tool
	
	
	stopped = false;
	Serial3.println("================================================================================");
	Serial3.println("CMD STATUS | CMD TYPE | BODY LEN | RPY STATUS | RPY SIZE");
	Serial3.println("================================================================================");
	delay(50);
}


////////////////////////////////////////////////////////////////////////////////
////  MAIN LOOP
////////////////////////////////////////////////////////////////////////////////
void loop(){
	analogWriteResolution(12);
	byte cmd[DEFAULT_BUF_LEN];				// to store message from client
	byte rpy[DEFAULT_BUF_LEN]; 				// buffer for the response
	bool success = false;

	long time = millis() - lastTime;
	FAULT_T fault_code = NO_FAULT;
	if(Serial.available() > 0){
		
		fault_code = hciRead(cmd);	// verify the command

		if(cmd_type(cmd) > 0){
			Serial3.print("CMD received : ");
			Serial3.println(cmd_type(cmd));
			delay(10);
		}

		if(fault_code == NO_FAULT){
			success = true;
		}else{ // there was an issue with the command
			success = false;
			Serial3.print("READ ERROR:\t");
			Serial3.print(fault_code);
			Serial3.print("\t");
			Serial3.println(cmd_type(cmd));
			//log_fault(fault_code);			// add to log or whatever
		}
	}else{
		if(time >= 5000){
			Serial3.println("Client is still not available.");
			lastTime = millis();
		}
		
	}
	maintain_motors(cmd, success);			// keep robot in a stable state
											// we may not need the cmd argument

	if(success){
		Serial3.print("SUCCESS! CMD received:\t");
		for(int i = 0; i<cmd_body_len(cmd)+RPY_HEADER_SIZE; i++){
			Serial3.print((uint8_t)cmd[i]);
			Serial3.print(" ");
		}
		Serial3.println();
		delay(150);
		fault_code = hciAnswer(cmd, rpy);	// reply to the client
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

