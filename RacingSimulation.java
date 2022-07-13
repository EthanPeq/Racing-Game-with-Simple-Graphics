import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//need racers .setPosition and .drive
// .drive got to be similar to .ripple

	public class RacingSimulation {
		public static void main(String[] args) {		
			RaceSimuFrame fr= new RaceSimuFrame("Racer Frame");
			fr.setVisible(true);;
		}
	}

	abstract class AnimationFrame extends JFrame{
		protected Thread animator;
		protected JPanel pnl;
		
		public AnimationFrame(String title){
			super(title);
			this.setSize(600, 600);
			this.setLocation(300, 100);
			pnl = getAnimationPanel();
			this.add(pnl);
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					animator.interrupt();
					e.getWindow().dispose();
					System.exit(0);
				}
			});
			init();
			animator = new Thread(getTask());
			animator.start();
		}
		
		public void repaint(){
			super.repaint();
			pnl.repaint();
		}	
		
		abstract void init();
		abstract Runnable getTask();
		abstract JPanel getAnimationPanel();
	}

	class RaceSimuFrame extends AnimationFrame{
		private RacingGamePlayer[] racers;
		Player prior = null;
		int winner = 0;
		
		final int finish = 400;
		
		public RaceSimuFrame(String title){
			super(title);
		}

		//Create racers and starting position
		@Override
		public void init() {
			racers = new RacingGamePlayer[8];
			for(int i = 0; i < racers.length; i++){
				racers[i] = new RacingGamePlayer("Player " + (i+1));
				racers[i].setPosition((25), ((i+1)* 50));
			}		
		}	

		@Override
		Runnable getTask() {
			return new RainTask();
		}
		
		//draw racers
		@Override
		JPanel getAnimationPanel() {
			class RacePanel extends JPanel{		
				public RacePanel(){
					this.setBackground(Color.white);
				}			
				public void paintComponent(Graphics g){
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.drawLine(100, 50, 100, 500);
					g2.drawLine(500, 50, 500, 500);
					for(int i = 0; i < racers.length; i++){
						RacingGamePlayer racer = racers[i];
						g2.draw(racers[i].getRacer());
					}
				}
			}//end RacePanel
			return new RacePanel();
		}
		
		private Player getPriorPlayer(int i){
			 Player prior = null;
			 if(i == 0){
				 int ind = racers.length-1;
				 if(ind > 0){
					 prior = racers[ind];
				 }
			 }
			 else {
				 prior = racers[i-1];
			 }
			 return prior;
		}	
		
		//move the racers
		class RainTask implements Runnable{
			@Override
			public void run() {
				while( winner == 0 ){
					for(int i = 0; i < racers.length; i++){
						int topPosition = 0;
						RacingGamePlayer racer = racers[i];
						
						if(racer.getPosition() > finish) continue;
						prior = getPriorPlayer(i);
						if(prior == null) return;
						if(racer.getPosition() > topPosition) {
							topPosition = racer.getPosition();
						}
						int priorPlayerPosition = prior.getPosition();
						racer.play(topPosition, priorPlayerPosition);
						racer.setPosition(racer.getPosition() + 100, racer.getY());
						
						if (racer.getPosition() >= finish){
				    		pnl.add(new JLabel("The Winner is: " + racer.getName()));
				    		winner = 1;
				    		repaint();
				    	} 
						
					}
					try{
						Thread.sleep(30);
					}catch(InterruptedException e){
						System.out.println(e.getStackTrace().toString());
					}
					pnl.repaint();
				}			
			}	
		}
	}
	