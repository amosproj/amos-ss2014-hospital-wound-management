package com.fau.amos.team2.WoundManagement;

import java.util.List;

import com.fau.amos.team2.WoundManagement.UserWardView.WardChangeEvent;
import com.fau.amos.team2.WoundManagement.UserWardView.WardChangeListener;
import com.fau.amos.team2.WoundManagement.model.Patient;
import com.fau.amos.team2.WoundManagement.model.Ward;
import com.fau.amos.team2.WoundManagement.provider.Environment;
import com.fau.amos.team2.WoundManagement.provider.PatientProvider;
import com.fau.amos.team2.WoundManagement.resources.MessageResources;
import com.fau.amos.team2.WoundManagement.subviews.UserBar;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
//added import Ward
import com.vaadin.ui.Table.Align;


/**
 * View to see patients of selected ward
 * @author ???
 */
@SuppressWarnings("serial")
public class PatientSelectionView extends NavigationView implements WardChangeListener{
	
	private static PatientProvider patientProvider = PatientProvider.getInstance();
	
	private List<Patient> allPatients;
	private List<Patient> patientsOfWard;
	private List<Patient> patientsForTable;
	private Table table;
	private Container container;
	private Property[][] propertiesOfWard;
	private Property[][] allProperties;
	private Property[][] propertiesForTable;
	
	private OptionGroup optionGroup;
	
	public PatientSelectionView() 
	{
		setCaption(MessageResources.getString("patientSelection"));
		
		Ward currentWard = Environment.INSTANCE.getCurrentEmployee().getCurrentWard();
		
		setRightComponent(new UserBar(this));
		
		VerticalComponentGroup verticalGroup = new VerticalComponentGroup();
		optionGroup = new OptionGroup(MessageResources.getString("pleaseChoose") + ":"); //$NON-NLS-1$
		optionGroup.addItem("patientsOfWard");
		optionGroup.setItemCaption("patientsOfWard", MessageResources.getString("patientsOfWard"));
		optionGroup.addItem("allPatients");
		optionGroup.setItemCaption("allPatients", MessageResources.getString("allPatients"));
		optionGroup.setImmediate(true);
		optionGroup.setValue("patientsOfWard");
		optionGroup.setMultiSelect(false);
		optionGroup.setNullSelectionAllowed(false);
		
		Panel tablePanel = new Panel();
		tablePanel.setSizeUndefined();
		tablePanel.setImmediate(true);
		
		table = new Table();
		table.setSelectable(true);
		table.setImmediate(true);
		
		allPatients = patientProvider.getAllItems();
		allProperties = new Property[allPatients.size()][2];
		patientsOfWard = patientProvider.getPatientsOfWard(currentWard);
		propertiesOfWard = new Property[patientsOfWard.size()][2];
		
		patientsForTable = patientsOfWard;
		propertiesForTable = propertiesOfWard;
		
		table.addContainerProperty("name", String.class , null, MessageResources.getString("name"), null , null);
		table.addContainerProperty("currentWounds", Integer.class, 0, MessageResources.getString("currentWounds"), null, Align.RIGHT);
		
		table.setColumnWidth("name", 500);
		
		container = table.getContainerDataSource();
		
		for (Patient p : patientsForTable){
			table.addItem(p.getId());
			Item item = table.getItem(p.getId());
			propertiesForTable[patientsForTable.indexOf(p)][0] = item.getItemProperty("name");
			propertiesForTable[patientsForTable.indexOf(p)][0].setValue(p.getFirstName() + " " + p.getLastName());	
			propertiesForTable[patientsForTable.indexOf(p)][1] = item.getItemProperty("currentWounds");
			// changed to show only current wounds
			propertiesForTable[patientsForTable.indexOf(p)][1].setValue(p.getCurrentWounds().size());
			// propertiesForTable[patientsForTable.indexOf(p)][1].setValue(p.getWounds().size());
			container.addItem(p.getId());
		} 
		
		tablePanel.setContent(table);
		tablePanel.getContent().setSizeUndefined();
		
		table.addValueChangeListener(new Property.ValueChangeListener() {
		    public void valueChange(ValueChangeEvent event) {
		    	Object value = table.getValue();
		    	if (value != null){
		    		Patient patient = patientProvider.getByID(value);
		    		NavigationView next = new PatientView(patient);
		    		getNavigationManager().navigateTo(next);
		    	}
		    }

		});
		
		optionGroup.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (optionGroup.getValue().equals("allPatients")){
					patientsForTable = allPatients;
					propertiesForTable = allProperties;
				} else {
					patientsForTable = patientsOfWard;
					propertiesForTable = propertiesOfWard;
				}
				
				container.removeAllItems();
				
				for (Patient p : patientsForTable){
					table.addItem(p.getId());
					Item item = table.getItem(p.getId());
					propertiesForTable[patientsForTable.indexOf(p)][0] = item.getItemProperty("name");
					propertiesForTable[patientsForTable.indexOf(p)][0].setValue(p.getFirstName() + " " + p.getLastName());	
					propertiesForTable[patientsForTable.indexOf(p)][1] = item.getItemProperty("currentWounds");
					// changed to show only current wounds
					propertiesForTable[patientsForTable.indexOf(p)][1].setValue(p.getCurrentWounds().size());
					//propertiesForTable[patientsForTable.indexOf(p)][1].setValue(p.getWounds().size());
					container.addItem(p.getId());
				} 
			}
			
		});
		
		verticalGroup.addComponent(optionGroup);
		verticalGroup.addComponent(tablePanel);
		
		setContent(verticalGroup);
		
	}

	@Override
	public void wardChanged(WardChangeEvent event) {
		if (event.getWard() != null){
			patientsOfWard = patientProvider.getPatientsOfWard(event.getWard());
			propertiesOfWard = new Property[patientsOfWard.size()][2];
			
			if (optionGroup.getValue().equals("allPatients")){
				patientsForTable = allPatients;
				propertiesForTable = allProperties;
			} else {
				patientsForTable = patientsOfWard;
				propertiesForTable = propertiesOfWard;
			}
			
			container.removeAllItems();
			
			for (Patient p : patientsForTable){
				table.addItem(p.getId());
				Item item = table.getItem(p.getId());
				propertiesForTable[patientsForTable.indexOf(p)][0] = item.getItemProperty("name");
				propertiesForTable[patientsForTable.indexOf(p)][0].setValue(p.getFirstName() + " " + p.getLastName());	
				propertiesForTable[patientsForTable.indexOf(p)][1] = item.getItemProperty("currentWounds");
				// changed to get current wounds only
				propertiesForTable[patientsForTable.indexOf(p)][1].setValue(p.getCurrentWounds().size());
				//propertiesForTable[patientsForTable.indexOf(p)][1].setValue(p.getWounds().size());
				container.addItem(p.getId());
			} 
		}
	}

}

