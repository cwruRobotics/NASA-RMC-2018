################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CC_SRCS += \
../Edge.cc \
../FloatImage.cc \
../GLine2D.cc \
../GLineSegment2D.cc \
../Gaussian.cc \
../GrayModel.cc \
../Homography33.cc \
../MathUtil.cc \
../Quad.cc \
../Segment.cc \
../TagDetection.cc \
../TagDetector.cc \
../TagFamily.cc \
../UnionFindSimple.cc \
../messages.pb.cc 

CPP_SRCS += \
../ConsumerThread.cpp \
../Serial.cpp \
../TagLocalization.cpp \
../imu.cpp 

CC_DEPS += \
./Edge.d \
./FloatImage.d \
./GLine2D.d \
./GLineSegment2D.d \
./Gaussian.d \
./GrayModel.d \
./Homography33.d \
./MathUtil.d \
./Quad.d \
./Segment.d \
./TagDetection.d \
./TagDetector.d \
./TagFamily.d \
./UnionFindSimple.d \
./messages.pb.d 

OBJS += \
./ConsumerThread.o \
./Edge.o \
./FloatImage.o \
./GLine2D.o \
./GLineSegment2D.o \
./Gaussian.o \
./GrayModel.o \
./Homography33.o \
./MathUtil.o \
./Quad.o \
./Segment.o \
./Serial.o \
./TagDetection.o \
./TagDetector.o \
./TagFamily.o \
./TagLocalization.o \
./UnionFindSimple.o \
./imu.o \
./messages.pb.o 

CPP_DEPS += \
./ConsumerThread.d \
./Serial.d \
./TagLocalization.d \
./imu.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -I/usr/local/lib -I/usr/include/opencv -I/usr/include/eigen3 -O2 -g -Wall -c -fmessage-length=0 -std=c++11 -pthread -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

%.o: ../%.cc
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -I/usr/local/lib -I/usr/include/opencv -I/usr/include/eigen3 -O2 -g -Wall -c -fmessage-length=0 -std=c++11 -pthread -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


