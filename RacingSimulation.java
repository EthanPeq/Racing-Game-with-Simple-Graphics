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
		
		public RaceSimuFrame(String title){
			super(title);
		}

		//Create racers and starting position
		@Override
		public void init() {
			racers = new RacingGamePlayer[10];
			for(int i = 0; i < racers.length; i++){
				racers[i] = new RacingGamePlayer("Player " + (i+1));
				racers[i].setPosition((int) (Math.random() * this.getWidth()), (int) (Math.random() * this.getHeight()));
			}		
		}	

		@Override
		Runnable getTask() {
			return new RainTask();
		}
		
		//draw racers
		@Override
		JPanel getAnimationPanel() {
			class RainPanel extends JPanel{		
				public RainPanel(){
					this.setBackground(Color.white);
				}			
				public void paintComponent(Graphics g){
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					for(int i = 0; i < racers.length; i++){
						RacingGamePlayer racer = racers[i];
						//g2.draw(drops[i].getDrop());
						g2.drawImage(racer.getImage(), 100 , 100 *(i+1), null); 
					}
				}
			}//end RainPanel
			return new RainPanel();
		}
		
		//move the racers
		class RainTask implements Runnable{
			@Override
			public void run() {
				while( !Thread.currentThread().isInterrupted() ){
					for(int i = 0; i < racers.length; i++){
						RacingGamePlayer racer = racers[i];
						//drop.ripple();
						//if(!racer.isVisible()){
						//	racer.setPosition((int) (Math.random() * pnl.getWidth()), (int) (Math.random() * pnl.getHeight()));
						//}
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
	