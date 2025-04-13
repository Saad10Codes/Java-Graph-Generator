package org.healthchecktool.ui.checkComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;

public class checkCombo implements ActionListener {
    
	private JPopupMenu popupMenu;
	private JLabel label;
	private checkComboStore stores[];
	private class checkComboStore {
        String text;
        boolean state;
     
        public checkComboStore(String text, boolean state) {
            this.text = text;
            this.state = state;
        }
     
        public String toString() {
            return text;
        }
    }
	
	public checkCombo(String list[]) {
		createCheckComboBox(list);
	}
	
	private void createCheckComboBox(String list[]) {
		stores = new checkComboStore[list.length];
		for(int i=0;i<list.length;i++) {
			stores[i] = new checkComboStore(list[i], false);
		}
	}
	
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();
        Component parent = button.getParent();
        if(popupMenu.getInvoker() == null) {
            popupMenu.setInvoker(parent);
            int w = ((JComponent)parent).getPreferredSize().width;
            Dimension d = popupMenu.getComponent(0).getPreferredSize();
            d.width = w;
            popupMenu.setPopupSize(d);
        }
        Rectangle r = parent.getBounds();
        Point p = r.getLocation();
        Container topLevel = button.getTopLevelAncestor();
        Container cp = ((JFrame)topLevel).getContentPane();
        SwingUtilities.convertPointToScreen(p, cp);
//        popupMenu.setLocation(p.x, p.y+r.height);
        Point point = label.getLocationOnScreen();
        
        popupMenu.setLocation(point.x, point.y+25);
        popupMenu.setVisible(true);
    }
 
    public Component getContent() {
        createPopupMenu();
        label = new JLabel("Select Files:");
        label.setBorder(BorderFactory.createLineBorder(new Color(175,175,200), 1));
        JButton button = new JButton() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                double width = 10.0;
                double x = (w - width)/2.0;
                double height = 5.0;
                double y = (getHeight() - height)/2.0;
                Path2D.Double path = new Path2D.Double();
                path.moveTo(x, y);
                path.lineTo(x+width, y);
                path.lineTo(w/2.0, y+height);
                path.closePath();
                g2.fill(path);
            }
 
            public Dimension getPreferredSize() {
                return new Dimension(20,25);
            }
        };
        button.addActionListener(this);
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = gbc.BOTH;
        p.add(label, gbc);
        gbc.weightx = 0;
        p.add(button, gbc);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(p, gbc);
        Dimension d = p.getPreferredSize();
        d.width = 120;
        p.setPreferredSize(d);
        return panel;
    }
 
    public String[] getSelectedItem() {
    	
    	String checked[] = new String[stores.length];
    	String temp[];
    	int j=0;
    	for(int i=0;i<checked.length;i++) {
    		if(stores[i].state==true) {
    			checked[j] = stores[i].text;
    			j++;
    		}
    	}
    	temp = new String[j];
    	for(int i=0;i<j;i++) {
    		temp[i] = checked[i];
    		
    	}
    	return temp;
    }
    
    private void createPopupMenu() {
    	ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox)e.getSource();
                String ac = cb.getActionCommand();
                for(int i = 0; i < stores.length; i++) {
                    if(stores[i].toString().equals(ac)) {
                        stores[i].state = cb.isSelected();
                    }
                }
                popupMenu.setVisible(true);
            }
        };
        popupMenu = new JPopupMenu();
        JPanel panel = new JPanel(new GridLayout(0,1));
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        for(int i = 0; i < stores.length; i++) {
            JCheckBox cb = new JCheckBox();
            cb.setActionCommand(stores[i].toString());
            cb.addActionListener(al);
            JLabel label = new JLabel(stores[i].toString(), JLabel.CENTER);
            JPanel p = new JPanel(gridbag);
            gbc.weightx = 0;
            gbc.gridwidth = GridBagConstraints.RELATIVE;
            p.add(cb, gbc);
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            p.add(label, gbc);
            panel.add(p);
        }
        popupMenu.add(panel);
    }
 
    private JPanel getComparisonCombo() {
        JComboBox combo = new JComboBox(new String[]{"Select Features!"});
        Dimension d = combo.getPreferredSize();
 //       System.out.printf("comparisonSize = [%d, %d]%n", d.width, d.height);
        JPanel panel = new JPanel();
        panel.add(combo);
        return panel;
    }
}