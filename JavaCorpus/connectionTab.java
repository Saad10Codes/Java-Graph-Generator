package org.healthchecktool.ui.tabs;

import org.healthchecktool.http.invoker.jmxHttpInvokerImpl;
import org.healthchecktool.report.reporter;
import org.healthchecktool.ui.util.checkComboBox.checkCombo;
import org.healthchecktool.util.config.config;
import org.healthchecktool.util.credentials.credential;
import org.healthchecktool.util.files.filerw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class connectionTab extends JPanel  implements ActionListener {
	private		JTabbedPane 	jTabbedPane;
	private		config			conf;
	private 	credential 		cred				=	null;
	private		JLabel			protoLable			=	new JLabel("Proto:");
	private 	JTextField 		protocol 			= 	new JTextField(30);
	private		JLabel			hostLable			=	new JLabel("Host:");
	private 	JTextField 		host 				= 	new JTextField(30);
	private		JLabel			portLable			=	new JLabel("Port:");
	private 	JTextField 		port 				= 	new JTextField(30);
	private		JLabel			userLable			=	new JLabel("User:");
	private 	JTextField 		user 				= 	new JTextField(30);
	private		JLabel			passLable			=	new JLabel("Pass:");
	private 	JPasswordField 	pass 				= 	new JPasswordField(30);
	private 	JButton 		buttonAccept 		= 	new JButton("Connect");
	private 	JButton 		buttonClear 		= 	new JButton("Clear");
	private		JLabel			customerLable		=	new JLabel("Customer:");
	private 	JTextField 		customer 			= 	new JTextField(30);
	private 	JLabel			fileDropListLable 	= 	new JLabel("File:");
	private 	checkCombo		fileDropList;
	
	
	public connectionTab(JTabbedPane jTabbedPane,config	conf) {
		this.conf = conf;
		this.jTabbedPane = jTabbedPane;
		setLayout(new GridLayout(8,2,6,6));
		add(protoLable);
		add(protocol);
		add(hostLable);
	    add(host);
	    add(portLable);	    
	    add(port);
	    add(userLable);
	    add(user);
	    add(passLable);
	    add(pass);
	    add(customerLable);
	    add(customer);
	    fileDropList = new checkCombo(makeFileList());
	    add(fileDropListLable);
	    add(fileDropList.getContent());
	    add(buttonAccept);
	    add(buttonClear);   
	    buttonAccept.addActionListener(this);
	    buttonClear.addActionListener(this);
	    
	    protocol.setText("http");
        host.setText("ucmdb-cpe-svr1.vlab.lohika.com");
        port.setText("8080");
        user.setText("sysadmin");
        pass.setText("sysadmin");
        customer.setText("1");
	}
	private String[] makeFileList() {
		return filerw.fileList(conf.getValueByKey("scenarioPath"));
	}
	// Show text when user presses ENTER. 
    public void actionPerformed(ActionEvent ae) { 
    	if(ae.getSource().equals(buttonAccept)) {
    		if(protocol.getText().isEmpty() || host.getText().isEmpty() || port.getText().isEmpty() ||
   				user.getText().isEmpty() || pass.getPassword()==null) {
    			JOptionPane.showMessageDialog(new Frame(), "All fields should be filled");
    		}
    		else {
    			cred = new credential(protocol.getText(),host.getText(),
    					new Integer(port.getText()),user.getText(),new String(pass.getPassword()));
    			jmxHttpInvokerImpl jhi = new jmxHttpInvokerImpl(cred);
    			jhi.setParamCustomerId(customer.getText());
    			String fileNames[] =fileDropList.getSelectedItem();
    			Map<String,String> res = new HashMap<String, String>();
    			for(int i=0;i<fileNames.length;i++){
    				filerw file = new filerw(conf.getValueByKey("scenarioPath")+"/"+fileNames[i]);
    				String content=null;
    				try {
    					content = file.readFile();
       					jhi.setParamoperationString(content);
       					jhi.setParams();
       					jhi.invoke();
//    					System.out.println("jhi.getResult(): "+jhi.getResult());
    					res.put(fileNames[i], jhi.getResult());
    					
    				}
    				catch(Exception e) {
    					e.printStackTrace();
    				}
    			}
    			if(!res.isEmpty()) {
    				try {
    					reporter report = new reporter(conf,res);
    					report.createExcel();
    			
    				}
    				catch(Exception e) {
    					e.printStackTrace();
    				}
    			}
				//jhi.getResult() - get result operation as String
	    			//parseJmxOutput pjo = new parseJmxOutput(jhi.getResult());
	    			//List<String>list = pjo.getMatcher();
	    			//mf.getjTabbedPane().setComponentAt(0, new resultTab(jhi.getResult()));
	    			
	    		}   	
	    	}
	    	if(ae.getSource().equals(buttonClear)) {
	    		protocol.setText("");
	    		host.setText("");
	    		port.setText("");
	    		user.setText("");
	    		pass.setText("");
	    		customer.setText("");
	    	}   
	    }
	public credential getCred() {
		return cred;
	}
}