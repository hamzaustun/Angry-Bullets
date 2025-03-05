/**
 * Program is angry birds-like game, player tries to avoid the obstacles and shoot the targets
 * @author Enes Hamza Ustun, Student ID: 2022400237
 * @since Date: 20.03.2024
*/
import java.awt.event.KeyEvent;
import java.awt.*;
public class Main {
    public static void main(String[] args) {
        // Game Parameters
        int width = 1600; //screen width
        int height = 800; // screen height
        double gravity = 9.80665; // gravity
        double x0 = 120; // x and y coordinates of the bullet’s starting position on the platform
        double y0 = 120;
        double bulletVelocity = 180; // initial velocity
        double bulletAngle = 45.0; // initial angle
        double[][] obstacleArray = {
                {1200, 0, 60, 220},
                {1000, 0, 60, 160},
                {600, 0, 60, 80},
                {600, 180, 60, 160},
                {220, 0, 120, 180}
        };
        double[][] targetArray = {
                {1160, 0, 30, 30},
                {730, 0, 30, 30},
                {150, 0, 20, 20},
                {1480, 0, 60, 60},
                {340, 80, 60, 30},
                {1500, 600, 60, 60}
        };

        StdDraw.setCanvasSize(width, height); // set the size of the drawing canvas
        double scaleConstant = 3; // To scale everything according to our canvas
        StdDraw.setXscale(0, scaleConstant*width); // set the scale of the coordinate system
        StdDraw.setYscale(0, scaleConstant*height);
        StdDraw.enableDoubleBuffering(); // Use for faster animations
        int keyboardPauseDuration = 100; // set the sensitivity of keyboard
        for (int i = 0; i < obstacleArray.length; i++) { // scale the locations of obstacles
            for (int j = 0; j < obstacleArray[i].length; j++) {
                obstacleArray[i][j] *= scaleConstant;
            }
        }
        for (int i = 0; i <targetArray.length; i++) { // scale the locations of targets
            for (int j = 0; j < targetArray[i].length; j++) {
                targetArray[i][j] *= scaleConstant;
            }
        }
        x0 *= scaleConstant; // scale the initial points
        y0 *= scaleConstant;

        boolean gameStarted = false; // to start the loop of ball animation
        boolean restart = true; // to restart the game
        /**
         * This loop is the main loop of the game. It runs until the game is closed.
         * */
        while(true) {
            /**
             * This loop is drawing everything that occurs before the shooting animations start
             * It draws the targets and obstacles
             * In each loop, it controls whether the values of the velocity and angle are changed and draws the shooting line according to them
             */
            while (!gameStarted && restart) { // setting the environment before the animation
                StdDraw.clear(StdDraw.WHITE); // clear the canvas
                StdDraw.setPenColor(StdDraw.DARK_GRAY); // set pen color for the obstacles
                for (int i = 0; i < obstacleArray.length; i++) // loop for drawing the obstacles
                    StdDraw.filledRectangle((obstacleArray[i][0] + obstacleArray[i][2] / 2), (obstacleArray[i][1] + obstacleArray[i][3] / 2),
                            (obstacleArray[i][2] / 2), (obstacleArray[i][3] / 2)); // set the coordinates of the obstacles
                StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);// set pen color for the targets
                for (int i = 0; i < targetArray.length; i++) // loop for drawing the targets
                    StdDraw.filledRectangle((targetArray[i][0] + targetArray[i][2] / 2), (targetArray[i][1] + targetArray[i][3] / 2),
                            (targetArray[i][2] / 2), (targetArray[i][3] / 2)); // set the coordinates of the targets
                StdDraw.setPenColor(StdDraw.BLACK); // set the pen color for texts
                StdDraw.setPenRadius(0.01); // set the thickness of the pen
                StdDraw.setFont(new Font("Helvetica Bold", Font.BOLD, 20)); // set the font and its boldness and size
                StdDraw.filledRectangle(scaleConstant*60, scaleConstant*60, scaleConstant*60, scaleConstant*60); // draw the shooting platform
                // Keyboard inputs
                if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) { // check if the left key is pressed
                    StdDraw.pause(keyboardPauseDuration); // pause the animation for the keyboard pause duration
                    bulletVelocity -= 1; // decrement the velocity of the bullet
                }
                if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) { // check if the right key is pressed
                    StdDraw.pause(keyboardPauseDuration); // pause the animation for the keyboard pause duration
                    bulletVelocity += 1;  // increment the velocity of the bullet
                }
                if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) { // check if the up key is pressed
                    StdDraw.pause(keyboardPauseDuration);  // pause the animation for the keyboard pause duration
                    bulletAngle += 1;  // increment the shooting angle of the bullet
                }
                if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) { // check if the down key is pressed
                    StdDraw.pause(keyboardPauseDuration);  // pause the animation for the keyboard pause duration
                    bulletAngle -= 1;  // decrement the shooting angle of the bullet
                }
                StdDraw.line(scaleConstant*120, scaleConstant*120, scaleConstant*(120 + (2* (bulletVelocity - 145)) * Math.cos(bulletAngle * Math.PI / 180)),
                        scaleConstant*(120 + (2 * (bulletVelocity - 145)) * Math.sin(bulletAngle * Math.PI / 180))); // draw the shooting line
                StdDraw.setPenColor(StdDraw.WHITE); // set the pen color for texts
                StdDraw.text(scaleConstant*60, scaleConstant*90, "a: " + bulletAngle); // write the shooting angle on the shooting platform
                StdDraw.text(scaleConstant*60, scaleConstant*60, "v: " + bulletVelocity); // write the velocity on the shooting platform
                StdDraw.show(); // show the results
                if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) { // check if the space key is pressed
                    gameStarted = true; // if space key pressed and start the ball animation
                }
            }
            /**
             * This loop is for shooting animation
             * It calculates the position of the ball in each loop and draws it.
             * Also draws the trajectory of the ball
             */
            double xLine = x0; // values of the previous ball coordinates
            double yLine = y0;
            int pauseDuration = 33;
            double time = 0; // starting time of the animation
            while (gameStarted) { // if game started, start the animation
                double xt = x0 + bulletVelocity* Math.cos(Math.toRadians(bulletAngle)) * time; // the x coordinate of the ball over time
                double yt = y0 + bulletVelocity* Math.sin(Math.toRadians(bulletAngle)) * time - (0.5) * gravity * Math.pow(time, 2); // the y coordinate of the ball over time
                time += 0.3; // increase the time passed in very loop
                StdDraw.setPenColor(StdDraw.BLACK); // set the pen color for the ball
                StdDraw.filledCircle(xt, yt, scaleConstant*4); // draw the ball
                StdDraw.setPenRadius(0.001); // set the radius for the line between two balls
                StdDraw.line(xLine, yLine, xt, yt); // draw the line between two balls
                xLine = xt; // change the starting x point of new line
                yLine = yt; // change the starting y point of new line
                StdDraw.setPenRadius(0.01); // set the pen radius for the game ending texts
                for (int i = 0; i < obstacleArray.length; i++) { //check whether the ball hit the obstacles
                    if (obstacleArray[i][0] < xt && xt < (obstacleArray[i][0] + obstacleArray[i][2]) && obstacleArray[i][1] < yt && yt < obstacleArray[i][1] + obstacleArray[i][3]) { // check the coordinates of ball if they are in the obstacles' coordinates
                        StdDraw.textLeft(scaleConstant*15, scaleConstant*775, "Hit an obstacle. Please 'r' to shoot again."); // write the text to inform the player that the game has ended at the left top corner
                        gameStarted = false; // set the gameStarted false to stop the animation
                        restart = false; // to prevent the environment loop from deleting the trajectory and the game end texts
                        StdDraw.show(); // show what happened
                    }
                }
                for (int i = 0; i < targetArray.length; i++) { //check whether the ball hit the targets
                    if (targetArray[i][0] < xt && xt <(targetArray[i][0] + targetArray[i][2]) && targetArray[i][1] < yt && yt < (targetArray[i][1] + targetArray[i][3])) {
                        StdDraw.textLeft(scaleConstant*15, scaleConstant*775, "Congratulations: You hit the target."); // write the text to inform the player that the game ended has at the left top corner
                        gameStarted = false; // set the gameStarted false to stop the animation
                        restart = false; // to prevent the environment loop from deleting the trajectory and the texts
                        StdDraw.show(); // show what happened
                    }
                }
                if (yt < 0) { //check if the ball hit the ground
                    StdDraw.textLeft(scaleConstant*15, scaleConstant*775, "Hit the ground. Please 'r' to shoot again."); // write the text to inform the player that the game has ended at the left top corner
                    gameStarted = false; // set the gameStarted false to stop the animation
                    restart = false; // to prevent the environment loop from deleting the trajectory and the texts
                    StdDraw.show(); // show what happened
                }
                if (xt > scaleConstant*width) { //check if the ball exceeded the width
                    StdDraw.textLeft(scaleConstant*15, scaleConstant*775, "Max X reached. Please 'r' to shoot again."); // write the text to inform the player that the game has ended at the left top corner
                    gameStarted = false; // set the gameStarted false to stop the animation
                    restart = false; // to prevent the environment loop from deleting the trajectory and the texts
                    StdDraw.show(); // show what happened
                }
                StdDraw.show(); // show what happened
                StdDraw.pause(pauseDuration); // pause the frames for smooth animation

            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_R)){ // check whether the 'r' key is pressed
                restart = true; // set the restart true to clear the canvas and draw the initial position
                bulletVelocity = 180; // set the velocity to initial velocity
                bulletAngle = 45.0; // set the angle to initial angle
            }

        }
    }
}
/** original game scene
double[][] obstacleArray = {
        {600, 50, 20, 250},
        {500, 600, 20, 120},
        {520, 580, 120, 20},
        {680, 580, 100, 20},
        {570, 380, 20, 160},
        {620, 360, 30, 20},
        {650, 370, 60, 20},
        {710, 360, 30, 20},
        {600, 300, 160, 30},
        {1100, 660, 40, 40},
        {1000, 660, 40, 40},
        {1020, 620, 40, 40},
        {1120, 620, 40, 40},
        {1040, 580, 40, 40},
        {1140, 580, 40, 40},
        {1060, 540, 40, 40},
        {1160, 540, 40, 40},
        {1080, 500, 40, 40},
        {1180, 500, 40, 40},
        {1100, 460, 40, 40},
        {1200, 460, 40, 40},
        {1470, 600, 10, 10},
        {1480, 610, 30, 10},
        {1510, 600, 10, 10},
        {1520, 590, 10, 10},
        {1530, 580, 10, 10},
        {1510, 570, 50, 10},
        {1510, 560, 50, 10},
        {1490, 560, 90, 10},
        {1480, 550, 110, 10},
        {1480, 540, 30, 10},
        {1520, 540, 70, 10},
        {1470, 530, 130, 10},
        {1470, 520, 130, 10},
        {1480, 510, 70, 10},
        {1560, 510, 30, 10},
        {1480, 500, 70, 10},
        {1560, 500, 30, 10},
        {1490, 490, 50, 10},
        {1550, 490, 30, 10},
        {1510, 480, 50, 10}
};
double[][] targetArray = {
        {1470, 580, 10, 10},
        {1460, 570, 10, 10},
        {1450, 580, 10, 10},
        {520, 680, 20, 20},
        {680, 440, 30, 30},
        {1170, 400, 40, 40},
        {1480, 20, 60, 70}
};
 */
