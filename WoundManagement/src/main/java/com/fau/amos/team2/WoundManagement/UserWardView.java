package com.fau.amos.team2.WoundManagement;

import java.util.ArrayList;
import java.util.List;

import com.fau.amos.team2.WoundManagement.model.Employee;
import com.fau.amos.team2.WoundManagement.model.Ward;
import com.fau.amos.team2.WoundManagement.provider.EmployeeProvider;
import com.fau.amos.team2.WoundManagement.provider.Environment;
import com.fau.amos.team2.WoundManagement.provider.WardProvider;
import com.fau.amos.team2.WoundManagement.resources.MessageResources;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class UserWardView extends Popover {
	
	private static WardProvider wardProvider = 
			WardProvider.getInstance();
	
	private static EmployeeProvider employeeProvider = 
			EmployeeProvider.getInstance();

	public UserWardView() {
		setClosable(true);
		setModal(true);
		
		//setWidth("450px");
		//setHeight("250px");

		setCaption(MessageResources.getString("changeWard"));
		final Employee user = Environment.INSTANCE.getCurrentEmployee();

		VerticalLayout layout = new VerticalLayout();
		
		final OptionGroup wardGroup = new OptionGroup();
		
		List<Ward> wards = wardProvider.getAllItems();
		for (Ward ward : wards) {
			wardGroup.addItem(ward);
			wardGroup.setItemCaption(ward, ward.getCharacterisation());
		}
		
		
		Button changeWardButton = new Button(MessageResources.getString("changeWard")); //$NON-NLS-1$
		changeWardButton.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				Ward newWard = (Ward) wardGroup.getValue();
				user.setCurrentWard(newWard);
				
				//TODO: why is update not working?
				employeeProvider.updateEmployeeWardManually(user);
				
				Notification.show(MessageResources.getString("currentWardChangedTo1")
						+  newWard.getCharacterisation()
						+ " " + MessageResources.getString("currentWardChangedTo2"));
				
				fireWardChangeEvent(newWard);
				
				UserWardView.this.close();
			}
			
		});
		
		layout.addComponent(wardGroup);
		layout.addComponent(changeWardButton);
		
		setContent(layout);
		
	}

		
	
	public List<WardChangeListener> wardChangeListeners = null;
	
	public interface WardChangeListener {
		public void wardChanged(WardChangeEvent event);
	}
    
    public class WardChangeEvent {
		final Ward ward;
		
		public WardChangeEvent(Ward ward) {
			this.ward = ward;
		}

		public Ward getWard(){
			return this.ward;
		}

	}
    
    public void fireWardChangeEvent(Ward ward) {
    	
		if (wardChangeListeners != null) {
			WardChangeEvent event = new WardChangeEvent(ward);
			for (WardChangeListener listener : wardChangeListeners)
				listener.wardChanged(event);
		}
	}
    
    public void addWardChangeListener(WardChangeListener listener) {
        if (wardChangeListeners == null) {
            wardChangeListeners = new ArrayList<WardChangeListener>();
        }
        wardChangeListeners.add(listener);
    }

    public void removeWardChangeListener(WardChangeListener listener) {
        if (wardChangeListeners != null) {
        	wardChangeListeners.remove(listener);
        }
    }

}
