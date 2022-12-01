package me.Josh123likeme.RendererTemplate;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import me.Josh123likeme.RendererTemplate.InputListener.KeyboardWitness;
import me.Josh123likeme.RendererTemplate.InputListener.MouseWitness;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int INITIAL_WIDTH = 400, INITIAL_HEIGHT = 400;
	
	private Thread thread;
	private boolean running = false;
	
	private MouseWitness mouseWitness;
	private KeyboardWitness keyboardWitness;
	
	private double deltaFrame;
	private int fps;
	
	public Game() {
		
		new Window(INITIAL_WIDTH, INITIAL_HEIGHT, "Window Name", this);
		
		initInputs();
		
	}
	
	private void initInputs() {
		
		mouseWitness = new MouseWitness();
		keyboardWitness = new KeyboardWitness();
		
		addMouseListener(mouseWitness);
		addMouseMotionListener(mouseWitness);
		addKeyListener(keyboardWitness);
		
		requestFocus();
		
	}
	
	public synchronized void start() {
		
		thread = new Thread(this);
		thread.start();
		running = true;
		
	}
	
	public synchronized void stop() {
		
		try 
		{
			thread.join();
			running = false;
		}
		
		catch(Exception e) {e.printStackTrace();}
		
	}
	
	public void run() {
		
		double targetfps = 60d;
		long targetDeltaFrame = Math.round((1d / targetfps) * 1000000000);
		long lastSecond = System.nanoTime();
		int frames = 0;
		
		long lastFrame = 0;
		
		while (running) {
			
			frames++;
			
			if (lastSecond + 1000000000 < System.nanoTime()) {
				
				fps = frames;
				
				frames = 0;
				
				lastSecond = System.nanoTime();
				
				targetDeltaFrame = Math.round((1d / targetfps) * 1000000000);
				
			}
			
			//starting to push frame
			
			long nextTime = System.nanoTime() + targetDeltaFrame;
			
			deltaFrame = ((double) (System.nanoTime() - lastFrame)) / 1000000000;
			
			lastFrame = System.nanoTime();
			
			paint();
			
			keyboardWitness.purgeTypedKeys();
			mouseWitness.purgeClickedButtons();
			
			//finished pushing frame
			
			while (nextTime > System.nanoTime());
			
		}
		stop();
		
	}
	
	private void tick() {
		
	}

	private void paint() {
	
		BufferStrategy bufferStrategy = this.getBufferStrategy();
		if(bufferStrategy == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics graphics = bufferStrategy.getDrawGraphics();
		
		//basic black background to stop flashing
		graphics.setColor(Color.black); 
		graphics.fillRect(0, 0, getWidth(), getHeight());
		
		//put rendering stuff here
		
		//this pushes the graphics to the window
		bufferStrategy.show();
		
	}
	
}
