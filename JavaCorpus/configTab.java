package org.healthchecktool.ui.tabs;

import org.healthchecktool.util.config.config;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;

public class configTab extends JPanel  implements ActionListener{
	private config conf;
	private JTabbedPane jTabbedPane;
	private JTable	jTable;
	
	
	public configTab(JTabbedPane jTabbedPane,config	conf) {
		this.conf = conf;
		this.jTabbedPane = jTabbedPane;
//		setLayout(new GridLayout(8,2,6,6));
		createTable();
		jTable.setSize(200, 200);
		add(this.jTable);
		
	}
	private void createTable() {
		Set keys = conf.getAllKeys();
		Object columnNames[] = {"Property: ","Value: "};
		String rowData[][] = new String[keys.size()][2];
		Iterator iterator = keys.iterator();
		for(int i=0;i<keys.size();i++) {
			rowData[i][0] = (String)iterator.next();
			rowData[i][1] = conf.getValueByKey(rowData[i][0]);
		}		
		this.jTable = new JTable(rowData, columnNames);
	}
	
	
	public void actionPerformed(ActionEvent ae) {
		
	} 

}
