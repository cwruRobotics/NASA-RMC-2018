package com.cwrubotix.glennifer.automodule;

import com.cwrubotix.glennifer.Messages;
import com.cwrubotix.glennifer.Messages.RpmUpdate;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Module for controlling movement
 *
 * @author Imran Hossain
 */
public class AutoTransit extends Module {
	private final Position DUMP_BIN = new Position(0.0F, 0.0F, Math.PI);
	/*Horizontal line representing where digging arena starts.*/
	private final Position DIGGING_AREA = new Position(0.0F, 4.41F, -1.0);
	private final float CLEARANCE_DIST = 0.3F; //Setting this to 30cm for now. Will have to change it after testing locomotion.
	private final float TRAVEL_SPEED = 1.0F; // Sensible speed at which to travel
	private static Position currentPos;
	private PathFinder pathFinder;
	private Path currentPath;
	private boolean launched = false;

	// Messaging stuff
	private String exchangeName;
	private Connection connection;
	private Channel channel;


	/*
	 * TODO LIST
	 * 
	 * 1) Create Wrapper data type for coordinate values that we receive from localization
	 * 2) Subscribe to appropriate sensor values. (location within the arena, locomotion motors)
	 * 3) Come up with path planning algorithm.
	 * 4) Come up with possible errors and handling mechanism.
	 * 5) Set up the Connection Factory
	 * 
	 */

	/////// MESSAGING

	/**
	 * Consumer class for launch command
	 */
	public class TransitLaunchConsumer extends DefaultConsumer {
		public TransitLaunchConsumer(Channel channel) {
			super(channel);
		}

		@Override
		public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
			
		    Messages.LaunchTransit cmd = Messages.LaunchTransit.parseFrom(body);
			// Get current position
			Position currentPos = new Position(
					cmd.getCurXPos(),
					cmd.getCurYPos(),
					cmd.getCurHeading());

			Position destinationPos = new Position(
					cmd.getDestXPos(),
					cmd.getDestYPos(),
					0f);
			AutoTransit.currentPos = currentPos;
			if(!launched){
			    System.out.println("Launch message received.");
			    launched = true;
			}
			pathFinder = new PathFinder(new ModifiedAStar(), currentPos, destinationPos);
			currentPath = pathFinder.getPath();


        }
	}

	public class TransitSoftStopConsumer extends DefaultConsumer {
		public TransitSoftStopConsumer(Channel channel) {
			super(channel);
		}

		@Override
		public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
			Messages.TransitSoftStop cmd = Messages.TransitSoftStop.parseFrom(body);
			// TODO: Implement soft stop handler
		}
	}

	public class TransitHardStopConsumer extends DefaultConsumer {
		public TransitHardStopConsumer(Channel channel) {
			super(channel);
		}

		@Override
		public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		    Messages.TransitHardStop cmd = Messages.TransitHardStop.parseFrom(body);
			// TODO: Implement hard stop handler
		}
	}

	public class TransitNewObstacleConsumer extends DefaultConsumer {
		public TransitNewObstacleConsumer(Channel channel) {
			super(channel);
		}

		@Override
		public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
			// Parse message
			Messages.TransitNewObstacle cmd = Messages.TransitNewObstacle.parseFrom(body);

			// Construct obstacle data
			float obsXPos = cmd.getXPos();
			float obsYPos = cmd.getXPos();
			float obsDiameter = cmd.getDiameter();
			Obstacle newObs = new Obstacle(obsXPos, obsYPos, obsDiameter);

			// Register new obstacle, and reset path if necessary
			try {
				pathFinder.registerObstacle(newObs);
			} catch (PathFinder.DestinationModified destinationModified) {
			    currentPath = pathFinder.getPath();
			}
		}
	}
	
	public class LocalizationPositionConsumer extends DefaultConsumer {
	    public LocalizationPositionConsumer(Channel channel){
		super(channel);
	    }
	    
	    @Override
	    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		//parse message
		Messages.LocalizationPosition pos = Messages.LocalizationPosition.parseFrom(body);
		

		// Updates current position
		currentPos = new Position(pos.getXPosition(), pos.getYPosition(), pos.getBearingAngle());
		System.out.println("current pos:" + currentPos);
	    }
	}

	/////// MODULE LOGIC

	public AutoTransit() {
		this("amq.topic");
	}

	public AutoTransit(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public static Position getCurrentPos(){
		return currentPos;
	}

	private void moveToPos(Position currPos, Position destPos) throws IOException {
		// Compute angle to turn to -- clockwise is positive
        double angleBetween = Position.angleBetween(currPos, destPos);

		turnAngle(angleBetween);
		driveTo(destPos);
	}

	private void turnAngle(double angle) throws IOException {
	    if (angle < 0) return;

        /*
        Determine direction
        Tell robot to turn
        while we're not at correct heading
            sleep 100ms
        stop
         */
        // Build messages
        RpmUpdate rWheelsMsg = RpmUpdate.newBuilder()
                .setRpm(-Math.signum((float) angle) * TRAVEL_SPEED)
                .build();

        RpmUpdate lWheelsMsg = RpmUpdate.newBuilder()
                .setRpm(Math.signum((float) angle) * TRAVEL_SPEED)
                .build();

        // Tell wheels to start moving
        this.channel.basicPublish(exchangeName, "sensor.locomotion.front_right.wheel_rpm", null, rWheelsMsg.toByteArray());
        this.channel.basicPublish(exchangeName, "sensor.locomotion.back_right.wheel_rpm", null, rWheelsMsg.toByteArray());
        this.channel.basicPublish(exchangeName, "sensor.locomotion.front_left.wheel_rpm", null, lWheelsMsg.toByteArray());
        this.channel.basicPublish(exchangeName, "sensor.locomotion.back_left.wheel_rpm", null, lWheelsMsg.toByteArray());

        // Stop when angle is reached
		while (!(Math.abs(currentPos.getHeading() - angle) < 0.05)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }

    private void driveTo(Position destPos) throws IOException {
	    /*
	    Time = distance / speed
	    Tell robot to drive
	    while !equalsWithinError(curr, dest, error)
	        sleep 100ms
        stop
	     */

	    // Drive
	    RpmUpdate driveMsg = RpmUpdate.newBuilder()
				.setRpm(TRAVEL_SPEED)
				.build();

		this.channel.basicPublish(exchangeName, "sensor.locomotion.front_right.wheel_rpm", null, driveMsg.toByteArray());
		this.channel.basicPublish(exchangeName, "sensor.locomotion.back_right.wheel_rpm", null, driveMsg.toByteArray());
		this.channel.basicPublish(exchangeName, "sensor.locomotion.front_left.wheel_rpm", null, driveMsg.toByteArray());
		this.channel.basicPublish(exchangeName, "sensor.locomotion.back_left.wheel_rpm", null, driveMsg.toByteArray());

	    // Stop when destination reached (within tolerance)
        while (!Position.equalsWithinError(currentPos, destPos, 0.1)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }

    @Override
    protected void runWithExceptions() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();

        // Listeners for commands
		String queueName = channel.queueDeclare().getQueue();
		this.channel.queueBind(queueName, exchangeName, "launch.transit");
		this.channel.basicConsume(queueName, true, new TransitLaunchConsumer(channel));

		queueName = channel.queueDeclare().getQueue();
		this.channel.queueBind(queueName, exchangeName, "softstop.transit");
		this.channel.basicConsume(queueName, true, new TransitSoftStopConsumer(channel));

		queueName = channel.queueDeclare().getQueue();
		this.channel.queueBind(queueName, exchangeName, "hardstop.transit");
		this.channel.basicConsume(queueName, true, new TransitHardStopConsumer(channel));

		queueName = channel.queueDeclare().getQueue();
		this.channel.queueBind(queueName, exchangeName, "newobstacle.transit");
		this.channel.basicConsume(queueName, true, new TransitNewObstacleConsumer(channel));
		
		queueName = channel.queueDeclare().getQueue();
		this.channel.queueBind(queueName, exchangeName, "loc.pos");
		this.channel.basicConsume(queueName, true, new LocalizationPositionConsumer(channel));

		// TODO Maybe don't use a while loop?
		while(!launched){
		    try{
			Thread.sleep(100L);
		    }catch(InterruptedException e){
			e.printStackTrace();
		    }
		}System.out.println("AutoTransit successfully launched.");
		while (currentPath.getPath().size() > 1) { // While we still have a position to go to
		    moveToPos(currentPath.getPath().remove(), currentPath.getPath().getFirst());
		}
    }

    public static void main(String[] args) {
		AutoTransit transitModule = new AutoTransit();
	    transitModule.start();
	}

}
