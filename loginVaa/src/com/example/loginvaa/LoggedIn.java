package com.example.loginvaa;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


public class LoggedIn extends CustomComponent implements View, Button.ClickListener {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private Label label_1;
	@AutoGenerated
	private Button logoutbutton;	//Referenz auf Navigator der UI anlegen

	
	//Referenz auf Navigator der UI anlegen
	Navigator navigator = LoginvaaUI.navigator;

	
	public LoggedIn() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// logoutbutton
		logoutbutton = new Button();
		logoutbutton.setCaption("Ich will wieder weg = logout");
		logoutbutton.setImmediate(true);
		logoutbutton.setWidth("-1px");
		logoutbutton.setHeight("-1px");

		//Falls loggout button gedrueckt wird, soll der User wieder zum Login-View
		logoutbutton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
	            getSession().setAttribute("user", null);
				navigator.addView("weg", new loginview());
				navigator.navigateTo("weg");
			}
		});
		mainLayout.addComponent(logoutbutton, "top:80.0px;left:20.0px;");
		
		// label_1
		label_1 = new Label();
		label_1.setImmediate(false);
		label_1.setWidth("-1px");
		label_1.setHeight("-1px");
		label_1.setValue("Hi, you are logged in! ");
		mainLayout.addComponent(label_1, "top:40.0px;left:38.0px;");
		
		return mainLayout;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
		String benutzer = String.valueOf(getSession().getAttribute("user"));
		
		Notification.show("Logge ein.... bitte warten " + benutzer);
        

	}

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub

	}

}
