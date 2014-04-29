package com.fau.amos.team2.WoundManagement;

import com.fau.amos.team2.WoundManagement.model.Employee;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class LoggedInView extends NavigationView {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private Label greetingLable;
	@AutoGenerated
	private Button logoutbutton;

	@SuppressWarnings("deprecation")
	public LoggedInView(Object id) {
		VerticalLayout mainLayout = new VerticalLayout();
		Employee user = EmployeeProvider.getInstance().getByID(id);
		
		greetingLable = new Label();
		greetingLable.setValue("Hi, you are logged in! " + user.getAbbreviation());
		mainLayout.addComponent(greetingLable);
		
		logoutbutton = new Button();
		logoutbutton.setCaption("Logout");
		logoutbutton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				Notification.show("Tschuess!");

				NavigationView next2 = new LoginView();
				getNavigationManager().navigateTo(next2);
			}
		});
		
		mainLayout.addComponent(logoutbutton);

		addComponent(mainLayout);
	}
}
