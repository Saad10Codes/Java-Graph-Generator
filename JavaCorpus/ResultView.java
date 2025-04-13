package com.niftyside.icloud.calendars.app.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.niftyside.icloud.calendars.api.model.UserData;
import com.niftyside.icloud.calendars.app.gui.event.UserDataEvent;
import com.niftyside.icloud.calendars.app.gui.listener.UserDataListener;
import com.niftyside.icloud.calendars.app.gui.model.ResultTableModel;

/**
 * Displays all results regarding the submitted credentials.
 * 
 * @author Daniel Muehlbachler
 * @copyright 2012 Daniel Muehlbachler
 * 
 * @see http://icloud.niftyside.com
 * 
 * @version 1.2
 * 
 */
public class ResultView extends JComponent {
	/* * * * * Variables * * * * */

	private static final long serialVersionUID = 1L;
	/** The principal URL. */
	private final JLabel principalUrl;
	/** The contacts URL. */
	private final JLabel contactsUrl;
	/** The table. */
	private final JTable table;
	/** The table's column model. */
	private final TableColumnModel columnModel;
	/** The table model. */
	private final ResultTableModel model;

	/* * * * * Constructor * * * * */

	/**
	 * Creates a new result view pane.
	 * 
	 * @since 1.2
	 */
	public ResultView() {
		super();
		setLayout(new BorderLayout());

		principalUrl = new JLabel();
		contactsUrl = new JLabel();
		layoutUserInfo();

		table = new JTable();
		model = new ResultTableModel();
		table.setModel(model);
		columnModel = table.getTableHeader().getColumnModel();
		layoutTable();

		setData(null);

		setCopy();

		setSize(table.getPreferredSize());
		setPreferredSize(table.getPreferredSize());
	}

	/* * * * * Methods * * * * */

	/**
	 * Gets a new {@link UserDataListener}.
	 * 
	 * @return a new user data listener
	 * 
	 * @since 1.1
	 */
	public UserDataListener getUserDataListener() {
		return new UserDataListener() {
			@Override
			public void newUserData(final UserDataEvent event) {
				setData(event);
			}
		};
	}

	/* * * * * Private methods * * * * */

	/**
	 * Layouts the user info.
	 * 
	 * @since 1.2
	 */
	private void layoutUserInfo() {
		final JPanel userInfo = new JPanel();
		userInfo.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 25));

		final JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
		labelPanel.add(new JLabel("Principal-URL: "));
		labelPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		labelPanel.add(new JLabel("Contacts-URL: "));
		userInfo.add(labelPanel);

		final JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
		fieldPanel.add(principalUrl);
		fieldPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		fieldPanel.add(contactsUrl);
		userInfo.add(fieldPanel);

		add(userInfo, BorderLayout.NORTH);
	}

	/**
	 * Layouts the table.
	 * 
	 * @since 1.1
	 */
	private void layoutTable() {
		table.setPreferredSize(new Dimension(975, 225));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

		final JTableHeader header = table.getTableHeader();
		header.setBackground(Color.LIGHT_GRAY);

		final JScrollPane pane = new JScrollPane(table);
		add(pane, BorderLayout.CENTER);
	}

	/**
	 * Sets the copy handler for the table.
	 * 
	 * @since 1.1
	 */
	private void setCopy() {
		final KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK, false);
		table.registerKeyboardAction(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final int col = table.getSelectedColumn();
				final int row = table.getSelectedRow();

				if (col != -1 && row != -1) {
					final Object val = table.getValueAt(row, col);
					final String data = (val == null) ? "" : val.toString();
					final StringSelection selection = new StringSelection(data);
					Toolkit.getDefaultToolkit().getSystemClipboard()
							.setContents(selection, selection);
				}
			}
		}, "copy", key, JComponent.WHEN_FOCUSED);
	}

	/**
	 * Displays the fetched {@link UserData} or error message.
	 * 
	 * @param event
	 *            the event
	 * 
	 * @since 1.2
	 */
	private void setData(final UserDataEvent event) {
		makeSingleColumn();

		if (event == null) {
			model.setMessage("Please submit your credentials first!");
			principalUrl.setText("Please submit your credentials first!");
			contactsUrl.setText("Please submit your credentials first!");
		} else if (event.isValid()) {
			makeColumns();
			model.setUserData(event.getUserData());
			principalUrl.setText(event.getUserData().getPrincipalUrl());
			contactsUrl.setText(event.getUserData().getCardDavUrl());
		} else {
			model.setMessage(event.getException().getMessage());
			principalUrl.setText("There was an error!");
			contactsUrl.setText("There was an error!");
		}

		table.revalidate();
	}

	/**
	 * Deletes all columns.
	 * 
	 * @since 1.0
	 */
	private void deleteColumns() {
		for (int i = columnModel.getColumnCount() - 1; i >= 0; i--) {
			columnModel.removeColumn(columnModel.getColumn(i));
		}
	}

	/**
	 * Makes one single column.
	 * 
	 * @since 1.0
	 */
	private void makeSingleColumn() {
		deleteColumns();

		final TableColumn col0 = new TableColumn(0);
		col0.setHeaderValue("Information - Error");
		columnModel.addColumn(col0);
	}

	/**
	 * Makes three columns.
	 * 
	 * @since 1.0
	 */
	private void makeColumns() {
		deleteColumns();

		final TableColumn col0 = new TableColumn(0);
		col0.setHeaderValue("Calendar");
		columnModel.addColumn(col0);

		final TableColumn col1 = new TableColumn(1);
		col1.setHeaderValue("Calendar href");
		col1.setPreferredWidth(250);
		columnModel.addColumn(col1);

		final TableColumn col2 = new TableColumn(2);
		col2.setHeaderValue("Calendar URL");
		col2.setPreferredWidth(375);
		columnModel.addColumn(col2);
	}
}
