# 6.00.2x Problem Set 2: Simulating robots

import math
import random

import ps2_visualize
import pylab

##################
## Comment/uncomment the relevant lines, depending on which version of Python you have
##################

# For Python 3.5:
#from ps2_verify_movement35 import testRobotMovement
# If you get a "Bad magic number" ImportError, you are not using Python 3.5 

# For Python 3.6:
from ps2_verify_movement36 import testRobotMovement
# If you get a "Bad magic number" ImportError, you are not using Python 3.6


# === Provided class Position
class Position(object):
    """
    A Position represents a location in a two-dimensional room.
    """
    def __init__(self, x, y):
        """
        Initializes a position with coordinates (x, y).
        """
        self.x = x
        self.y = y
        
    def getX(self):
        return self.x
    
    def getY(self):
        return self.y
    
    def getNewPosition(self, angle, speed):
        """
        Computes and returns the new Position after a single clock-tick has
        passed, with this object as the current position, and with the
        specified angle and speed.

        Does NOT test whether the returned position fits inside the room.

        angle: number representing angle in degrees, 0 <= angle < 360
        speed: positive float representing speed

        Returns: a Position object representing the new position.
        """
        old_x, old_y = self.getX(), self.getY()
        angle = float(angle)
        # Compute the change in position
        delta_y = speed * math.cos(math.radians(angle))
        delta_x = speed * math.sin(math.radians(angle))
        # Add that to the existing position
        new_x = old_x + delta_x
        new_y = old_y + delta_y
        return Position(new_x, new_y)

    def __str__(self):  
        return "(%0.2f, %0.2f)" % (self.x, self.y)


# === Problem 1
class RectangularRoom(object):
    """
    A RectangularRoom represents a rectangular region containing clean or dirty
    tiles.

    A room has a width and a height and contains (width * height) tiles. At any
    particular time, each of these tiles is either clean or dirty.
    """
    def __init__(self, width, height):
        """
        Initializes a rectangular room with the specified width and height.

        Initially, no tiles in the room have been cleaned.

        width: an integer > 0
        height: an integer > 0
        """
        self.width = width
        self.height = height
        self.cleanedtiles = []
    
    def cleanTileAtPosition(self, pos):
        """
        Mark the tile under the position POS as cleaned.

        Assumes that POS represents a valid position inside this room.

        pos: a Position
        """
        x = int(pos.getX())
        y = int(pos.getY())
        
        if [x,y] not in self.cleanedtiles:
            self.cleanedtiles.append([x,y])

    def isTileCleaned(self, m, n):
        """
        Return True if the tile (m, n) has been cleaned.

        Assumes that (m, n) represents a valid tile inside the room.

        m: an integer
        n: an integer
        returns: True if (m, n) is cleaned, False otherwise
        """
        return [m,n] in self.cleanedtiles
    
    def getNumTiles(self):
        """
        Return the total number of tiles in the room.

        returns: an integer
        """
        return self.width*self.height

    def getNumCleanedTiles(self):
        """
        Return the total number of clean tiles in the room.

        returns: an integer
        """
        return len(self.cleanedtiles)

    def getRandomPosition(self):
        """
        Return a random position inside the room.

        returns: a Position object.
        """
        x = random.random()*self.width
        y = random.random()*self.height
        return Position(x,y)

    def isPositionInRoom(self, pos):
        """
        Return True if pos is inside the room.

        pos: a Position object.
        returns: True if pos is in the room, False otherwise.
        """
        x = pos.getX()
        y = pos.getY()
        return 0<=x < self.width and 0<=y < self.height


# === Problem 2
class Robot(object):
    """
    Represents a robot cleaning a particular room.

    At all times the robot has a particular position and direction in the room.
    The robot also has a fixed speed.

    Subclasses of Robot should provide movement strategies by implementing
    updatePositionAndClean(), which simulates a single time-step.
    """
    def __init__(self, room, speed):
        """
        Initializes a Robot with the given speed in the specified room. The
        robot initially has a random direction and a random position in the
        room. The robot cleans the tile it is on.

        room:  a RectangularRoom object.
        speed: a float (speed > 0)
        """
        self.room = room
        self.speed = float(speed)
        self.position = self.room.getRandomPosition()
        self.direction = int(random.random()*360)
        self.room.cleanTileAtPosition(self.position)

    def getRobotPosition(self):
        """
        Return the position of the robot.

        returns: a Position object giving the robot's position.
        """
        return self.position
    
    def getRobotDirection(self):
        """
        Return the direction of the robot.

        returns: an integer d giving the direction of the robot as an angle in
        degrees, 0 <= d < 360.
        """
        return self.direction

    def setRobotPosition(self, position):
        """
        Set the position of the robot to POSITION.

        position: a Position object.
        """
        self.position = position

    def setRobotDirection(self, direction):
        """
        Set the direction of the robot to DIRECTION.

        direction: integer representing an angle in degrees
        """
        self.direction = int(direction)

    def updatePositionAndClean(self):
        """
        Simulate the passage of a single time-step.

        Move the robot to a new position and mark the tile it is on as having
        been cleaned.
        """
        raise NotImplementedError # don't change this!


# === Problem 3
class StandardRobot(Robot):
    """
    A StandardRobot is a Robot with the standard movement strategy.

    At each time-step, a StandardRobot attempts to move in its current
    direction; when it would hit a wall, it *instead* chooses a new direction
    randomly.
    """
    def updatePositionAndClean(self):
        """
        Simulate the passage of a single time-step.

        Move the robot to a new position and mark the tile it is on as having
        been cleaned.
        """
        # if goes beyond the boundary it should be set so that eg if x>getX() then should change direction
        while True:
            newpos = self.position.getNewPosition(self.direction,self.speed)
            if self.room.isPositionInRoom(newpos):
                self.position = self.position.getNewPosition(self.direction, self.speed)
                self.room.cleanTileAtPosition(self.position)
                break
            else:
                self.setRobotDirection(random.randrange(361))
                break
            

#
## Uncomment this line to see your implementation of StandardRobot in action!
#testRobotMovement(StandardRobot, RectangularRoom)


## === Problem 4
#def runSimulation(num_robots, speed, width, height, min_coverage, num_trials,
#                  robot_type):
#    """
#    Runs NUM_TRIALS trials of the simulation and returns the mean number of
#    time-steps needed to clean the fraction MIN_COVERAGE of the room.
#
#    The simulation is run with NUM_ROBOTS robots of type ROBOT_TYPE, each with
#    speed SPEED, in a room of dimensions WIDTH x HEIGHT.
#
#    num_robots: an int (num_robots > 0)
#    speed: a float (speed > 0)
#    width: an int (width > 0)
#    height: an int (height > 0)
#    min_coverage: a float (0 <= min_coverage <= 1.0)
#    num_trials: an int (num_trials > 0)
#    robot_type: class of robot to be instantiated (e.g. StandardRobot or
#                RandomWalkRobot)
#    """
#    
#    def onesim(num_robots, speed, width, height, min_coverage, robot_type):
#        tag = 1
#        list = []
#        room = RectangularRoom(width,height)
#        type = robot_type(room, speed)
#        
#        for robot in range(num_robots):
#            list.append('r'+str(tag)+'=type')
#            tag += 1
#        
#    #robots tagged from 0 - n
#    
#        for robotinit in list:
#            exec(robotinit)
#            
#        time = 0
#        while (room.getNumCleanedTiles()/room.getNumTiles()) < min_coverage:
#            time += 1
#            #each robot needs to move once
#            for robot in list:
#                exec(robot[:2]+'.updatePositionAndClean()')
#        return time
#    
#    total = 0
#    for trials in range(num_trials):
#        total += onesim(num_robots, speed, width, height, min_coverage, robot_type)
#        
#    return total/num_trials

# Uncomment this line to see how much your simulation takes on average
##print(runSimulation(1, 1.0, 10, 10, 0.75, 30, StandardRobot))
def runSimulation(num_robots, speed, width, height, min_coverage, num_trials,
                  robot_type):
#    tag = 1
#    rlist = []
#    room = RectangularRoom(width,height)
#    rtype = robot_type(room,speed)
#    for robot in range(num_robots):
#        rlist.append('r'+str(tag)+'=type')
#        tag+=1
#    
#    for robotinit in rlist:
#        exec(robotinit)
        
        #robots and rooms only initialised once, remember to reset room and randomise locations of robots after
        #each trial
        
    total = 0
    for trials in range(num_trials):
        
        tag = 1
        rlist = []
        room = RectangularRoom(width,height)
        for robot in range(num_robots):
            rlist.append('r'+str(tag)+'=robot_type(room, speed)')
            tag+=1
    
        for robotinit in rlist:
            exec(robotinit)
        
        time = 0
        while (room.getNumCleanedTiles()/room.getNumTiles()) < min_coverage:
            time += 1
            for robot in rlist:
                exec(robot[:2]+'.updatePositionAndClean()')
                
        total += time
            
    return total/num_trials
            
        
    
    

# === Problem 5
class RandomWalkRobot(Robot):
    """
    A RandomWalkRobot is a robot with the "random walk" movement strategy: it
    chooses a new direction at random at the end of each time-step.
    """
    def updatePositionAndClean(self):
        """
        Simulate the passage of a single time-step.

        Move the robot to a new position and mark the tile it is on as having
        been cleaned.
        """
        # if goes beyond the boundary it should be set so that eg if x>getX() then should change direction
        while True:
            
            newpos = self.position.getNewPosition(self.direction,self.speed)
            if self.room.isPositionInRoom(newpos):
                self.position = self.position.getNewPosition(self.direction, self.speed)
                self.room.cleanTileAtPosition(self.position)
                self.setRobotDirection(random.randrange(361))
                break
            else:
                self.setRobotDirection(random.randrange(361))
                break


def showPlot1(title, x_label, y_label):
    """
    What information does the plot produced by this function tell you?
    """
    num_robot_range = range(1, 11)
    times1 = []
    times2 = []
    for num_robots in num_robot_range:
        print("Plotting", num_robots, "robots...")
        times1.append(runSimulation(num_robots, 1.0, 20, 20, 0.8, 20, StandardRobot))
        times2.append(runSimulation(num_robots, 1.0, 20, 20, 0.8, 20, RandomWalkRobot))
    pylab.plot(num_robot_range, times1)
    pylab.plot(num_robot_range, times2)
    pylab.title(title)
    pylab.legend(('StandardRobot', 'RandomWalkRobot'))
    pylab.xlabel(x_label)
    pylab.ylabel(y_label)
    pylab.show()
                
#            
#def showPlot1(title, x_label, y_label):
#    """
#    What information does the plot produced by this function tell you?
#    """
#    num_robot_range = range(1, 11)
#    times1 = []
#    times2 = []
#    for num_robots in num_robot_range:
#        print("Plotting", num_robots, "robots...")
#        times1.append(runSimulation(num_robots, 1.0, 20, 20, 0.8, 20, StandardRobot))
#        times2.append(runSimulation(num_robots, 1.0, 20, 20, 0.8, 20, RandomWalkRobot))
#    pylab.plot(num_robot_range, times1, label = '1')
#    pylab.plot(num_robot_range, times2, label = '2')
#    pylab.title(title)
#    pylab.legend()
#    pylab.xlabel(x_label)
#    pylab.ylabel(y_label)
#    pylab.show()

    
def showPlot2(title, x_label, y_label):
    """
    What information does the plot produced by this function tell you?
    """
    aspect_ratios = []
    times1 = []
    times2 = []
    for width in [10, 20, 25, 50]:
        height = 300//width
        print("Plotting cleaning time for a room of width:", width, "by height:", height)
        aspect_ratios.append(float(width) / height)
        times1.append(runSimulation(2, 1.0, width, height, 0.8, 200, StandardRobot))
        times2.append(runSimulation(2, 1.0, width, height, 0.8, 200, RandomWalkRobot))
    pylab.plot(aspect_ratios, times1)
    pylab.plot(aspect_ratios, times2)
    pylab.title(title)
    pylab.legend(('StandardRobot', 'RandomWalkRobot'))
    pylab.xlabel(x_label)
    pylab.ylabel(y_label)
    pylab.show()
    

# === Problem 6
# NOTE: If you are running the simulation, you will have to close it 
# before the plot will show up.

#
# 1) Write a function call to showPlot1 that generates an appropriately-labeled
#     plot.
#
#       (... your call here ...)
#

#
# 2) Write a function call to showPlot2 that generates an appropriately-labeled
#     plot.
#
#       (... your call here ...)
#
