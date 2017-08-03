package labinterface;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class SolutionButtonMenu extends JPanel {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(SolutionButtonMenu.class);

	private JPanel solutionPanel;
	
	
	public SolutionButtonMenu(JPanel solPanel, int champIndex) {
		
		setBorder(new LineBorder(SystemColor.textHighlight, 3, true));

		solutionPanel = solPanel;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		
			
		for (int i = 0; i<solutionPanel.getComponentCount();i++){
			
			JButton button = new JButton("Solution "+(i+1));
			
			button.setFocusPainted(false);
			
			button.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
//			button.setPreferredSize(new Dimension(100,5));
			
			int w = 100;
			int l = 30;
			button.setMinimumSize(new Dimension(w,l));
			button.setMaximumSize(new Dimension(w,l));
//			button.setOpaque(true);
//			button.setBorder(null);
			
			
			
			
			if (i==(champIndex-1)){
			
//				LOG.debug("i = "+i+" champ ="+champIndex);
//				button.setOpaque(true);
//				button.setBackground(SystemColor.desktop);
				button.setForeground(SystemColor.ORANGE);
				
		
			}
			
			add(button);
			
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					JPanel panel = (JPanel)button.getParent();
					
					int index = panel.getComponentZOrder(button);
					
					showSolution(index);
					
					
				}
				
			});
			
		}
		
		
		validate();
		
		setVisible(true);
		
		showSolution(0);
		
	}
		
		public void showSolution (int index){
			
			Component[] cmp = solutionPanel.getComponents();
			
			for (int i = 0; i<cmp.length;i++){
				(cmp[i]).setVisible(false);
				JButton button = (JButton)getComponent(i);
				button.setEnabled(true);
			}
			
			(cmp[index]).setVisible(true);
					
			((JButton)getComponent(index)).setEnabled(false);
			
			
		}

}
