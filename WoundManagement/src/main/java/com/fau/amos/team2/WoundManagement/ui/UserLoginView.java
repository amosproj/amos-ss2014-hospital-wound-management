package com.fau.amos.team2.WoundManagement.ui;


import java.util.List;

import com.fau.amos.team2.WoundManagement.WoundManagementUI;
import com.fau.amos.team2.WoundManagement.model.Employee;
import com.fau.amos.team2.WoundManagement.provider.EmployeeProvider;
import com.fau.amos.team2.WoundManagement.resources.MessageResources;
import com.fau.amos.team2.WoundManagement.ui.subviews.NumericButtonField;
import com.vaadin.addon.responsive.Responsive;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class UserLoginView extends SessionedNavigationView {
	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private Button loginButton;
	@AutoGenerated
	private PasswordField passwordField;
	@AutoGenerated
	private TextField usernameField;
	private NumericButtonField passwordInput;

	private static EmployeeProvider employeeProvider = 
			EmployeeProvider.getInstance();
	
	final VerticalLayout fieldsandbutton = new VerticalLayout();

	/**
	 * Creates an instance of LoginView in order to login.
	 * 
	 * If user login correct creates a new View
	 * @see com.fau.amos.team2.WoundManagement.LoggedInView
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UserLoginView() {
		setCaption(MessageResources.getString("login")); //$NON-NLS-1$
		setSizeFull();
		
		HorizontalSplitPanel split = new HorizontalSplitPanel();
		new Responsive(split);
		setContent(split);
		
		split.setSplitPosition(50, Unit.PERCENTAGE);
		split.setMinSplitPosition(100, Unit.PIXELS);
		split.setMaxSplitPosition(1200, Unit.PIXELS);
		
		VerticalLayout left = new VerticalLayout();
		left.setSpacing(true);
		left.setSizeFull();
		split.addComponent(left);
		
		final VerticalLayout right = new VerticalLayout();
		right.setSizeFull();	
		right.setVisible(false);
		split.addComponent(right);
		
		new Responsive(left);	
		new Responsive(split);
		new Responsive(right);
		
		usernameField = new TextField();
		usernameField.setValue(""); //$NON-NLS-1$
		usernameField.setCaption(MessageResources.getString("username") + ":"); //$NON-NLS-1$
		usernameField.setVisible(false);
		
		left.addComponent(usernameField);
		
		passwordField = new PasswordField();
		passwordField.setCaption(MessageResources.getString("PIN") + ":"); //$NON-NLS-1$
		passwordField.setValue(""); //$NON-NLS-1$
		passwordField.setWidth("100%");
		new Responsive(passwordField);
		
		right.addComponent(passwordField);
		
		passwordInput = new NumericButtonField(passwordField);
		passwordInput.addEnterListener(validateClickListener);
		passwordInput.setBackListener(backListener);
		passwordInput.setSizeFull();
		
		new Responsive(passwordInput);
		
		right.addComponent(passwordInput);
		
		Panel userList = new Panel();
		userList.setSizeFull();
		userList.setHeight("100%");
		left.addComponent(userList);
		
		List<Employee> employees = employeeProvider.getAllItems();

		final Table table = new Table();
		
		table.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		table.setSelectable(true);
		table.setImmediate(true);
		table.setNullSelectionAllowed(false);
		
		userList.setContent(table);
		table.setSizeFull();

		Property[][] properties = new Property[employees.size()][1];
		table.addContainerProperty("username", String.class, null, null, null, null);
		for (Employee employee : employees) {
			
			table.addItem(employee.getId());
			
			Item tableItem = table.getItem(employee.getId());
			
			properties[employees.indexOf(employee)][0] = tableItem.getItemProperty("username");
			properties[employees.indexOf(employee)][0].setValue(employee.getFirstName() + " " + employee.getLastName());
			
			table.getContainerDataSource().addItem(employee.getId());
		}
		
		table.sort(new Object[]{ "username" }, new boolean[]{ true });
		
		// *** end: styling left content *** 
		
				// **** **** ****
				// **** **** **** 
		
		// *** styling right content: start *** 


		
		/*// override the CSS-Layout-Style
		CssLayout pinAndNumberField = new CssLayout(){
		    @Override
		    protected String getCss(Component c) {
		        if (c instanceof Label) {
		            // center the passwordField
		            return "display: block; margin-left: auto; margin-right: auto;";
		        }
		        return null;
		    }
		};
		pinAndNumberField.setSizeFull(); // wrap passwordField*/
		
		//fieldsandbutton.addComponent(pinAndNumberField);

		table.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				
				Object id = table.getValue();
				
				if (id != null) {
					Employee employee = employeeProvider.getByID(id);
					
					String selectedloginname = employee.getAbbreviation();
					
					usernameField.setValue(selectedloginname);
					passwordField.focus();
					
					right.setVisible(true);
				}
			}
		});			
		
		setContent(split);

	}
	
	private Button.ClickListener backListener = new ClickListener() {

		@Override
		public void buttonClick(ClickEvent event) {
			usernameField.setValue("");
			fieldsandbutton.setVisible(false);
		}
		
	};
	
	private Button.ClickListener validateClickListener = new ClickListener() {
		
		@Override
		public void buttonClick(ClickEvent event) {
			validateData();
		}
	};
	
	/**
	 * Validate user input with data in the database
	 * @return true if user fits password in the database, false otherwise
	 */
	private void validateData() {
		String username = usernameField.getValue();
		String password = passwordField.getValue();
		
		if (UI.getCurrent() == null) {
			System.out.println("CurrentUI is null");
		}
		else if (((WoundManagementUI)UI.getCurrent()).getMySession() == null)
			System.out.println("myysession is null");
		else if (getEnvironment() == null)
			System.out.println("Environment is null, session id is " + ((WoundManagementUI)UI.getCurrent()).getMySession().getId());
		else {
			getEnvironment().loginEmployee(username, password);
			boolean correctdata = getEnvironment().getCurrentEmployee() != null;
			
			if (correctdata) {
				NavigationView next = new PatientSelectionView();
				getNavigationManager().navigateTo(next);
			} else {
				Notification.show(MessageResources.getString("incorrectData")); //$NON-NLS-1$
	
				this.passwordField.setValue(""); //$NON-NLS-1$
			}
		}
	}

}