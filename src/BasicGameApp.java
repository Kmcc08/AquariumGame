import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


//*******************************************************************************
// Class Definition Section

public class BasicGameApp implements Runnable {

	//Variable Definition Section
	//Declare the variables used in the program
	//You can set their initial values too

	//Sets the width and height of the program window
	final int WIDTH = 1000;
	final int HEIGHT = 700;

	//Declare the variables needed for the graphics
	public JFrame frame;
	public Canvas canvas;
	public JPanel panel;
public int counter =0;
public int counter2 =0;
public int collissioncounter=0;
public int fireCounter = 0;
	public BufferStrategy bufferStrategy;
	public Image DinosaurPic;
	public Image badGuyPic;
	public Image BackgroundPic;
	public Image FirePic;
	public Image swordPic;
	public Image GameOverPic;
	//Declare the objects used in the program
	//These are things that are made up of more than one variable type
	private Character Dinosaur;
	private Character badGuy;
	private Character Fire;
	private Character sword;
	private Character gameover;


	// Main method definition
	// This is the code that runs first and automatically
	public static void main(String[] args) {
		BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
		new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
	}


	// Constructor Method
	// This has the same name as the class
	// This section is the setup portion of the program
	// Initialize your variables and construct your program objects here.
	public BasicGameApp() {

		setUpGraphics();

		//variable and objects
		//create (construct) the objects needed for the game and load up
		DinosaurPic = Toolkit.getDefaultToolkit().getImage("dinosaur.png");
		FirePic = Toolkit.getDefaultToolkit().getImage("fire.jpg");
		swordPic = Toolkit.getDefaultToolkit().getImage("swords.jpg");
		badGuyPic = Toolkit.getDefaultToolkit().getImage("Screenshot 2024-12-18 115637.png");//load the picture
		BackgroundPic = Toolkit.getDefaultToolkit().getImage("cave.png");
		GameOverPic = Toolkit.getDefaultToolkit().getImage("GameOver.jpg");
		Dinosaur = new Character(10, 100);
		badGuy = new Character(600, 600);
		Fire = new Character(200, 400);
		sword = new Character(600,350);
		gameover = new Character(500,350);
		Fire.isAlive = false;
		sword.isAlive = false;
		badGuy.isAlive = true;
		gameover.isAlive = false;


	}// BasicGameApp()


//*******************************************************************************
//User Method Section
//
// put your code to do things here.

	// main thread
	// this is the code that plays the game after you set things up
	public void run() {

		//for the moment we will loop things forever.
		while (true) {

			moveThings();  //move all the game objects
			render();  // paint the graphics
			pause(20); // sleep for 10 ms
		}
	}


	public void moveThings() {
if(sword.isAlive) {
	counter++;
}
if(Fire.isAlive){
	fireCounter++;
}

if(badGuy.isAlive ==false){  // this is showing how one character would get killed if it was a video game
	counter2++;             // the counter makes sure that the bad guy comes back after a certian amount of time
}
		//calls the move( ) code in the object
		collisions();
		Dinosaur.bounce();
		//badGuy.bounce();


		if(counter> 100){ // counter for sword picture disapear
			sword.isAlive=false;   // the sword image is showing how the characters are fighting like in a video game
			counter =0;
		}
		if(counter2>100){ //bad guy coming back to life
			badGuy.isAlive=true;
			counter2=0;
		}
		if(fireCounter>100){
			Fire.isAlive = false;
			fireCounter = 0;
		}

		if(collissioncounter<3 && collissioncounter>1){
			badGuy.wrap();
			//Fire.isAlive = true;

		} else if(collissioncounter< 4 && collissioncounter>2){

           gameover.isAlive = true;
		   badGuy.isAlive = false;
		   Dinosaur.isAlive = false;
			System.out.println("END OF GAME");// When this happens it is the end of the project

		}
		else{
			badGuy.bounce();

		}



	}

	public void collisions() {
		if (Dinosaur.rec.intersects(badGuy.rec) && Dinosaur.isCrashing == false) {
			collissioncounter++;
			System.out.println(" explosion!!!!!!! ");
			Dinosaur.dx = -Dinosaur.dx;
			Dinosaur.dy = -Dinosaur.dy;
			badGuy.dx = -badGuy.dx;
			badGuy.dy = -badGuy.dy;
			Dinosaur.isCrashing = true;
			System.out.println("-1 life");
			badGuy.isAlive = false;

           if(collissioncounter==1){
			   sword.isAlive=true;
		   }
		   if(collissioncounter==2){
			   Fire.isAlive = true;
		   }

		   if(collissioncounter == 3){
			   gameover.isAlive = true;
		   }


		}
		if (!Dinosaur.rec.intersects(badGuy.rec)) {
			Dinosaur.isCrashing = false;
		//	sword.isAlive = false;
		//	Fire.isAlive = false;

		}


	}

	//Pauses or sleeps the computer for the amount specified in milliseconds
	public void pause(int time) {
		//sleep
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {

		}
	}

	//Graphics setup method
	private void setUpGraphics() {
		frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

		panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
		panel.setLayout(null);   //set the layout

		// creates a canvas which is a blank rectangular area of the screen onto which the application can draw
		// and trap input events (Mouse and Keyboard events)
		canvas = new Canvas();
		canvas.setBounds(0, 0, WIDTH, HEIGHT);
		canvas.setIgnoreRepaint(true);

		panel.add(canvas);  // adds the canvas to the panel.

		// frame operations
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
		frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
		frame.setResizable(false);   //makes it so the frame cannot be resized
		frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

		// sets up things so the screen displays images nicely.
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		canvas.requestFocus();
		System.out.println("DONE graphic setup");

	}


	//paints things on the screen using bufferStrategy
	private void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);

		//draw the image of the Dinosaurnaut
		g.drawImage(BackgroundPic, 0, 0, WIDTH, HEIGHT, null);

		if(Dinosaur.isAlive == true){
			g.drawImage(DinosaurPic, Dinosaur.xpos, Dinosaur.ypos, Dinosaur.width, Dinosaur.height, null);
		}
	//	if (Fire.isAlive == true) {
		//	g.drawImage(FirePic, Fire.xpos, Fire.ypos, Fire.width, Fire.height, null);
		//}
		if (sword.isAlive == true) {
			//System.out.println("alive");
			g.drawImage(swordPic, sword.xpos, sword.ypos, sword.width, sword.height, null);
		}

        if (badGuy.isAlive == true){
			g.drawImage(badGuyPic, badGuy.xpos, badGuy.ypos, badGuy.width, badGuy.height, null);
		}
		if(gameover.isAlive==true){
			g.drawImage(GameOverPic, gameover.xpos, gameover.ypos, gameover.width, gameover.height, null);
		}
		if(Fire.isAlive==true){
			g.drawImage(FirePic, Fire.xpos, Fire.ypos, Fire.width, Fire.height, null);
		}

		g.dispose();


		bufferStrategy.show();

	}
}