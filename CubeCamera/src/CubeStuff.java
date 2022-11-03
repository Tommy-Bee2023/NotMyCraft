import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;


public class CubeStuff extends JFrame implements KeyListener
{
	private DrawArea drawArea;
	private JPanel canvas;
	private int cameraDist;
	private Vector[][] coords;
	private int[][][] XYcoords;
	// [Quads][Points][Coords]
	private int[][][] cube = {
								{{-1, 1, 1}, {-1, -1, 1}, {-1, -1, -1}, {-1, 1,-1}},
								{{1, 1, 1}, {-1, 1, 1}, {-1, 1, -1}, {1, 1, -1}},
								{{1, -1, 1}, {-1, -1, 1}, {-1, -1, -1}, {1, -1, -1}},
								{{1, 1, 1}, {-1, 1, 1}, {-1, -1, 1}, {1, -1,1}},
								{{1, 1, -1}, {-1, 1, -1}, {-1, -1, -1}, {1, -1, -1}},
								{{1, 1, 1}, {1, -1, 1}, {1, -1, -1}, {1, 1,-1}},					
							 };
	private int[] center;
	private Vector cameraPos;
	private Vector cameraDir;
	private Vector right;
	private Vector up;
	
	public CubeStuff(int len)
	{
		addKeyListener(this);
		
		drawArea = new DrawArea();
		canvas = new JPanel();
		canvas.add(drawArea);
		drawArea.requestFocus();
		this.setContentPane(canvas);
		setBackground(Color.white);

		cameraPos = new Vector (100, 0, 0);
		cameraDir = new Vector (-100, 0,  0);
		up = new Vector (0, 0, 1);
		right = cameraDir.cross(up);
		
		coords = new Vector[cube.length][4];
		
		XYcoords = new int[cube.length][4][2];
		center = new int[2];
		center[0] = 300;
		center[1] = 300;
		
		copyTo(len);
	}
	
	private void copyTo(int side) {
		for(int i = 0; i < coords.length; i++) {
				for(int j = 0; j < coords[i].length; j++) {
					coords[i][j] = new Vector(side * cube[i][j][0],side * cube[i][j][1], side * cube[i][j][2]);
				}
		}
	}
	
	public void keyPressed(KeyEvent e)
	{
			if (e.getKeyCode() == KeyEvent.VK_W)
			{
				forwardMove(5);
			}
			else if (e.getKeyCode() == KeyEvent.VK_S)
			{
				forwardMove(-5);
				//System.out.println(cameraPos.abs());
			}
			else if (e.getKeyCode() == KeyEvent.VK_D)
			{
				sideMove(-5);
			}
			else if (e.getKeyCode() == KeyEvent.VK_A)
			{
				sideMove(5);
			}
			/*
			else if (e.getKeyCode() == KeyEvent.VK_W)
			{
				zMove(2);
			}
			else if (e.getKeyCode() == KeyEvent.VK_S)
			{
				zMove(-2);
			}*/
			else if (e.getKeyCode() == KeyEvent.VK_UP) {
				vertCam(5);
			}
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				vertCam(-5);
			}
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				horizCam(5);
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				horizCam(-5);
			}
			right = cameraDir.cross(up);
			repaint();
	}	
	public void keyReleased(KeyEvent e)
	{
	}	
	public void keyTyped(KeyEvent e)
	{
	}
	
	private void zMove(int dist) {
		cameraPos = cameraPos.add(new Vector(0, 0, dist));
	}
	private void sideMove(int dist) {
		Vector change = cameraDir.add(new Vector(0, 0, -cameraDir.z())).cross(up).normalize().scale(dist);
		cameraPos = cameraPos.add(change);
	}
	private void forwardMove(int dist) {
		Vector change = cameraDir.add(new Vector(0, 0, -cameraDir.z())).normalize().scale(dist);
		cameraPos = cameraPos.add(change);
	}
	private void horizCam(int dist){
		cameraDir = cameraDir.add(new Vector(0,dist,0)).normalize().scale(100);
	}
	private void vertCam(int dist){
		cameraDir = cameraDir.add(new Vector(0,0,dist)).normalize().scale(100);
	}
	
	
	private void draw(Graphics graphics) {
		for(int i = 0; i < XYcoords.length; i++) {
			graphics.setColor(genColor((int)((i+1) * (300.0/XYcoords.length))));
			graphics.fillPolygon(xCoords(XYcoords[i]), yCoords(XYcoords[i]), XYcoords[i].length);
		}
	}
	
	private void calcXY() {
		CameraPlane intPlane = new CameraPlane(cameraPos, cameraDir, right, 5*cameraPos.abs());
		CameraPlane cPlane = new CameraPlane(cameraPos, cameraDir, right, 0);
		for(int i = 0; i < coords.length; i++) {
			for(int j = 0; j < coords[i].length; j++) {
				Line arrow = new Line(coords[i][j], cameraPos);
				if(!cPlane.visible(coords[i][j])) {
					arrow = new Line(cPlane.intersect(arrow), cameraPos);
					System.out.println(arrow.slope().dot(cameraDir));
				}
				XYcoords[i][j] = intPlane.getCoords(intPlane.intersect(arrow)); 
				if(!cPlane.visible(coords[i][j])) {
				}
			}
		}
	}
	
	private void calcGround(Graphics graphics) {
		CameraPlane plane = new CameraPlane(cameraPos, cameraDir, right, 5*cameraPos.abs());
		for(int i = -1000; i < 1000; i++) {
				Line start = new Line(new Vector(5*i, -1000, 0), cameraPos);
				Line end = new Line(new Vector(5*i, 1000, 0), cameraPos);
				int[] startPoint = plane.getCoords(plane.intersect(start));
				int[] endPoint = plane.getCoords(plane.intersect(end));
				graphics.drawLine(startPoint[0], startPoint[1], endPoint[0],  endPoint[1]);
				
			}
	}
	
	private void sortXY() {
		for(int i = 0; i < coords.length; i++) {
			double max = -1;
			int maxIndex = -1;
			for(int j = i; j < coords.length; j++) {
				double dist = avg(j).dist(cameraPos);
				if(dist > max) {
					max = dist;
					maxIndex = j;
				}
			}
			//System.out.println(i + " " + maxIndex);
			swap(XYcoords, i, maxIndex);
			swap(coords, i, maxIndex);
		}
	}
	
	private int[] xCoords(int[][] arr) {
		int[] x = new int[arr.length];
		for(int i = 0; i < arr.length; i++) {
			x[i] = arr[i][0] + center[0];
		}
		return x;
	}
	
	private int[] yCoords(int[][] arr) {
		int[] x = new int[arr.length];
		for(int i = 0; i < arr.length; i++) {
			x[i] = arr[i][1]+ center[1];
		}
		return x;
	}
	
	private int rand() {
		return (int)(Math.random() * 256);
	}
	
	private Color genColor(int i) {
		int a = limit(i-100);	
		int b = limit(i-200);	
		int c = limit(i-300);	
		return new Color(a+55, b+55, c+55);
	}
	
	private int limit(int i) {
		if (i < 0) {
			return 0;
		}
		if(i > 100) {
			return 100;
		}
		return i;
	}
	
	private Vector avg(int i) {
		double xTotal = 0;
		double yTotal = 0;
		double zTotal = 0;
		for(int j = 0; j < coords[i].length; j++) {
			xTotal += coords[i][j].x();
			yTotal += coords[i][j].y();
			zTotal += coords[i][j].z();
		}
		Vector res = (new Vector(xTotal, yTotal, zTotal)).scale(1.0/coords[i].length);
		return res;
	}
	
	private void swap(int[][][] arr, int i, int j) {
		int[][] temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	private void swap(Vector[][] arr, int i, int j) {
		Vector[] temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
	private void print(int[][] arr) {
		for(int i = 0; i < arr.length; i++) {
			for(int j = 0; j < arr[i].length; j++) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public class DrawArea extends JPanel{
		public DrawArea(){	
			this.setPreferredSize(new Dimension(600,600));
			this.setMaximumSize(new Dimension(600, 600));
		}
		
		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			//drawAxes(g);
			//calcGround(g);
			calcXY();
			sortXY();
			draw(g);
			
		}
	}
}