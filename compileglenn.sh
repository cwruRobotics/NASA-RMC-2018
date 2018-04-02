
datestring=$(date +%Y_%m_%d_%H_%M_%S)
cd /home/workspace
mkdir -p "logs/$datestring"

cd /home/workspace/NASA-RMC-2018/

# copy config
cp -r config glennifer/HCI/
cp -r config glennifer/robot_state
cp -r config glennifer/motor_dispatch/src/python
cp -r config glennifer/automodule

# compile the things
cd glennifer/HCI
mvn package
cd ..

cd robot_state
mvn package
cd ..

cd automodule
mvn package
cd ..
