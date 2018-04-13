#ifndef HEADER_FILE
#define HEADER_FILE

////////////////////////////////////////////////////////////////////////////////
void setup_sensors(){
	// SENSORS
	// For testing
	SensorInfo sensor_0;
	sensor_infos[0].hardware 	= SH_PIN_POT;
	sensor_infos[0].whichPin 	= A0;
	sensor_infos[0].responsiveness = 1;
	sensor_infos[0].scale 		= 1;
	sensor_infos[0] 	= sensor_0;


}
////////////////////////////////////////////////////////////////////////////////
void setup_motors(){
	// Motor related serial stuff
	Serial2.begin(SABERTOOTH_BAUD);
	SabertoothSimplified ST(Serial2); 	//
	roboclawSerial.begin(ROBOCLAW_BAUD);

	board_infos[0].type 	= MC_ROBOCLAW;
	board_infos[0].addr 	= ROBOCLAW_0_ADDR;
	board_infos[0].roboclaw = & roboclawSerial;
	
	board_infos[1].type 	= MC_ROBOCLAW;
	board_infos[1].addr 	= ROBOCLAW_1_ADDR;
	board_infos[1].roboclaw = & roboclawSerial;
	
	board_infos[2].type 	= MC_ROBOCLAW;
	board_infos[2].addr 	= ROBOCLAW_2_ADDR;
	board_infos[2].roboclaw = & roboclawSerial;

	board_infos[3].type 		= MC_BRUSHED;
	board_infos[3].ST 			= & ST;
	board_infos[3].selectPin 	= SABERTOOTH_1_SLCT;

	board_infos[4].type 		= MC_BRUSHED;
	board_infos[4].ST 			= & ST;
	board_infos[4].selectPin 	= SABERTOOTH_0_SLCT;
	
	pinMode(SABERTOOTH_0_SLCT, OUTPUT);
	pinMode(SABERTOOTH_1_SLCT, OUTPUT);
	

	// MOTORS
	motor_infos[0].whichMotor	= 0;
	motor_infos[0].hardware 	= MH_RC_VEL;
	motor_infos[0].board 		= & (board_infos[0]);
	
	motor_infos[1].whichMotor 	= 0; // USED TO BE 1
	motor_infos[1].hardware 	= MH_RC_VEL;
	motor_infos[1].is_reversed 	= true;
	motor_infos[1].board 		= & (board_infos[1]);
	
	motor_infos[2].whichMotor 	= 0;
	motor_infos[2].hardware 	= MH_RC_VEL;
	motor_infos[2].board 		= & (board_infos[2]);

	motor_infos[3].whichMotor 	= 1;
	motor_infos[3].hardware 	= MH_RC_VEL;
	motor_infos[3].board 		= & (board_infos[2]);
	
	// motor_infos[4].whichMotor 	= 0;
	// motor_infos[4].hardware 	= MH_RC_VEL;
	// motor_infos[4].board 		= & (board_infos[1]);

	motor_infos[6].whichMotor = 0;
	motor_infos[6].board = &(board_infos[3]);
	motor_infos[6].hardware = MH_ST_POS;
	
	motor_infos[7].whichMotor = 1;
	motor_infos[7].board = &(board_infos[3]);
	motor_infos[7].hardware = MH_ST_POS;
	
	motor_infos[8].whichMotor = 0;
	motor_infos[8].board = &(board_infos[4]);
	motor_infos[8].hardware = MH_ST_POS;


}

#endif
	// motor_infos[3] 		= drive_3;
	// motor_infos[2] 				= drive_2;
	// MotorInfo drive_3;
	// MotorInfo drive_1;
	// motor_infos[1] 		= drive_1;
	// MotorInfo drive_2;// MotorInfo drive_0;
	// PID(roboclaw_0.addr,drive_0.kd,drive_0.kp,drive_0.ki,drive_0.qpps);
	// motor_infos[0] 		= drive_0;
	// MotorInfo motor_6;
	// motor_infos[6] = motor_6;
	// MotorInfo motor_7;
	// motor_infos[7] = motor_7;
	// MotorInfo motor_8;
	// motor_infos[8] = motor_8;
	

	// MotorInfo motor_0;
	// motor_0.whichMotor = 0;
	// motor_0.board = & odrive_board_0;
	// motor_0.hardware = MH_BL_VEL;
	// motor_infos[0] = motor_0;

	// MotorInfo motor_1;
	// motor_1.whichMotor = 1;
	// motor_1.board = & odrive_board_0;
	// motor_1.hardware = MH_BL_VEL;
	// motor_infos[1] = motor_1;

	// MotorInfo motor_2;
	// motor_2.whichMotor = 0;
	// motor_2.board = & odrive_board_1;
	// motor_2.hardware = MH_BL_VEL;
	// motor_infos[2] = motor_2;

	// MotorInfo motor_3;
	// motor_3.whichMotor = 1;
	// motor_3.board = & odrive_board_1;
	// motor_3.hardware = MH_BL_VEL;
	// motor_infos[3] = motor_3;

	// MotorInfo motor_4;
	// motor_4.whichMotor = 0;
	// motor_4.board = & odrive_board_2;
	// motor_4.hardware = MH_BL_POS;
	// motor_infos[4] = motor_4;

	// MotorInfo motor_5;
	// motor_5.whichMotor = 1;
	// motor_5.board = & odrive_board_2;
	// motor_5.hardware = MH_BL_POS;
	// motor_infos[5] = motor_5;

	// MOTOR CONTROLLERS (BOARDS)
	// ODrive motors were "setup" in values_and_types.h
	// MCInfo odrive_board_0;
	// odrive_board_0.odrive = & odrive0;
	// board_infos[0] = odrive_board_0;
	// MCInfo odrive_board_1;
	// odrive_board_1.odrive = & odrive1;
	// board_infos[1] = odrive_board_1;
	// MCInfo odrive_board_2;
	// odrive_board_2.odrive = & odrive2;
	// board_infos[1] = odrive_board_2;
	// MCInfo board_0;
	// board_infos[4] 		= board_0;
	// MCInfo board_1;
	// board_infos[3] 		= board_1;
	//MCInfo roboclaw_0;//board_infos[0] 		= roboclaw_0;
	//MCInfo roboclaw_1;//board_infos[1] 		= roboclaw_1;
	//MCInfo roboclaw_2;
	//board_infos[2]  	= roboclaw_2;