package com.diloso.bookhair.app.persist.manager;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.EventDAOGoogle;
import com.diloso.bookhair.app.negocio.dto.EventDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.negocio.utils.Utils;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;


@Component
@Scope(value = "singleton")
public class GEventManager extends Manager implements EventDAOGoogle {

	protected static final Logger log = Logger.getLogger(GEventManager.class.getName());
	
	protected static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	protected static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	protected static Map<Long,Calendar> calendarsPull;
	protected static String ACCOUNT_ID_OAUTH2 = "dilosohairapp@appspot.gserviceaccount.com";
	protected static String FILE_OAUTH2 = "dilosohairapp-d053b9335cfe.p12";
	public final static String GCALENDAR_HOLIDAYS = "#holiday@group.v.calendar.google.com";
	
	protected static final String STATUS_CONFIRMED = "confirmed";
	protected static final String VISIBILITY_DEFAULT = "default";
	
	public GEventManager() {
	}

	
	public Calendar getCalendarService(LocalDTO local) throws AuthenticationException {
		if (calendarsPull == null) {
			calendarsPull = new HashMap<Long,Calendar>();
		}	
		if (!calendarsPull.containsKey(local.getId())) {
			String userCalendar = null;
			if (local.getLocSinGCalendar()!=null){
				userCalendar = local.getLocSinGCalendar().getSinConEmail();
			} 
		    
			GoogleCredential credential = null;
			try {
				List<String> scopes = Arrays.asList(CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_READONLY);
				URL credentialsJSON = GEventManager.class.getClassLoader().getResource(FILE_OAUTH2);

				credential = new GoogleCredential.Builder()
						    .setTransport(HTTP_TRANSPORT)
						    .setJsonFactory(JSON_FACTORY)
						    .setServiceAccountId(ACCOUNT_ID_OAUTH2)
						    .setServiceAccountPrivateKeyFromP12File(new File(credentialsJSON.getFile()))
						    .setServiceAccountScopes(scopes)
						    .setServiceAccountUser(userCalendar)
						    .build();
				
				Calendar calendarServiceInstance = new Calendar.Builder(HTTP_TRANSPORT,
			            JSON_FACTORY,credential).build();
				
				log.info("calendarServiceInstance creada para : "+ credential.getServiceAccountId());
					
				calendarsPull.put(local.getId(), calendarServiceInstance);
				
			} catch (Exception e) {
				return null;
			} 
		}	
		return calendarsPull.get(local.getId());

	}
		
	public List<Event> getEvent(LocalDTO local, DateTime startTime,
			DateTime endTime) throws Exception {

		String calGoogleId = local.getLocWhere().getWheGoogleHoliday()+GCALENDAR_HOLIDAYS;
		String pageToken = null;
		List<Event> items = new ArrayList<Event>();
		List<Event> itemsAux = null;
		do {

			Calendar.Events.List list = getCalendarService(local).events().list(
					calGoogleId);
			list.setTimeMin(startTime).setTimeMax(endTime);

			Events events = list.execute();

			itemsAux = events.getItems();
			items.addAll(itemsAux);
			
			pageToken = events.getNextPageToken();
		} while (pageToken != null);

		return items;
	}
	
	public String insertEvent(LocalDTO local, EventDTO event) throws Exception {
		
		// Calculamos el desplazamiento de la zona horaria desde UTC, pero al revés
		TimeZone calendarTimeZone = TimeZone.getTimeZone(local.getLocWhere().getWheTimeZone());
		Date eveStartTimeGC =  new Date(event.getEveStartTime().getTime() - calendarTimeZone.getOffset(event.getEveStartTime().getTime()));
		Date eveEndTimeGC =  new Date(event.getEveEndTime().getTime() - calendarTimeZone.getOffset(event.getEveEndTime().getTime()));
		
		String name = event.getEveClient().getWhoName();
		if (event.getEveClient().getWhoSurname()!=null){
			name += " "+event.getEveClient().getWhoSurname();
		}
		String  telf = "";
		if (event.getEveClient().getWhoTelf1()!=null){
			telf = event.getEveClient().getWhoTelf1();
		} else if (event.getEveClient().getWhoTelf2()!=null){
			telf = event.getEveClient().getWhoTelf2();
		}
		String nameTask = event.getEveLocalTask().getLotName();
		
		Event content = new Event();
		
		content.setStart(new EventDateTime().setDateTime(new DateTime(eveStartTimeGC)));
		content.setEnd(new EventDateTime().setDateTime(new DateTime(eveEndTimeGC)));
		String summary = nameTask+", "+name+", "+telf;
		// Configuracion de campos extras de evento
		if (event.getEveCelebrationTime()!=null){
			summary += ", "+Utils.getFormatTextNoHour(event.getEveCelebrationTime(),event.getEveLocale(), calendarTimeZone);
		}
		content.setSummary(summary);
		if (event.getEveDesc()!=null && event.getEveDesc().length()>0){
			summary += ", "+event.getEveDesc();
		}
		content.setDescription(summary);
		content.setLocation(local.getLocLocation());
		
//		Organizer organizer = new Organizer();
//		organizer.setEmail(local.getLocSinGCalendar().getSinConEmail());
//		content.setOrganizer(organizer);
//
//		EventAttendee attendee = new EventAttendee();
//		attendee.setEmail(event.getEveClient().getWhoEmail());
//		attendee.setDisplayName(event.getEveClient().getWhoEmail()+" "+name);
//		List<EventAttendee> attendees = new ArrayList<EventAttendee>();
//		attendees.add(attendee);
//		content.setAttendees(attendees);

		content.setGuestsCanInviteOthers(true);
		content.setGuestsCanModify(false);
		content.setGuestsCanSeeOtherGuests(true);
		content.setAnyoneCanAddSelf(false);
		content.setStatus(STATUS_CONFIRMED);
		content.setVisibility(VISIBILITY_DEFAULT);
		
		/*Colors colors = getCalendarService(local).colors().get().execute();
		String colorId = "";
		for (Map.Entry<String, ColorDefinition> color : colors.getEvent().entrySet()) {
			  System.out.println("ColorId : " + color.getKey());
			  System.out.println("  Background: " + color.getValue().getBackground());
			  System.out.println("  Foreground: " + color.getValue().getForeground());
			  colorId = color.getKey();
			}
	    
		1 - a4bdfc azul
		2 - 7ae7bf verde cyan
		3 - dbadff violeta
		4 - ff887c rojo rosa
		5 - fbd75b amarillo
		6 - ffb878 naranja claro
		7 - 46d6db azul claro
		8 - e1e1e1 fris claro
		9 - 5484ed azul oscuro
		10 - 51b749 verde
		11 - dc2127 rojo*/	
		
		if (event.getEveLocalTask().getLotVisible()==1){
			content.setColorId("9");
		} else{
			content.setColorId("4");
		}
		
		Event insert = getCalendarService(local).events().insert("primary", content).execute();
		
		return insert.getId(); 
	}
	
	
	public void removeEvent(LocalDTO local, EventDTO event) throws Exception {
		
		getCalendarService(local).events().delete("primary", event.getEveIDGCalendar()).execute();
	}
	
}