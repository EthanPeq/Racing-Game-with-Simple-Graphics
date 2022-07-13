import java.awt.*;
import java.awt.geom.Ellipse2D;


public abstract class Player{
	protected int position;
	protected int moveTypeNum;
	protected int nRound;
	protected int x,y;
	protected String playerName;
	
	public Player() {
		this("Unknown");
	}
	
	public Player(String name){
		position = 0;
		moveTypeNum = 1;
		playerName = name;
		nRound = 0;
	}
		
	
	
	public String getName(){
		return playerName; 
	}
	
	public int getPosition() {
		return position;
	}
	
	public void advance(int topPosition, int priorPlayerPosition) {
		if(moveTypeNum == 3) {
			moveType3(priorPlayerPosition);
		}
		else if(moveTypeNum == 2) {
			moveType2();
		}
		else {
			moveType1(topPosition);
		}	
		if(position < 0) {
			position = 0;
		}
	}

	public void moveType1(int topPosition){ 
		int advanceNum = rollDice()  + Math.abs((topPosition - position ) / 2);
		if(advanceNum <= 2)
			advanceNum = advanceNum * -1;
		position += advanceNum;
		return;
	}
	
	public void moveType2() { 
		int advanceNum = rollDice()*3;
		position += advanceNum;
		return;
	}
	
	public void moveType3(int lastPlayerPosition) { 
		int advanceNum = rollDice() + Math.abs((position - lastPlayerPosition)/2);
		if(advanceNum >= 3) 
			advanceNum = advanceNum * -1;
		position += advanceNum;
		return;
	}
	
	public int rollDice() {
		return (int)(Math.random()*6 + 1);
	}
	
	public abstract void play(int t, int p);
	public abstract void setPosition(int  x, int y);
}


class RacingGamePlayer extends Player{
	
	
	public RacingGamePlayer() {
		super();
	}
	
	public RacingGamePlayer(String name){
		super(name);
	
	}

	@Override
	public void play(int topPosition, int priorPlayerPosition) {
		if(nRound == 0) {
			nRound = (int) (Math.random()*3 + 3); 
			moveTypeNum = (int) (Math.random()*4 + 1);
		}
		
		advance(topPosition, priorPlayerPosition);
		nRound--;
	}
	
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Shape getRacer(){
		return new Ellipse2D.Double(x, y, 15, 15);
	}	
	
	public int getX() {return x;}
	public int getY() {return y;}
	

}
