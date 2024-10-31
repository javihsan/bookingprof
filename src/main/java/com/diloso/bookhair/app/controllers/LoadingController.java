package com.diloso.bookhair.app.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.ClientDTO;
import com.diloso.bookhair.app.negocio.dto.DiaryDTO;
import com.diloso.bookhair.app.negocio.dto.EventDTO;
import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.dto.LangDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatDTO;
import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.negocio.dto.SincroDTO;
import com.diloso.bookhair.app.negocio.dto.TaskClassDTO;
import com.diloso.bookhair.app.negocio.dto.TaskDTO;
import com.diloso.bookhair.app.negocio.dto.WhereDTO;
import com.diloso.bookhair.app.negocio.manager.ICalendarManager;
import com.diloso.bookhair.app.negocio.manager.IClientManager;
import com.diloso.bookhair.app.negocio.manager.IDiaryManager;
import com.diloso.bookhair.app.negocio.manager.IEventManager;
import com.diloso.bookhair.app.negocio.manager.IFirmManager;
import com.diloso.bookhair.app.negocio.manager.ILangManager;
import com.diloso.bookhair.app.negocio.manager.ILocalManager;
import com.diloso.bookhair.app.negocio.manager.ILocalTaskManager;
import com.diloso.bookhair.app.negocio.manager.IMultiTextManager;
import com.diloso.bookhair.app.negocio.manager.IProfessionalManager;
import com.diloso.bookhair.app.negocio.manager.ISemanalDiaryManager;
import com.diloso.bookhair.app.negocio.manager.ISincroManager;
import com.diloso.bookhair.app.negocio.manager.ITaskClassManager;
import com.diloso.bookhair.app.negocio.manager.ITaskManager;
import com.diloso.bookhair.app.negocio.manager.IWhereManager;
import com.diloso.bookhair.app.negocio.manager.LocalTaskManager;
import com.diloso.bookhair.app.negocio.manager.MultiTextManager;
import com.diloso.bookhair.app.negocio.manager.TaskClassManager;
import com.diloso.bookhair.app.negocio.manager.TaskManager;
import com.diloso.bookhair.app.negocio.utils.ApplicationContextProvider;
import com.diloso.bookhair.app.negocio.utils.ExtendMessageSource;
import com.diloso.bookhair.app.negocio.utils.templates.Generator;
import com.diloso.weblogin.aut.AppRole;
import com.diloso.weblogin.aut.AppUser;
import com.diloso.weblogin.aut.DatastoreUserRegistry;
import com.diloso.weblogin.aut.UserRegistry;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/loading")
public class LoadingController {

	protected static final Logger log = Logger
			.getLogger(LoadingController.class.getName());

	@Autowired
	protected MessageSource messageSourceApp;

	@Autowired
	protected Generator generatorVelocity;

	@Autowired
	protected ILocalManager localManager;

	@Autowired
	protected ICalendarManager calendarManager;

	@Autowired
	protected IFirmManager firmManager;

	@Autowired
	protected IMultiTextManager multiTextManager;

	@Autowired
	protected IProfessionalManager professionalManager;

	@Autowired
	protected ILangManager langManager;

	@Autowired
	protected IWhereManager whereManager;

	@Autowired
	protected ITaskManager taskManager;

	@Autowired
	protected ITaskClassManager taskClassManager;

	@Autowired
	protected IDiaryManager diaryManager;

	@Autowired
	protected ISemanalDiaryManager semanalDiaryManager;

	@Autowired
	protected ILocalTaskManager localTaskManager;

	@Autowired
	protected ISincroManager sincroManager;

	@Autowired
	protected IEventManager eventManager;

	@Autowired
	protected IClientManager clientManager;

	protected UserRegistry userRegistry = new DatastoreUserRegistry();

	@RequestMapping(method = RequestMethod.GET, value = "/admin/langSystem_no")
	@ResponseStatus(HttpStatus.OK)
	protected void langSystem(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// List<MultiTextDTO> listMultiDefault =
		// multiTextDAO.getMultiTextSystemByLanCode("eu");
		// for (MultiTextDTO multiTextDefault : listMultiDefault) {
		// multiTextDAO.remove(multiTextDefault.getId());
		// }

		// Lang

		LangDTO lang = new LangDTO();
		lang.setEnabled(1);
		lang.setLanCode("es");
		lang.setLanName("Español");
		lang = langManager.create(lang);
		
		lang = new LangDTO();
		lang.setEnabled(1);
		lang.setLanCode("en");
		lang.setLanName("English");
		lang = langManager.create(lang);
		
		lang = new LangDTO();
		lang.setEnabled(1);
		lang.setLanCode("pt");
		lang.setLanName("Portugues");
		lang = langManager.create(lang);
		
		lang = new LangDTO();
		lang.setEnabled(1);
		lang.setLanCode("fr");
		lang.setLanName("Français");
		lang = langManager.create(lang);

		lang = new LangDTO();
		lang.setEnabled(1);
		lang.setLanCode("eu");
		lang.setLanName("Euskera");
		lang = langManager.create(lang);

		// lang = new LangDTO();
		// lang.setEnabled(1);
		// lang.setLanCode("de"); // ca
		// lang.setLanName("Deutsch"); Català
		// lang = langDAO.create(lang);

		// TaskClass

		MultiTextDTO nameMulti = null;

		String nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskClassManager.KEY_MULTI_TASKCLASS_NAME + "hairdresser";
		// TaskClassDTO taskClassHair = taskClassDAO.getByName(nameKey);
		TaskClassDTO taskClassHair = new TaskClassDTO();
		taskClassHair.setEnabled(1);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Peluquería");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Hairdresser");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Cabeleireiro");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coiffure");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Ileapaindegia");
		multiTextManager.create(nameMulti);

		taskClassHair.setTclNameMulti(nameKey);
		taskClassHair = taskClassManager.create(taskClassHair);

		
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskClassManager.KEY_MULTI_TASKCLASS_NAME + "beauty";
		// TaskClassDTO taskClassBeauty = taskClassDAO.getByName(nameKey);
		TaskClassDTO taskClassBeauty = new TaskClassDTO();
		taskClassBeauty.setEnabled(1);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Belleza");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Beauty");
		multiTextManager.create(nameMulti);
	
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Beleza");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Beauté");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Edertasuna");
		multiTextManager.create(nameMulti);

		taskClassBeauty.setTclNameMulti(nameKey);

		taskClassBeauty = taskClassManager.create(taskClassBeauty);

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskClassManager.KEY_MULTI_TASKCLASS_NAME + "celebration";
		//TaskClassDTO taskClassCelebration = taskClassDAO.getByName(nameKey);
		TaskClassDTO taskClassCelebration = new TaskClassDTO();
		taskClassCelebration.setEnabled(1);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Celebración");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Celebration");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Celebrações");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Célébrations");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Ospakizunak");
		multiTextManager.create(nameMulti);

		taskClassCelebration.setTclNameMulti(nameKey);

		taskClassCelebration = taskClassManager.create(taskClassCelebration);

		// Task

		// Corte de pelo caballero
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME
				+ "hairdresser_haircut_gentleman";

		TaskDTO task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Corte de pelo caballero");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Haircut gentleman");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corte de cabelo cabellero");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coupe de cheveux homme");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gizonentzat ile mozketa");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Peinado caballero
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME
				+ "hairdresser_hairstyle_gentleman";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Peinado caballero");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Hairstyle gentleman");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Penteado cabellero");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coiffure homme");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gizonentzat orrazkera");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Tinte caballero
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_dye_gentleman";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Tinte caballero");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dye gentleman");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corante cabellero");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Colorant homme");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gizonentzat tindatzea");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Mechas caballero
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME
				+ "hairdresser_wicks_gentleman";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Mechas caballero");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Wicks gentleman");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Mechas cabellero");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Wicks homme");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gizonentzat metxak");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Barba
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_beard";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Barba");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Beard");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Barba");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Barbe");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Bizarra");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Corte de pelo señora
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_haircut_lady";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Corte de pelo señora");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Haircut lady");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corte de cabelo senhora");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coupe de cheveux femmes");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Emakumeen ile mozketa");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Peinado señora
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME
				+ "hairdresser_hairstyle_lady";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Peinado señora");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Hairstyle lady");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Penteado senhora");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coiffure femmes");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Emakumeen orrazkera");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Tinte señora
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_dye_lady";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Tinte señora");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dye lady");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corante senhora");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Colorant femmes");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Emakumeen tindatzea");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Mechas señora
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_wicks_lady";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Mechas señora");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Wicks lady");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Mechas senhora");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Wicks femmes");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Emakumeen metxak");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Decapar pelo
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_scrape";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Decapar pelo");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Scrape");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Raspar o cabelo");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Grattez les cheveux");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Ilea Desugertu");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Extensiones
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_extensions";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Extensiones");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Extensions");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Extensões");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Extensions");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Luzapenak");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Lavar pelo
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_wash";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Lavar pelo");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Wash hair");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Lavar o cabelo");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Laver les cheveux");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Ilea garbitu");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Corte de pelo niño
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_haircut_baby";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Corte de pelo niño");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Haircut baby");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corte de cabelo criança");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coupe de cheveux enfant");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Umearen ile mozketa");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Peinado niño
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME
				+ "hairdresser_hairstyle_baby";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassHair);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Peinado niño");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Hairstyle baby");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Penteado criança");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coiffure enfant");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Umearen orrazkera");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Manicura
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_manicure";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassBeauty);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Manicura");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Manicure");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Manicure");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Manucure");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Manikura");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Pedicura
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_pedicure";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassBeauty);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Pedicura");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Pedicure");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Pedicure");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Pédicure");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Pedikuro");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Depilación señora
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_depilation_lady";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassBeauty);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Depilación señora");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Depilation lady");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Depilação senhora");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Epilation femmes");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Emakumeen depilazioa");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Depilación caballero
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME
				+ "beauty_depilation_gentleman";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassBeauty);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Depilación caballero");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Depilation gentleman");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Depilação cabellero");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Epilation homme");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gizonentzat depilazioa");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Cutis
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_skin";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassBeauty);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Cutis");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Skin");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Pele");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Complexion");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Aurpegiko larruazala");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Cejas o labio
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_eyebrows_lip";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassBeauty);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Cejas o labio");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Eyebrows or lip");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Sobrancelhas ou lábio");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Sourcils ou lèvre");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Bekain edo ezpain");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Maquillaje
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_makeup";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassBeauty);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Maquillaje");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Makeup");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Maquiagem");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Maquillage");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Makillajea");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Ingles o axilas
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_crotches_armpits";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassBeauty);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Ingles o axilas");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Crotches or armpits");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Virilha ou axilas");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Anglais ou aisselles");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Izterondoak edo besapeak");
		multiTextManager.create(nameMulti);
		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Pestañas
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_eyelashes";
		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassBeauty);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Pestañas");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Eyelashes");
		multiTextManager.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Pestanas");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Cils");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Betileal");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskManager.create(task);

		// Novia
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "celebration_bride";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCelebration);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Novia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Bride");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Noiva");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Jeune mariée");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Emaztegaia");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Novio
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "celebration_bridegroom";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCelebration);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Novio");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Bridegroom");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Noivo");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Jeune marié");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Senargaia");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Madrina
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "celebration_godmother";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCelebration);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Madrina");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Godmother");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Madrinha");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Marraine");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Amabitxia");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Padrino
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "celebration_godfather";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCelebration);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Padrino");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Godfather");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Padrinho");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Parrain");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Aitabitxia");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Invitada
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "celebration_invited_lady";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCelebration);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Invitada");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Invited lady");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Convidado senhora");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Invité femmes");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gonbidatua emakumea");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Invitado
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME
				+ "celebration_invited_gentleman";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCelebration);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Invitado");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Invited gentleman");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Convidado cabellero");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Invité homme");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gonbidatua gizona");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Comunión
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "celebration_communion";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCelebration);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Comunión");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Communion");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Comunhão");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Communion");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Jaunartzea");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Accesorios
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "celebration_accessories";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCelebration);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Accesorios");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Accessories");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Acessórios");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Accessoires");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Osagarriak");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/loadDemo_no")
	@ResponseStatus(HttpStatus.OK)
	protected void loadDemo(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// Firm

		String firName = "Demo Beauty";// arg0.getParameter("firName");
		String firDomain = "demo";// arg0.getParameter("firDomain");

		String firResponName = "Javier";// arg0.getParameter("firResponName");
		String firResponSurname = "Sánchez";// arg0.getParameter("firResponSurname");
		String firResponEmail = "javihsan@gmail.com";// arg0.getParameter("firResponEmail");
		String firResponTelf1 = "687123456";// arg0.getParameter("firResponTelf1");

		String firAddress = "Calle Real, 4";// arg0.getParameter("firAddress");
		String firCity = "Collado Villalba";// arg0.getParameter("firCity");
		String firState = "Madrid";// arg0.getParameter("firState");
		String firCP = "28400";// arg0.getParameter("firCP");
		String firCountry = "España";// arg0.getParameter("firCountry");

		FirmDTO firm = new FirmDTO();

		ProfessionalDTO respon = new ProfessionalDTO();

		respon.setEnabled(1);
		respon.setWhoName(firResponName);
		respon.setWhoSurname(firResponSurname);
		respon.setWhoEmail(firResponEmail);
		respon.setWhoTelf1(firResponTelf1);

		respon = professionalManager.create(respon);
		firm.setFirRespon(respon);

		WhereDTO where = new WhereDTO();

		where.setEnabled(1);
		where.setWheAddress(firAddress);
		where.setWheCity(firCity);
		where.setWheState(firState);
		where.setWheCP(firCP);
		where.setWheCountry(firCountry);

		where = whereManager.create(where);
		firm.setFirWhere(where);

		List<String> firGwtUsers = new ArrayList<String>();

		String firEmail = "info@bookingprof.com";
		firGwtUsers.add(firEmail);
	
		firEmail = "info@perepeluqueros.com";
		firGwtUsers.add(firEmail);
	
		firEmail = "savetheclock@gmail.com";
		firGwtUsers.add(firEmail);
		

		firEmail = "jose.m.delorenzo@gmail.com";
		firGwtUsers.add(firEmail);
		
		firEmail = "bertadelorenzo@gmail.com";
		firGwtUsers.add(firEmail);
	
		firEmail = "jorgezazo@gmail.com";
		firGwtUsers.add(firEmail);
	
		firm.setFirGwtUsers(firGwtUsers);

		List<TaskClassDTO> firClassTasks = taskClassManager.getTaskClass();
		List<Long> firClassTasksId = new ArrayList<Long>();
		for (TaskClassDTO taskClass : firClassTasks) {
			firClassTasksId.add(taskClass.getId());
		}

		firm.setFirClassTasks(firClassTasksId);

		firm.setEnabled(1);
		firm.setFirName(firName);
		firm.setFirDomain(firDomain);

		firm = firmManager.create(firm);

		where.setResFirId(firm.getId());
		whereManager.update(where);

		respon.setResFirId(firm.getId());
		professionalManager.update(respon);

		Set<AppRole> roles = EnumSet.noneOf(AppRole.class);
		roles.add(AppRole.MANAGER);
		roles.add(AppRole.OPERATOR);
		firEmail = "info@bookingprof.com";
		AppUser user = new AppUser(firEmail, roles, true, firm.getId());
		userRegistry.registerUser(user);

		firEmail = "info@perepeluqueros.com";
		user = new AppUser(firEmail, roles, true, firm.getId());
		userRegistry.registerUser(user);

		firEmail = "savetheclock@gmail.com";
		user = new AppUser(firEmail, roles, true, firm.getId());
		userRegistry.registerUser(user);

		firEmail = "jose.m.delorenzo@gmail.com";
		user = new AppUser(firEmail, roles, true, firm.getId());
		userRegistry.registerUser(user);

		firEmail = "bertadelorenzo@gmail.com";
		user = new AppUser(firEmail, roles, true, firm.getId());
		userRegistry.registerUser(user);

		firEmail = "jorgezazo@gmail.com";
		user = new AppUser(firEmail, roles, true, firm.getId());
		userRegistry.registerUser(user);

		
		// Local

		String locName = "Caballeros";// arg0.getParameter("locName");

		String locAddress = "Playa frexeira 4 local 5";// arg0.getParameter("locAddress");
		String locCity = "Collado Villalba";// arg0.getParameter("locCity");
		String locState = "Madrid";// arg0.getParameter("locState");
		String locCP = "28400";// arg0.getParameter("locCP");
		String locCountry = "España";// arg0.getParameter("locCountry");
		String locTimeZone = "Europe/Madrid"/* America/New_York */;// arg0.getParameter("locTimeZone");
		String locGoogleHoliday = "es.spain";// arg0.getParameter("locGoogleHoliday");
		String locCurrency = "€";// arg0.getParameter("locCurrency");

		int locTimeRestricted = Integer.parseInt("30"/*
													 * arg0.getParameter(
													 * "locTimeRestricted")
													 */);
		int locApoDuration = Integer.parseInt("15"/*
												 * arg0.getParameter(
												 * "locApoDuration")
												 */);
		int locOpenDays = Integer.parseInt("21"/*
												 * arg0.getParameter("locOpenDays"
												 * )
												 */);
		int locNumPersonsApo = Integer.parseInt("3"/*
													 * arg0.getParameter(
													 * "locNumPersonsApo")
													 */);
		int locNumUsuDays = Integer.parseInt("7"/*
												 * arg0.getParameter("locNumUsuDays"
												 * )
												 */);

		LocalDTO local = new LocalDTO();

		where = new WhereDTO();

		where.setEnabled(1);
		where.setResFirId(firm.getId());
		where.setWheAddress(locAddress);
		where.setWheCity(locCity);
		where.setWheState(locState);
		where.setWheCP(locCP);
		where.setWheCountry(locCountry);
		where.setWheTimeZone(locTimeZone);
		where.setWheGoogleHoliday(locGoogleHoliday);
		where.setWheCurrency(locCurrency);

		where = whereManager.create(where);

		local.setLocWhere(where);

		DiaryDTO diary = new DiaryDTO();
		diary.setEnabled(1);
		List<String> diaTimes = new ArrayList<String>();

		String strTime = "10:00";
		diaTimes.add(strTime);
		strTime = "13:45";
		diaTimes.add(strTime);

		strTime = "16:00";
		diaTimes.add(strTime);
		strTime = "20:15";
		diaTimes.add(strTime);

		diary.setDiaTimes(diaTimes);

		DiaryDTO diaryCreatedMon = diaryManager.create(diary);
		DiaryDTO diaryCreatedTue = diaryManager.create(diary);
		DiaryDTO diaryCreatedWed = diaryManager.create(diary);
		DiaryDTO diaryCreatedThu = diaryManager.create(diary);
		DiaryDTO diaryCreatedFri = diaryManager.create(diary);

		SemanalDiaryDTO semanalDiary = new SemanalDiaryDTO();
		semanalDiary.setEnabled(1);
		semanalDiary.setSemMonDiary(diaryCreatedMon);
		semanalDiary.setSemTueDiary(diaryCreatedTue);
		semanalDiary.setSemWedDiary(diaryCreatedWed);
		semanalDiary.setSemThuDiary(diaryCreatedThu);
		semanalDiary.setSemFriDiary(diaryCreatedFri);

		diaTimes = new ArrayList<String>();

		strTime = "10:00";
		diaTimes.add(strTime);
		strTime = "13:45";
		diaTimes.add(strTime);

		diary.setDiaTimes(diaTimes);

		DiaryDTO diaryCreatedSat = diaryManager.create(diary);
		semanalDiary.setSemSatDiary(diaryCreatedSat);

		diaTimes = new ArrayList<String>();
		diary.setDiaTimes(diaTimes);
		DiaryDTO diaryCreatedSun = diaryManager.create(diary);
		semanalDiary.setSemSunDiary(diaryCreatedSun);

		semanalDiary = semanalDiaryManager.create(semanalDiary);

		local.setLocSemanalDiary(semanalDiary);

		List<LangDTO> locLangs = new ArrayList<LangDTO>();
		locLangs.add(langManager.getByCode("es"));
		locLangs.add(langManager.getByCode("en"));
		locLangs.add(langManager.getByCode("pt"));
		locLangs.add(langManager.getByCode("fr"));
		locLangs.add(langManager.getByCode("eu"));

		local.setEnabled(1);
		local.setLocBookingClient(1);
		local.setLocMailBookign(0);
		local.setLocNewClientDefault(1);
		local.setId(new Long("5709643623956480"));
		local.setResFirId(firm.getId());
		local.setLocName(locName);
		local.setLocApoDuration(locApoDuration);
		local.setLocTimeRestricted(locTimeRestricted);
		local.setLocOpenDays(locOpenDays);
		local.setLocNumPersonsApo(locNumPersonsApo);
		local.setLocNumUsuDays(locNumUsuDays);
		local.setLocLangs(locLangs);
		local.setLocMulServices(0);
		local.setLocSelCalendar(1);
		local.setLocCacheTasks(0);

		respon = new ProfessionalDTO();

		respon.setEnabled(1);
		respon.setWhoName(firResponName);
		respon.setWhoSurname(firResponSurname);
		respon.setWhoEmail(firResponEmail);
		respon.setWhoTelf1(firResponTelf1);

		respon = professionalManager.create(respon);
		local.setLocRespon(respon);
		
		local = localManager.create(local);

		// Calendar
		String calName = "Puesto1 Caballeros";// arg0.getParameter("calName");
		String calDesc = "Puesto1 para Caballeros";// arg0.getParameter("calDesc");
		/*
		 * //String calProfEmail = arg0.getParameter("calProf");
		 * 
		 * ProfessionalDTO calProf = new ProfessionalDTO();
		 * calProf.setEnabled(1); calProf.setResFirId(firm.getId());
		 * //calProf.setWhoName(calProfName); calProf.setWhoEmail(calProfEmail);
		 * calProf = professionalDAO.create(calProf);
		 */

		diary = new DiaryDTO();
		diary.setEnabled(1);
		diaTimes = new ArrayList<String>();
		strTime = "10:00";
		diaTimes.add(strTime);
		strTime = "13:45";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		strTime = "16:00";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		strTime = "20:35";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);

		diaryCreatedMon = diaryManager.create(diary);
		diaryCreatedTue = diaryManager.create(diary);
		diaryCreatedWed = diaryManager.create(diary);
		diaryCreatedThu = diaryManager.create(diary);
		diaryCreatedFri = diaryManager.create(diary);

		semanalDiary = new SemanalDiaryDTO();
		semanalDiary.setEnabled(1);
		semanalDiary.setSemMonDiary(diaryCreatedMon);
		semanalDiary.setSemTueDiary(diaryCreatedTue);
		semanalDiary.setSemWedDiary(diaryCreatedWed);
		semanalDiary.setSemThuDiary(diaryCreatedThu);
		semanalDiary.setSemFriDiary(diaryCreatedFri);

		diaTimes = new ArrayList<String>();
		strTime = "10:00";
		diaTimes.add(strTime);
		strTime = "13:45";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		diaryCreatedSat = diaryManager.create(diary);
		semanalDiary.setSemSatDiary(diaryCreatedSat);

		diaTimes = new ArrayList<String>();
		diary.setDiaTimes(diaTimes);
		diaryCreatedSun = diaryManager.create(diary);
		semanalDiary.setSemSunDiary(diaryCreatedSun);

		semanalDiary = semanalDiaryManager.create(semanalDiary);

		CalendarDTO calendar = new CalendarDTO();
		calendar.setEnabled(1);
		calendar.setResFirId(firm.getId());
		calendar.setCalName(calName);
		calendar.setCalDesc(calDesc);
		calendar.setCalLocalId(local.getId());
		calendar.setCalLocalTasksId(new ArrayList<Long>());
		calendar.setCalSemanalDiary(semanalDiary);

		calendarManager.create(calendar);

		calName = "Puesto2 Caballeros";// arg0.getParameter("calName");
		calDesc = "Puesto2 para Caballeros";// arg0.getParameter("calDesc");

		/*
		 * //String calProfEmail = arg0.getParameter("calProf");
		 * 
		 * ProfessionalDTO calProf = new ProfessionalDTO();
		 * calProf.setEnabled(1); calProf.setResFirId(firm.getId());
		 * //calProf.setWhoName(calProfName); calProf.setWhoEmail(calProfEmail);
		 * calProf = professionalDAO.create(calProf);
		 */

		calendar = new CalendarDTO();
		calendar.setEnabled(1);
		calendar.setResFirId(firm.getId());
		calendar.setCalName(calName);
		calendar.setCalDesc(calDesc);
		calendar.setCalLocalId(local.getId());
		calendar.setCalLocalTasksId(new ArrayList<Long>());

		diary = new DiaryDTO();
		diary.setEnabled(1);
		diaTimes = new ArrayList<String>();
		strTime = "10:00";
		diaTimes.add(strTime);
		strTime = "13:45";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		strTime = "16:00";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		strTime = "20:35";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);

		diaryCreatedMon = diaryManager.create(diary);
		diaryCreatedTue = diaryManager.create(diary);
		diaryCreatedWed = diaryManager.create(diary);
		diaryCreatedThu = diaryManager.create(diary);
		diaryCreatedFri = diaryManager.create(diary);

		semanalDiary = new SemanalDiaryDTO();
		semanalDiary.setEnabled(1);
		semanalDiary.setSemMonDiary(diaryCreatedMon);
		semanalDiary.setSemTueDiary(diaryCreatedTue);
		semanalDiary.setSemWedDiary(diaryCreatedWed);
		semanalDiary.setSemThuDiary(diaryCreatedThu);
		semanalDiary.setSemFriDiary(diaryCreatedFri);

		diaTimes = new ArrayList<String>();
		strTime = "10:00";
		diaTimes.add(strTime);
		strTime = "13:45";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		diaryCreatedSat = diaryManager.create(diary);
		semanalDiary.setSemSatDiary(diaryCreatedSat);

		diaTimes = new ArrayList<String>();
		diary.setDiaTimes(diaTimes);
		diaryCreatedSun = diaryManager.create(diary);
		semanalDiary.setSemSunDiary(diaryCreatedSun);

		semanalDiary = semanalDiaryManager.create(semanalDiary);

		calendar.setCalSemanalDiary(semanalDiary);

		calendarManager.create(calendar);

		// Local2

		locName = "Señoras";// arg0.getParameter("locName");

		locAddress = "Avenida de Atenas";// arg0.getParameter("locAddress");
		locCity = "Las Rozas";// arg0.getParameter("locCity");
		locCP = "28232";// arg0.getParameter("locCP");

		local = new LocalDTO();

		where = new WhereDTO();

		where.setEnabled(1);
		where.setResFirId(firm.getId());
		where.setWheAddress(locAddress);
		where.setWheCity(locCity);
		where.setWheState(locState);
		where.setWheCP(locCP);
		where.setWheCountry(locCountry);
		where.setWheTimeZone(locTimeZone);
		where.setWheGoogleHoliday(locGoogleHoliday);
		where.setWheCurrency(locCurrency);

		where = whereManager.create(where);

		local.setLocWhere(where);

		diary = new DiaryDTO();
		diary.setEnabled(1);
		diaTimes = new ArrayList<String>();
		strTime = "10:00";
		diaTimes.add(strTime);
		strTime = "13:45";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		strTime = "16:00";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		strTime = "20:35";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);

		diaryCreatedMon = diaryManager.create(diary);
		diaryCreatedTue = diaryManager.create(diary);
		diaryCreatedWed = diaryManager.create(diary);
		diaryCreatedThu = diaryManager.create(diary);
		diaryCreatedFri = diaryManager.create(diary);

		semanalDiary = new SemanalDiaryDTO();
		semanalDiary.setEnabled(1);
		semanalDiary.setSemMonDiary(diaryCreatedMon);
		semanalDiary.setSemTueDiary(diaryCreatedTue);
		semanalDiary.setSemWedDiary(diaryCreatedWed);
		semanalDiary.setSemThuDiary(diaryCreatedThu);
		semanalDiary.setSemFriDiary(diaryCreatedFri);

		diaTimes = new ArrayList<String>();
		strTime = "10:00";
		diaTimes.add(strTime);
		strTime = "13:45";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		diaryCreatedSat = diaryManager.create(diary);
		semanalDiary.setSemSatDiary(diaryCreatedSat);

		diaTimes = new ArrayList<String>();
		diary.setDiaTimes(diaTimes);
		diaryCreatedSun = diaryManager.create(diary);
		semanalDiary.setSemSunDiary(diaryCreatedSun);

		semanalDiary = semanalDiaryManager.create(semanalDiary);

		local.setLocSemanalDiary(semanalDiary);

		local.setEnabled(1);
		local.setLocBookingClient(1);
		local.setLocMailBookign(0);
		local.setLocNewClientDefault(1);
		local.setId(new Long("6642544880386048"));
		local.setResFirId(firm.getId());
		local.setLocName(locName);
		local.setLocApoDuration(locApoDuration);
		local.setLocTimeRestricted(locTimeRestricted);
		local.setLocOpenDays(locOpenDays);
		local.setLocNumPersonsApo(locNumPersonsApo);
		local.setLocNumUsuDays(locNumUsuDays);
		local.setLocLangs(locLangs);
		local.setLocMulServices(1);
		local.setLocSelCalendar(0);
		local.setLocCacheTasks(1);

		respon = new ProfessionalDTO();

		respon.setEnabled(1);
		respon.setWhoName(firResponName);
		respon.setWhoSurname(firResponSurname);
		respon.setWhoEmail(firResponEmail);
		respon.setWhoTelf1(firResponTelf1);

		respon = professionalManager.create(respon);
		local.setLocRespon(respon);
		
		local = localManager.create(local);

		// Calendar
		calName = "Puesto1 Señoras";// arg0.getParameter("calName");
		calDesc = "Puesto1 para Señoras";// arg0.getParameter("calDesc");
		/*
		 * //String calProfEmail = arg0.getParameter("calProf");
		 * 
		 * ProfessionalDTO calProf = new ProfessionalDTO();
		 * calProf.setEnabled(1); calProf.setResFirId(firm.getId());
		 * //calProf.setWhoName(calProfName); calProf.setWhoEmail(calProfEmail);
		 * calProf = professionalDAO.create(calProf);
		 */

		diary = new DiaryDTO();
		diary.setEnabled(1);
		diaTimes = new ArrayList<String>();
		strTime = "10:00";
		diaTimes.add(strTime);
		strTime = "13:45";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		strTime = "16:00";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		strTime = "20:35";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);

		diaryCreatedMon = diaryManager.create(diary);
		diaryCreatedTue = diaryManager.create(diary);
		diaryCreatedWed = diaryManager.create(diary);
		diaryCreatedThu = diaryManager.create(diary);
		diaryCreatedFri = diaryManager.create(diary);

		semanalDiary = new SemanalDiaryDTO();
		semanalDiary.setEnabled(1);
		semanalDiary.setSemMonDiary(diaryCreatedMon);
		semanalDiary.setSemTueDiary(diaryCreatedTue);
		semanalDiary.setSemWedDiary(diaryCreatedWed);
		semanalDiary.setSemThuDiary(diaryCreatedThu);
		semanalDiary.setSemFriDiary(diaryCreatedFri);

		diaTimes = new ArrayList<String>();
		strTime = "10:00";
		diaTimes.add(strTime);
		strTime = "13:45";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		diaryCreatedSat = diaryManager.create(diary);
		semanalDiary.setSemSatDiary(diaryCreatedSat);

		diaTimes = new ArrayList<String>();
		diary.setDiaTimes(diaTimes);
		diaryCreatedSun = diaryManager.create(diary);
		semanalDiary.setSemSunDiary(diaryCreatedSun);

		semanalDiary = semanalDiaryManager.create(semanalDiary);

		calendar.setEnabled(1);
		calendar.setResFirId(firm.getId());
		calendar.setCalName(calName);
		calendar.setCalDesc(calDesc);
		calendar.setCalLocalId(local.getId());
		calendar.setCalLocalTasksId(new ArrayList<Long>());

		calendar.setCalSemanalDiary(semanalDiary);

		calendarManager.create(calendar);

		calName = "Puesto2 Señoras";// arg0.getParameter("calName");
		calDesc = "Puesto2 para Señoras";// arg0.getParameter("calDesc");

		/*
		 * //String calProfEmail = arg0.getParameter("calProf");
		 * 
		 * ProfessionalDTO calProf = new ProfessionalDTO();
		 * calProf.setEnabled(1); calProf.setResFirId(firm.getId());
		 * //calProf.setWhoName(calProfName); calProf.setWhoEmail(calProfEmail);
		 * calProf = professionalDAO.create(calProf);
		 */

		calendar = new CalendarDTO();
		calendar.setEnabled(1);
		calendar.setResFirId(firm.getId());
		calendar.setCalName(calName);
		calendar.setCalDesc(calDesc);
		calendar.setCalLocalId(local.getId());
		calendar.setCalLocalTasksId(new ArrayList<Long>());

		diary = new DiaryDTO();
		diary.setEnabled(1);
		diaTimes = new ArrayList<String>();
		strTime = "10:00";
		diaTimes.add(strTime);
		strTime = "13:45";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		strTime = "16:00";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		strTime = "20:35";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);

		diaryCreatedMon = diaryManager.create(diary);
		diaryCreatedTue = diaryManager.create(diary);
		diaryCreatedWed = diaryManager.create(diary);
		diaryCreatedThu = diaryManager.create(diary);
		diaryCreatedFri = diaryManager.create(diary);

		semanalDiary = new SemanalDiaryDTO();
		semanalDiary.setEnabled(1);
		semanalDiary.setSemMonDiary(diaryCreatedMon);
		semanalDiary.setSemTueDiary(diaryCreatedTue);
		semanalDiary.setSemWedDiary(diaryCreatedWed);
		semanalDiary.setSemThuDiary(diaryCreatedThu);
		semanalDiary.setSemFriDiary(diaryCreatedFri);

		diaTimes = new ArrayList<String>();
		strTime = "10:00";
		diaTimes.add(strTime);
		strTime = "13:45";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		diaryCreatedSat = diaryManager.create(diary);
		semanalDiary.setSemSatDiary(diaryCreatedSat);

		diaTimes = new ArrayList<String>();
		diary.setDiaTimes(diaTimes);
		diaryCreatedSun = diaryManager.create(diary);
		semanalDiary.setSemSunDiary(diaryCreatedSun);

		semanalDiary = semanalDiaryManager.create(semanalDiary);

		calendar.setCalSemanalDiary(semanalDiary);

		calendarManager.create(calendar);

	}
	
	protected void newLocalTask(Long localId, Long lotTaskId,
			String strLotTaskDuration, String strLotTaskPost, 
			String strLotTaskRate, boolean setLocalDefault, String keyNameMulti,
			Map<String, String> hashNamesParam) throws Exception {

		LocalTaskDTO localTask = new LocalTaskDTO();

		LocalDTO local = localManager.getById(localId);

		Integer lotTaskDuration = new Integer(strLotTaskDuration);
		Integer lotTaskPost = new Integer(strLotTaskPost);
		strLotTaskRate = strLotTaskRate.replace(",", ".");
		Float lotTaskRate = new Float(strLotTaskRate);

		String nameValue = "";

		MultiTextDTO nameMulti = null;
		for (String codeLangMap : hashNamesParam.keySet()) {
			nameValue = hashNamesParam.get(codeLangMap);
			nameMulti = new MultiTextDTO();
			nameMulti.setEnabled(1);
			nameMulti.setMulKey(keyNameMulti);
			nameMulti.setMulLanCode(codeLangMap);
			nameMulti.setMulText(nameValue);
			multiTextManager.create(nameMulti);
		}

		localTask.setEnabled(1);
		localTask.setLotLocalId(localId);
		localTask.setLotNameMulti(keyNameMulti);
		localTask.setLotTaskId(lotTaskId);
		localTask.setLotTaskDuration(lotTaskDuration);
		localTask.setLotTaskPost(lotTaskPost);
		localTask.setLotTaskRate(lotTaskRate);
		localTask.setLotVisible(1);
		
		localTask = localTaskManager.create(localTask);

		// localTask.setEnabled(1);
		// localTask.setLotLocalId(localId);
		List<Long> lotTaskCombiId = new ArrayList<Long>();
		List<Integer> lotTaskCombiRes = new ArrayList<Integer>();
		localTask.setLotTaskCombiId(lotTaskCombiId);
		localTask.setLotTaskCombiRes(lotTaskCombiRes);
		localTask = localTaskManager.create(localTask);

		if (setLocalDefault) {
			localTask.setLotDefault(1);
			local.setLocTaskDefaultId(localTask.getId());
			localManager.update(local);
		}

	}

	protected void newLocalTaskMulti(Long localId, List<Long> lotTaskCombiId,
			List<Integer> lotTaskCombiRes, boolean setLocalDefault)
			throws Exception {

		LocalTaskDTO localTask = new LocalTaskDTO();

		LocalDTO local = localManager.getById(localId);

		localTask.setEnabled(1);
		localTask.setLotLocalId(localId);
		localTask.setLotTaskCombiId(lotTaskCombiId);
		localTask.setLotTaskCombiRes(lotTaskCombiRes);
		localTask.setLotVisible(1);
		localTask = localTaskManager.create(localTask);

		if (setLocalDefault) {
			localTask.setLotDefault(1);
			local.setLocTaskDefaultId(localTask.getId());
			localManager.update(local);
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/initloadLocalTask_no")
	@ResponseStatus(HttpStatus.OK)
	protected void initloadLocalTask(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		Long localId = new Long(arg0.getParameter("localId"));

		String keyNameMultiBase = LocalTaskManager.KEY_MULTI_LOCAL_TASK_NAME
				+ localId + "_";

		// Peluqueria

		// Corte de pelo caballero
		String taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME
				+ "hairdresser_haircut_gentleman";
		Long lotTaskId = (taskManager.getByName(taskMulti)).getId();

		Map<String, String> hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Corte pelo caballero");
		hashNamesParam.put("en", "Haircut gentleman");
		String keyNameMulti = keyNameMultiBase
				+ "hairdresser_haircut_gentleman";
		newLocalTask(localId, lotTaskId, "15", "0", "10", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Corte pelo caballero + lavado");
		hashNamesParam.put("en", "Haircut gentleman + washing");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_haircut_gentleman_washing";
		newLocalTask(localId, lotTaskId, "0", "0", "11.5", false, keyNameMulti,
				hashNamesParam);

		// Corte de pelo señora
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_haircut_lady";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Corte de pelo señora");
		hashNamesParam.put("en", "Haircut lady");
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady";
		newLocalTask(localId, lotTaskId, "15", "0", "12", true, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Corte + forma");
		hashNamesParam.put("en", "Haircut lady + shape");
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady_shape";
		newLocalTask(localId, lotTaskId, "0", "0", "17", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Corte flequillo");
		hashNamesParam.put("en", "Haircut fringe");
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady_fringe";
		newLocalTask(localId, lotTaskId, "0", "0", "3.7", false, keyNameMulti,
				hashNamesParam);

		// Corte de pelo niño
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_haircut_baby";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Corte de pelo niño (hasta 8 años)");
		hashNamesParam.put("en", "Haircut child (up to 8 years)");
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_child";
		newLocalTask(localId, lotTaskId, "15", "0", "8.7", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Corte de pelo bebé");
		hashNamesParam.put("en", "Haircut baby");
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_baby";
		newLocalTask(localId, lotTaskId, "0", "0", "5", false, keyNameMulti,
				hashNamesParam);

		// Peinado señora
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME
				+ "hairdresser_hairstyle_lady";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Peinado señora");
		hashNamesParam.put("en", "Hairstyle lady");
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady";
		newLocalTask(localId, lotTaskId, "30", "0", "0", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Peinado pelo 3/4");
		hashNamesParam.put("en", "Hairstyle 3/4 hair");
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady_34";
		newLocalTask(localId, lotTaskId, "0", "0", "12", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Peinado pelo corto");
		hashNamesParam.put("en", "Hairstyle short hair");
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady_short";
		newLocalTask(localId, lotTaskId, "0", "0", "9", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Peinado pelo medio");
		hashNamesParam.put("en", "Hairstyle medium hair");
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady_medium";
		newLocalTask(localId, lotTaskId, "0", "0", "10", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Peinado pelo largo");
		hashNamesParam.put("en", "Hairstyle long hair");
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady_long";
		newLocalTask(localId, lotTaskId, "0", "0", "14.1", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Peinado plancha pelo 3/4");
		hashNamesParam.put("en", "Hairstyle iron 3/4 hair");
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady_iron_34";
		newLocalTask(localId, lotTaskId, "0", "0", "14.3", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Peinado plancha pelo medio");
		hashNamesParam.put("en", "Hairstyle iron medium hair");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_hairstyle_lady_iron_medium";
		newLocalTask(localId, lotTaskId, "0", "0", "12.2", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Peinado plancha pelo largo");
		hashNamesParam.put("en", "Hairstyle iron long hair");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_hairstyle_lady_iron_long";
		newLocalTask(localId, lotTaskId, "0", "0", "16.3", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Peinado rulos pelo corto");
		hashNamesParam.put("en", "Hairstyle curlers short hair");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_hairstyle_lady_curlers_short";
		newLocalTask(localId, lotTaskId, "0", "0", "9", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Peinado rulos pelo medio");
		hashNamesParam.put("en", "Hairstyle curlers medium hair");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_hairstyle_lady_curlers_medium";
		newLocalTask(localId, lotTaskId, "0", "0", "10", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Peinado rulos pelo largo");
		hashNamesParam.put("en", "Hairstyle curlers long hair");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_hairstyle_lady_curlers_long";
		newLocalTask(localId, lotTaskId, "0", "0", "13.8", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Recogido");
		hashNamesParam.put("en", "Hair collected");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_hairstyle_lady_collected";
		newLocalTask(localId, lotTaskId, "0", "0", "25", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Recogido con marcado");
		hashNamesParam.put("en", "Hair collected with marked");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_hairstyle_lady_collected_marked";
		newLocalTask(localId, lotTaskId, "0", "0", "30", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Recogido novia");
		hashNamesParam.put("en", "Hair collected bride");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_hairstyle_lady_collected_bride";
		newLocalTask(localId, lotTaskId, "0", "0", "40", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Moldeado");
		hashNamesParam.put("en", "Molded");
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady_molded";
		newLocalTask(localId, lotTaskId, "30", "0", "0", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Moldeado a medias");
		hashNamesParam.put("en", "Molded half");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_hairstyle_lady_molded_half";
		newLocalTask(localId, lotTaskId, "0", "0", "18", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Moldeado pelo corto");
		hashNamesParam.put("en", "Hair molded short");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_hairstyle_lady_molded_short";
		newLocalTask(localId, lotTaskId, "0", "0", "25", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Moldeado pelo medio");
		hashNamesParam.put("en", "Hair molded medium");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_hairstyle_lady_molded_medium";
		newLocalTask(localId, lotTaskId, "0", "0", "28.5", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Moldeado pelo largo");
		hashNamesParam.put("en", "Hair molded long");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_hairstyle_lady_molded_long";
		newLocalTask(localId, lotTaskId, "0", "0", "35.5", false, keyNameMulti,
				hashNamesParam);

		// Peinado niño
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME
				+ "hairdresser_hairstyle_baby";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Peinado comunión");
		hashNamesParam.put("en", "Hairstyle communion");
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_baby";
		newLocalTask(localId, lotTaskId, "25", "0", "18", false, keyNameMulti,
				hashNamesParam);

		// Tinte señora
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_dye_lady";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Tinte señora");
		hashNamesParam.put("en", "Hair dye lady");
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady";
		newLocalTask(localId, lotTaskId, "25", "20", "0", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Tinte pelo 3/4");
		hashNamesParam.put("en", "Hair dye lady 3/4");
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady_34";
		newLocalTask(localId, lotTaskId, "0", "0", "19", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Tinte pelo 3/4 sin amoniaco");
		hashNamesParam.put("en", "Hair dye lady 3/4 without ammonia");
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady_34_sin";
		newLocalTask(localId, lotTaskId, "0", "0", "32", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Tinte pelo corto");
		hashNamesParam.put("en", "Hair dye lady short");
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady_short";
		newLocalTask(localId, lotTaskId, "0", "0", "16.5", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Tinte pelo corto sin amoniaco");
		hashNamesParam.put("en", "Hair dye lady short without ammonia");
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady_short_sin";
		newLocalTask(localId, lotTaskId, "0", "0", "26", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Tinte pelo medio");
		hashNamesParam.put("en", "Hair dye lady medium");
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady_medium";
		newLocalTask(localId, lotTaskId, "0", "0", "17.5", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Tinte pelo medio sin amoniaco");
		hashNamesParam.put("en", "Hair dye lady medium without ammonia");
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady_medium_sin";
		newLocalTask(localId, lotTaskId, "0", "0", "29", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Tinte pelo largo");
		hashNamesParam.put("en", "Hair dye lady long");
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady_long";
		newLocalTask(localId, lotTaskId, "0", "0", "21", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Retoque de tinte");
		hashNamesParam.put("en", "Hair dye retouch");
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady_retouch";
		newLocalTask(localId, lotTaskId, "0", "0", "12", false, keyNameMulti,
				hashNamesParam);

		// Mechas señora
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_wicks_lady";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Mechas señora");
		hashNamesParam.put("en", "Hair wicks lady");
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_lady";
		newLocalTask(localId, lotTaskId, "40", "40", "0", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Mechas plata 3/4");
		hashNamesParam.put("en", "Hair wicks silver 3/4");
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_lady_silver_34";
		newLocalTask(localId, lotTaskId, "0", "0", "32", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Mechas plata corto");
		hashNamesParam.put("en", "Hair wicks silver short");
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_lady_silver_short";
		newLocalTask(localId, lotTaskId, "0", "0", "26", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Mechas plata medio");
		hashNamesParam.put("en", "Hair wicks silver medium");
		keyNameMulti = keyNameMultiBase
				+ "hairdresser_wicks_lady_silver_medium";
		newLocalTask(localId, lotTaskId, "0", "0", "29", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Mechas plata largo");
		hashNamesParam.put("en", "Hair wicks silver large");
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_lady_silver_large";
		newLocalTask(localId, lotTaskId, "0", "0", "35.5", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Mechas por delante");
		hashNamesParam.put("en", "Hair wicks before");
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_lady_before";
		newLocalTask(localId, lotTaskId, "0", "0", "14", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Mechones por todo");
		hashNamesParam.put("en", "Hair wisp all");
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_lady_wisp_all";
		newLocalTask(localId, lotTaskId, "0", "0", "25.5", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Retoque de mechas");
		hashNamesParam.put("en", "Hair wisp retouch");
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_lady_wisp_retouch";
		newLocalTask(localId, lotTaskId, "0", "0", "22", false, keyNameMulti,
				hashNamesParam);

		// Decapar pelo
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_scrape";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Decapar pelo");
		hashNamesParam.put("en", "Scrape");
		keyNameMulti = keyNameMultiBase + "hairdresser_scrape";
		newLocalTask(localId, lotTaskId, "60", "0", "0", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Decapar pelo medio");
		hashNamesParam.put("en", "Scrape medium hair");
		keyNameMulti = keyNameMultiBase + "hairdresser_scrape_medium";
		newLocalTask(localId, lotTaskId, "0", "0", "30", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Decapar pelo largo");
		hashNamesParam.put("en", "Scrape long hair");
		keyNameMulti = keyNameMultiBase + "hairdresser_scrape_long";
		newLocalTask(localId, lotTaskId, "0", "0", "60", false, keyNameMulti,
				hashNamesParam);

		// Extensiones
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_extensions";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Extensiones");
		hashNamesParam.put("en", "Extensions");
		keyNameMulti = keyNameMultiBase + "hairdresser_extensions";
		newLocalTask(localId, lotTaskId, "5", "0", "3", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Extensiones lisas");
		hashNamesParam.put("en", "Extensions smooth");
		keyNameMulti = keyNameMultiBase + "hairdresser_extensions_smooth";
		newLocalTask(localId, lotTaskId, "0", "0", "2", false, keyNameMulti,
				hashNamesParam);

		// Lavar pelo
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_wash";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Lavar pelo");
		hashNamesParam.put("en", "Wash hair");
		keyNameMulti = keyNameMultiBase + "hairdresser_wash";
		newLocalTask(localId, lotTaskId, "10", "0", "6", false, keyNameMulti,
				hashNamesParam);

		// Manicura
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_manicure";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Manicura");
		hashNamesParam.put("en", "Manicure");
		keyNameMulti = keyNameMultiBase + "beauty_manicure";
		newLocalTask(localId, lotTaskId, "40", "0", "9", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Manicura + hidratación");
		hashNamesParam.put("en", "Manicure + hydration");
		keyNameMulti = keyNameMultiBase + "beauty_manicure_hydration";
		newLocalTask(localId, lotTaskId, "0", "0", "12", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Manicura francesa");
		hashNamesParam.put("en", "Manicure french");
		keyNameMulti = keyNameMultiBase + "beauty_manicure_french";
		newLocalTask(localId, lotTaskId, "0", "0", "14", false, keyNameMulti,
				hashNamesParam);

		// Pedicura
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_pedicure";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Pedicura");
		hashNamesParam.put("en", "Pedicure");
		keyNameMulti = keyNameMultiBase + "beauty_pedicure";
		newLocalTask(localId, lotTaskId, "60", "0", "15", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Pedicura completa");
		hashNamesParam.put("en", "Pedicure full");
		keyNameMulti = keyNameMultiBase + "beauty_pedicure_full";
		newLocalTask(localId, lotTaskId, "0", "0", "28.5", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Pedicura con maquillaje permanente");
		hashNamesParam.put("en", "Pedicure with permanent makeup");
		keyNameMulti = keyNameMultiBase + "beauty_pedicure_makeup";
		newLocalTask(localId, lotTaskId, "0", "0", "33", false, keyNameMulti,
				hashNamesParam);

		// Depilación señora
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_depilation_lady";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Depilación señora");
		hashNamesParam.put("en", "Depilation lady");
		keyNameMulti = keyNameMultiBase + "beauty_depilation_lady";
		newLocalTask(localId, lotTaskId, "20", "0", "0", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Depilación señora brazos");
		hashNamesParam.put("en", "Depilation lady arms");
		keyNameMulti = keyNameMultiBase + "beauty_depilation_lady_arms";
		newLocalTask(localId, lotTaskId, "0", "0", "12", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Depilación señora gluteos");
		hashNamesParam.put("en", "Depilation lady buttocks");
		keyNameMulti = keyNameMultiBase + "beauty_depilation_lady_buttocks";
		newLocalTask(localId, lotTaskId, "0", "0", "6", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Depilación señora piernas enteras");
		hashNamesParam.put("en", "Depilation lady legs whole");
		keyNameMulti = keyNameMultiBase + "beauty_depilation_lady_legs_whole";
		newLocalTask(localId, lotTaskId, "0", "0", "17.9", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Depilación señora piernas medias");
		hashNamesParam.put("en", "Depilation lady half legs");
		keyNameMulti = keyNameMultiBase + "beauty_depilation_lady_legs_half";
		newLocalTask(localId, lotTaskId, "0", "0", "17.9", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Depilación señora muslos");
		hashNamesParam.put("en", "Depilation lady thighs");
		keyNameMulti = keyNameMultiBase + "beauty_depilation_lady_thighs";
		newLocalTask(localId, lotTaskId, "0", "0", "8.6", false, keyNameMulti,
				hashNamesParam);

		// Depilación caballero
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME
				+ "beauty_depilation_gentleman";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Depilación caballero");
		hashNamesParam.put("en", "Depilation gentleman");
		keyNameMulti = keyNameMultiBase + "beauty_depilation_gentleman";
		newLocalTask(localId, lotTaskId, "25", "0", "0", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Depilación caballero brazos");
		hashNamesParam.put("en", "Depilation gentleman arms");
		keyNameMulti = keyNameMultiBase + "beauty_depilation_gentleman_arms";
		newLocalTask(localId, lotTaskId, "0", "0", "13", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Depilación caballero espalda");
		hashNamesParam.put("en", "Depilation gentleman back");
		keyNameMulti = keyNameMultiBase + "beauty_depilation_gentleman_back";
		newLocalTask(localId, lotTaskId, "0", "0", "14", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Depilación caballero piernas enteras");
		hashNamesParam.put("en", "Depilation gentleman legs whole");
		keyNameMulti = keyNameMultiBase
				+ "beauty_depilation_gentleman_legs_whole";
		newLocalTask(localId, lotTaskId, "0", "0", "20", false, keyNameMulti,
				hashNamesParam);

		// Cutis
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_skin";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Cutis limpieza");
		hashNamesParam.put("en", "Skin cleaning");
		keyNameMulti = keyNameMultiBase + "beauty_skin";
		newLocalTask(localId, lotTaskId, "90", "0", "31.5", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Cutis limpieza + hidratación");
		hashNamesParam.put("en", "Skin cleaning + hydration");
		keyNameMulti = keyNameMultiBase + "beauty_skin_hydration";
		newLocalTask(localId, lotTaskId, "0", "0", "36", false, keyNameMulti,
				hashNamesParam);

		// Cejas o labio
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_eyebrows_lip";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Cejas");
		hashNamesParam.put("en", "Eyebrows");
		keyNameMulti = keyNameMultiBase + "beauty_eyebrows";
		newLocalTask(localId, lotTaskId, "15", "0", "4.5", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Cejas con forma");
		hashNamesParam.put("en", "Eyebrow with shape");
		keyNameMulti = keyNameMultiBase + "beauty_eyebrows_shape_lady";
		newLocalTask(localId, lotTaskId, "0", "0", "6", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Cejas con forma caballero");
		hashNamesParam.put("en", "Eyebrow with shape gentleman");
		keyNameMulti = keyNameMultiBase + "beauty_eyebrows_shape_gentleman";
		newLocalTask(localId, lotTaskId, "0", "0", "10", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Labio");
		hashNamesParam.put("en", "Lip");
		keyNameMulti = keyNameMultiBase + "beauty_lip";
		newLocalTask(localId, lotTaskId, "15", "0", "3.5", false, keyNameMulti,
				hashNamesParam);

		// Maquillaje
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_makeup";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Maquillaje");
		hashNamesParam.put("en", "Makeup");
		keyNameMulti = keyNameMultiBase + "beauty_makeup";
		newLocalTask(localId, lotTaskId, "60", "0", "32.5", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Maquillaje de novia");
		hashNamesParam.put("en", "Makeup Bridal");
		keyNameMulti = keyNameMultiBase + "beauty_makeup_bridal";
		newLocalTask(localId, lotTaskId, "0", "0", "60", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Maquillaje prueba de novia");
		hashNamesParam.put("en", "Makeup Bridal Test");
		keyNameMulti = keyNameMultiBase + "beauty_makeup_bridal_test";
		newLocalTask(localId, lotTaskId, "0", "0", "40", false, keyNameMulti,
				hashNamesParam);

		// Ingles o axilas
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_crotches_armpits";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Ingles");
		hashNamesParam.put("en", "Crotches");
		keyNameMulti = keyNameMultiBase + "beauty_crotches";
		newLocalTask(localId, lotTaskId, "15", "0", "7", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Ingles + pubis");
		hashNamesParam.put("en", "Crotches + pubis");
		keyNameMulti = keyNameMultiBase + "beauty_crotches_pubis";
		newLocalTask(localId, lotTaskId, "0", "0", "20", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Ingles brasileñas");
		hashNamesParam.put("en", "Crotches brazilian");
		keyNameMulti = keyNameMultiBase + "beauty_crotches_brazilian";
		newLocalTask(localId, lotTaskId, "0", "0", "13", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Axilas");
		hashNamesParam.put("en", "Armpits");
		keyNameMulti = keyNameMultiBase + "beauty_armpits";
		newLocalTask(localId, lotTaskId, "10", "0", "5", false, keyNameMulti,
				hashNamesParam);

		// Pestañas
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_eyelashes";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Pestañas");
		hashNamesParam.put("en", "Eyelashes");
		keyNameMulti = keyNameMultiBase + "beauty_eyelashes";
		newLocalTask(localId, lotTaskId, "30", "0", "0", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Pestañas permanente");
		hashNamesParam.put("en", "Eyelashes permanent");
		keyNameMulti = keyNameMultiBase + "beauty_eyelashes_permanent";
		newLocalTask(localId, lotTaskId, "0", "0", "18", false, keyNameMulti,
				hashNamesParam);

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Pestañas tinte");
		hashNamesParam.put("en", "Eyelashes dye");
		keyNameMulti = keyNameMultiBase + "beauty_eyelashes_dye";
		newLocalTask(localId, lotTaskId, "0", "0", "18", false, keyNameMulti,
				hashNamesParam);

		// Combinados

		List<Long> lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		List<Integer> lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(20);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(20);
		lotTaskCombiRes.add(0);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(20);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(60);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(60);
		lotTaskCombiRes.add(0);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(60);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(0);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady_molded";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(0);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "beauty_eyebrows";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "beauty_lip";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(0);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "beauty_manicure";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "beauty_pedicure";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(0);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "beauty_crotches";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "beauty_armpits";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(0);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/initloadLocalTaskCab_no")
	@ResponseStatus(HttpStatus.OK)
	protected void initloadLocalTaskCab(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		Long localId = new Long(arg0.getParameter("localId"));

		String keyNameMultiBase = LocalTaskManager.KEY_MULTI_LOCAL_TASK_NAME
				+ localId + "_";

		// Peluqueria

		// Corte de pelo caballero
		String taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME
				+ "hairdresser_haircut_gentleman";
		Long lotTaskId = (taskManager.getByName(taskMulti)).getId();

		Map<String, String> hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Corte pelo caballero");
		hashNamesParam.put("en", "Haircut gentleman");
		String keyNameMulti = keyNameMultiBase
				+ "hairdresser_haircut_gentleman";
		newLocalTask(localId, lotTaskId, "15", "0", "10", true, keyNameMulti,
				hashNamesParam);

		// Corte de pelo niño
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_haircut_baby";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Corte de pelo niño (hasta 8 años)");
		hashNamesParam.put("en", "Haircut child (up to 8 years)");
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_child";
		newLocalTask(localId, lotTaskId, "15", "0", "9", false, keyNameMulti,
				hashNamesParam);

		// Tinte caballero
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_dye_gentleman";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Tinte caballero");
		hashNamesParam.put("en", "Hair dye gentleman");
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_gentleman";
		newLocalTask(localId, lotTaskId, "25", "20", "18", false, keyNameMulti,
				hashNamesParam);

		// Mechas caballero
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME
				+ "hairdresser_wicks_gentleman";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Mechas caballero");
		hashNamesParam.put("en", "Hair wicks gentleman");
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_gentleman";
		newLocalTask(localId, lotTaskId, "40", "40", "30", false, keyNameMulti,
				hashNamesParam);

		// Barba
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_beard";
		lotTaskId = (taskManager.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Barba");
		hashNamesParam.put("en", "Beard");
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_beard";
		newLocalTask(localId, lotTaskId, "40", "0", "5", false, keyNameMulti,
				hashNamesParam);

		// Combinados

		List<Long> lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_gentleman";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_gentleman";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		List<Integer> lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(20);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_gentleman";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_gentleman";
		lotTaskId = (localTaskManager.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(40);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/loadAux_no")
	@ResponseStatus(HttpStatus.OK)
	protected void loadAux(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		Long localId = new Long(arg0.getParameter("localId"));

		List<LocalTaskDTO> localTaskList = localTaskManager.getLocalTaskSimpleInv(
				localId, "es");
		Collections.sort(localTaskList, new SortByTask());
		for (LocalTaskDTO localTaskDTO : localTaskList) {
			System.out.print(localTaskDTO.getLotName());
			for (int i = 0; i < (40 - localTaskDTO.getLotName().length()); i++) {
				System.out.print(" ");
			}
			System.out.print(localTaskDTO.getLotTaskRate() + " eur");
			if (localTaskDTO.getLotTaskDuration() > 0) {
				System.out.println(" (este servicio vale tb para reserva, "
						+ localTaskDTO.getLotTaskDuration() + " min)");
			} else
				System.out.println();
		}

		/*
		 * LocalDTO local = localDAO.getById(localId);
		 * local.setLocNewClientDefault(1); localDAO.update(local);
		 */
		/*
		 * String firEmail =
		 * "tribaldopelu.ETS@gmail.com";//arg0.getParameter("firEmail");
		 * List<String> firGwtUsers = new ArrayList<String>();
		 * firGwtUsers.add(firEmail); Set<AppRole> roles =
		 * EnumSet.noneOf(AppRole.class); roles.add(AppRole.MANAGER);
		 * roles.add(AppRole.OPERATOR); AppUser user = new AppUser(firEmail,
		 * roles, true);
		 * 
		 * userRegistry.registerUser(user); FirmDTO firm = firmDAO.getById(new
		 * Long("5021797632180224")); firm.setFirGwtUsers(firGwtUsers); firm =
		 * firmDAO.update(firm);
		 */
	}

	private class SortByTask implements Comparator {
		public int compare(Object obj1, Object obj2) {
			return (((LocalTaskDTO) obj1).getLotTaskId())
					.compareTo(((LocalTaskDTO) obj2).getLotTaskId());
			// return
			// (((LocalTaskDTO)obj1).getLotTaskId()Name()).compareToIgnoreCase(((LocalTaskDTO)obj2).getLotName());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/getTextProperties_no")
	@ResponseStatus(HttpStatus.OK)
	private void getTextProperties(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		Locale locale = new Locale("en");
		ExtendMessageSource messageSourceApp = (ExtendMessageSource) ApplicationContextProvider
				.getApplicationContext().getBean("messageSource");
		Properties props = messageSourceApp.getResolvedProps("es");
		for (String key : props.stringPropertyNames()) {
			System.out.println(props.getProperty(key));
			locale = new Locale("en");
			// System.out.println(messageSourceApp.getMessage(key, null,
			// locale));
			// locale = new Locale("fr");
			System.out.println(messageSourceApp.getMessage(key, null, locale));
			System.out.println("Aleman");
			// System.out.println("Vasco");
			// System.out.println("Catalan");
			System.out.println();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/setTextProperties_no")
	@ResponseStatus(HttpStatus.OK)
	private void setTextProperties(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		ExtendMessageSource messageSourceApp = (ExtendMessageSource) ApplicationContextProvider
				.getApplicationContext().getBean("messageSource");
		Properties props = messageSourceApp.getResolvedProps("es");
		BidiMap map1 = new DualHashBidiMap();
		for (String key : props.stringPropertyNames()) {
			map1.put(key, props.getProperty(key));
		}

		props = messageSourceApp.getResolvedProps("eu"); // "fr"
		URL url = getClass().getClassLoader().getResource(
				"PeluqueriaFrancesVasco.txt");
		BufferedReader textsIn = new BufferedReader(new FileReader(
				url.getFile()));
		String strValue = "";
		String key = "";
		int line = 0;
		while (true) {
			strValue = textsIn.readLine();
			line++;
			if (strValue == null) {
				break;
			}
			key = (String) map1.getKey(strValue);
			if (key == null) {
				System.out.println("Line " + line
						+ " no encontrado ---------------------" + strValue);
				textsIn.readLine();
				line++;
			} else {
				textsIn.readLine();
				line++; // Poner para Vasco
				strValue = textsIn.readLine();
				line++;
				strValue = StringUtils.capitalise(strValue); // Poner para Vasco
				if (!key.startsWith("web.")
						&& !strValue.equals(props.getProperty(key))) {
					System.out.println("key " + key + " nuevo:" + strValue
							+ "-  actual:" + props.getProperty(key));
				}
			}
			for (int i = 0; i < 1; i++) { // 2 para francés 1 para vasco
				textsIn.readLine();
				line++;
			}
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/updateCalendar_no")
	@ResponseStatus(HttpStatus.OK)
	protected void updateCalendarObject(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		String calId = arg0.getParameter("id");

		CalendarDTO calendar = calendarManager.getById(new Long(calId));

		DiaryDTO diary = new DiaryDTO();
		diary.setEnabled(1);
		List<String> diaTimes = new ArrayList<String>();
		String strTime = "10:00";
		diaTimes.add(strTime);
		strTime = "13:45";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		strTime = "17:00";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);
		strTime = "20:15";
		diaTimes.add(strTime);
		diary.setDiaTimes(diaTimes);

		// DiaryDTO diaryCreatedMon = diaryDAO.create(diary);
		// DiaryDTO diaryCreatedTue = diaryDAO.create(diary);
		// DiaryDTO diaryCreatedWed = diaryDAO.create(diary);
		// DiaryDTO diaryCreatedThu = diaryDAO.create(diary);
		// DiaryDTO diaryCreatedFri = diaryDAO.create(diary);
		// DiaryDTO diaryCreatedSat = diaryDAO.create(diary);
		// DiaryDTO diaryCreatedSun = diaryDAO.create(diary);
		//
		//
		// SemanalDiaryDTO semanalDiary = calendar.getCalSemanalDiary();
		//
		// semanalDiary.setSemMonDiary(diaryCreatedMon);
		// semanalDiary.setSemTueDiary(diaryCreatedTue);
		// semanalDiary.setSemWedDiary(diaryCreatedWed);
		// semanalDiary.setSemThuDiary(diaryCreatedThu);
		// semanalDiary.setSemFriDiary(diaryCreatedFri);
		// semanalDiary.setSemSatDiary(diaryCreatedSat);
		// semanalDiary.setSemSunDiary(diaryCreatedSun);
		//
		// semanalDiary = semanalDiaryDAO.update(semanalDiary);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/update_no")
	@ResponseStatus(HttpStatus.OK)
	protected void updateObject(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		String domain = arg0.getParameter("domain");

		if (domain != null) {
			FirmDTO firm = firmManager.getFirmDomainAdmin(domain);
			List<LocalDTO> localList = localManager.getLocalAdmin(firm.getId());
			for (LocalDTO local : localList) {

				SincroDTO sincroGCal = new SincroDTO();
				sincroGCal.setEnabled(1);
				sincroGCal.setSinType(0);
				sincroGCal.setSinConEmail("info@bookingprof.com");// info@bookingprof.com
				// sincroGCal = sincroDAO.create(sincroGCal);

				// local.setLocSinGCalendar(sincroGCal);
				local.setLocMulServices(1);
				local.setLocSelCalendar(0);
				local.setLocCacheTasks(0);
				// localDAO.update(local);
			}
		} else {
			Long localId = new Long(arg0.getParameter("localId"));
			LocalDTO local = localManager.getById(localId);

			SincroDTO sincroGCal = new SincroDTO();
			sincroGCal.setEnabled(1);
			sincroGCal.setSinType(0);
			sincroGCal.setSinConEmail("info@bookingprof.com");// info@bookingprof.com
			//sincroGCal = sincroDAO.create(sincroGCal);

			local.setLocSinGCalendar(sincroGCal);

			//localDAO.update(local);
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/updateLocalTask_no")
	@ResponseStatus(HttpStatus.OK)
	protected void updateLocalTask(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		Locale locale = RequestContextUtils.getLocale(arg0);

		List<LocalDTO> listLocal = null;
		List<LocalTaskDTO> list = null;
		List<FirmDTO> firms = firmManager.getFirmAdmin();
		for (FirmDTO firm : firms) {
			listLocal = localManager.getLocalAdmin(firm.getId());
			for (LocalDTO local : listLocal) {
				list = localTaskManager.getLocalTaskAdmin(local.getId(),
						locale.getLanguage());
				for (LocalTaskDTO localTask : list) {
					if (localTask.getLotVisible() == null) {
						localTask.setLotVisible(1);
						localTaskManager.update(localTask);
					}
				}
			}
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/updateLocalTaskPost")
	@ResponseStatus(HttpStatus.OK)
	protected void updateLocalTaskPost(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		Locale locale = RequestContextUtils.getLocale(arg0);

		List<LocalDTO> listLocal = null;
		List<LocalTaskDTO> list = null;
		List<FirmDTO> firms = firmManager.getFirmAdmin();
		for (FirmDTO firm : firms) {
			listLocal = localManager.getLocalAdmin(firm.getId());
			for (LocalDTO local : listLocal) {
				list = localTaskManager.getLocalTaskAdmin(local.getId(), locale.getLanguage());
				for (LocalTaskDTO localTaskSearch : list) {
					List<Long> combi = localTaskSearch.getLotTaskCombiId();
					if (combi!=null && combi.size()>0){
						List<Integer> combiRes = localTaskSearch.getLotTaskCombiRes();
						int indx = 0;
						int taskPost = 0;
						for (Long localTaskComId : combi) {
							if (indx<combiRes.size()){
								LocalTaskDTO localTask = localTaskManager.getById(localTaskComId);
								if (localTask.getLotTaskPost() == null && combiRes.get(indx)>0) {
									taskPost = combiRes.get(indx);
									localTask.setLotTaskPost(taskPost);
									localTaskManager.update(localTask);
								}
								indx ++;
							}
						}	
					}	
				}
			}
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/admin/updateEvent_no")
	@ResponseStatus(HttpStatus.OK)
	protected void updateEvent(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		List<LocalDTO> listLocal = null;
		List<CalendarDTO> listCalendar = null;
		List<EventDTO> list = null;
		List<FirmDTO> firms = firmManager.getFirmAdmin();
		for (FirmDTO firm : firms) {
			listLocal = localManager.getLocalAdmin(firm.getId());
			for (LocalDTO local : listLocal) {
				listCalendar = calendarManager.getCalendarAdmin(local.getId());
				for (CalendarDTO calendar : listCalendar) {
					list = eventManager.getEventAdmin(calendar);
					for (EventDTO event : list) {
						if (event.getEveEndTime() == null) {
							LocalTaskDTO localTask = event.getEveLocalTask();
							Calendar calendarGreg = new GregorianCalendar();
							calendarGreg.setTime(event.getEveStartTime());
							calendarGreg.add(Calendar.MINUTE,
									localTask.getLotTaskDuration());
							event.setEveEndTime(calendarGreg.getTime());
							eventManager.update(event);
						}
					}
				}
			}
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/migrateClient_no")
	@ResponseStatus(HttpStatus.OK)
	protected void migrateClient(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		List<Entity> resultQuery = null;

		try {

			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"ClientPere");

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(1000));
			int max = 1000;
			int indx = 0;
			Long firmId = new Long("5165000163328000");

			for (Entity entity : resultQuery) {
				if (indx < max) {
					com.google.appengine.api.datastore.Entity newEntity = new Entity(
							"Client");
					newEntity.setPropertiesFrom(entity);

					newEntity.setProperty("cliCreationTime", new Date());

					newEntity.setProperty("resFirId", firmId);
					newEntity.removeProperty("resAppId");

					dataStore.put(newEntity);
					// log.info("migrateClientInsertttt: Put newClient: "+newEntity);
					indx++;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/migrateEvent_no")
	@ResponseStatus(HttpStatus.OK)
	protected void migrateEvent(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		List<Entity> resultQuery = null;

		try {

			/*
			 * String[] dates =
			 * selectedDate.split(CalendarController.CHAR_SEP_DATE); String year
			 * = dates[0]; String month = dates[1]; String day = dates[2];
			 * 
			 * Calendar calendarGreg = new GregorianCalendar();
			 * calendarGreg.set(Calendar.YEAR, new Integer(year));
			 * calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
			 * calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
			 * calendarGreg.set(Calendar.HOUR_OF_DAY,0);
			 * calendarGreg.set(Calendar.MINUTE,0);
			 * calendarGreg.set(Calendar.SECOND,0);
			 * calendarGreg.set(Calendar.MILLISECOND,0); Date startTime =
			 * calendarGreg.getTime();
			 * 
			 * Filter fromFilter = new FilterPredicate("eveStartTime",
			 * FilterOperator.GREATER_THAN_OR_EQUAL, startTime);
			 * 
			 * com.google.appengine.api.datastore.Query query = new
			 * com.google.appengine
			 * .api.datastore.Query("Event").setFilter(fromFilter);
			 */
			com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
					"EventPere");
			query.addSort("eveBookingTime", SortDirection.ASCENDING);

			DatastoreService dataStore = DatastoreServiceFactory
					.getDatastoreService();
			PreparedQuery pq = dataStore.prepare(query);
			resultQuery = pq.asList(FetchOptions.Builder.withLimit(1000));
			int max = 1000;
			int indx = 0;

			LocalTaskDTO localTask = null;
			Long clientId = null;
			Long calendarId = new Long("6320998664110080");
			String eveGoogleId = null;
			String[] a = null;
			for (Entity entity : resultQuery) {
				if (indx < max) {
					com.google.appengine.api.datastore.Entity newEntity = new Entity(
							"Event");
					newEntity.setPropertiesFrom(entity);

					localTask = getLocalTask((Long) entity
							.getProperty("eveTaskId"));
					newEntity.setProperty("eveLocalTaskId", localTask.getId());
					newEntity.removeProperty("eveTaskId");

					Calendar calendarGreg = new GregorianCalendar();
					calendarGreg.setTime((Date) entity
							.getProperty("eveStartTime"));
					calendarGreg.add(Calendar.MINUTE,
							localTask.getLotTaskDuration());
					newEntity.setProperty("eveEndTime", calendarGreg.getTime());

					clientId = getClientId((Long) entity
							.getProperty("eveClientId"));
					newEntity.setProperty("eveClientId", clientId);

					newEntity.setProperty("eveCalendarId", calendarId);

					eveGoogleId = (String) entity.getProperty("eveGoogleId");
					a = eveGoogleId.split("_");
					eveGoogleId = a[1].replace("@Pere Peluqueros",
							"@BookingProf");
					newEntity.setProperty("eveICS", clientId + "_"
							+ eveGoogleId);
					newEntity.removeProperty("eveGoogleId");

					newEntity.removeProperty("eveRate");

					if (clientId != null) {
						log.info("migrateEvent: Put newEvent: " + newEntity);
						if (indx == 0) {
							dataStore.put(newEntity);
							log.info("migrateEventInsertttt: Put newEvent: "
									+ newEntity);
						}
					}
					indx++;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private LocalTaskDTO getLocalTask(Long idPere) {
		Long id = null;
		if (idPere.longValue() == 74007) { // Corte pelo caballero
			id = new Long("5674248798470144");
		} else if (idPere.longValue() == 107004) { // Barba
			id = new Long("5112264407384064");
		} else if (idPere.longValue() == 73005) { // Mechas
			id = new Long("4902396467609600");
		} else if (idPere.longValue() == 72005) { // Desrizado
			id = new Long("5156048109305856");
		}
		return localTaskManager.getById(id);
	}

	private Long getClientId(long idClient) {

		Filter idFilter = new FilterPredicate("idPere", FilterOperator.EQUAL,
				idClient);
		DatastoreService dataStore = DatastoreServiceFactory
				.getDatastoreService();
		com.google.appengine.api.datastore.Query query = new com.google.appengine.api.datastore.Query(
				"Client").setFilter(idFilter);
		PreparedQuery pq = dataStore.prepare(query);
		if (pq.countEntities(FetchOptions.Builder.withDefaults()) == 1) {
			Entity ent = pq.asSingleEntity();
			return ent.getKey().getId();
		} else {
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/createRepeat")
	@ResponseStatus(HttpStatus.OK)
	protected void createRepeat(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		// RepeatController rc = new RepeatController();
		// rc.newMatrix(arg0, arg1, localId);

		RepeatDTO repeatDTO = new RepeatDTO();

		repeatDTO.setEnabled(1);
		// repeatDTO.setEveBookingTime(eveBookingTime);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/admin/classSystem_no")
	@ResponseStatus(HttpStatus.OK)
	protected void classSystem1(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// TaskClass

		MultiTextDTO nameMulti = null;

		// Deporte

		String nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskClassManager.KEY_MULTI_TASKCLASS_NAME + "sport";
		//TaskClassDTO taskClassSport = taskClassDAO.getByName(nameKey);
		TaskClassDTO taskClassSport = new TaskClassDTO();
		taskClassSport.setEnabled(1);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Deporte");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Sport");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Esporte");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Sport");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Kirola");
		multiTextManager.create(nameMulti);

		taskClassSport.setTclNameMulti(nameKey);

		taskClassSport = taskClassManager.create(taskClassSport);

		// Fisio

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskClassManager.KEY_MULTI_TASKCLASS_NAME + "physio";
		//TaskClassDTO taskClassPhysio = taskClassDAO.getByName(nameKey);
		TaskClassDTO taskClassPhysio = new TaskClassDTO();
		taskClassPhysio.setEnabled(1);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Fisio");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Physio");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Fisio");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Physio");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Fisio");
		multiTextManager.create(nameMulti);

		taskClassPhysio.setTclNameMulti(nameKey);

		taskClassPhysio = taskClassManager.create(taskClassPhysio);

		// Psicologia

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskClassManager.KEY_MULTI_TASKCLASS_NAME + "psychology";
		// TaskClassDTO taskClassPsychology = taskClassDAO.getByName(nameKey);
		TaskClassDTO taskClassPsychology = new TaskClassDTO();
		taskClassPsychology.setEnabled(1);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Psicologia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Psychology");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Psicologia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Psychologie");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Psikologia");
		multiTextManager.create(nameMulti);

		taskClassPsychology.setTclNameMulti(nameKey);

		taskClassPsychology = taskClassManager.create(taskClassPsychology);

		// Pelu canina

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskClassManager.KEY_MULTI_TASKCLASS_NAME + "canine";
		//TaskClassDTO taskClassCanine = taskClassDAO.getByName(nameKey);
		TaskClassDTO taskClassCanine = new TaskClassDTO();
		taskClassCanine.setEnabled(1);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Canina");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Canine");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Canino");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Canin");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakurrena");
		multiTextManager.create(nameMulti);

		taskClassCanine.setTclNameMulti(nameKey);

		taskClassCanine = taskClassManager.create(taskClassCanine);

		// Podologia

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskClassManager.KEY_MULTI_TASKCLASS_NAME + "chiropody";
		// TaskClassDTO taskClassChiropody = taskClassDAO.getByName(nameKey);
		TaskClassDTO taskClassChiropody = new TaskClassDTO();
		taskClassChiropody.setEnabled(1);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Podologia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Chiropody");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Quiropodia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Podologie");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Podologia");
		multiTextManager.create(nameMulti);

		taskClassChiropody.setTclNameMulti(nameKey);

		taskClassChiropody = taskClassManager.create(taskClassChiropody);

		// Dentista

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskClassManager.KEY_MULTI_TASKCLASS_NAME + "dentist";
		// TaskClassDTO taskClassDentist = taskClassDAO.getByName(nameKey);
		TaskClassDTO taskClassDentist = new TaskClassDTO();
		taskClassDentist.setEnabled(1);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Dentista");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dentist");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Dentista");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Dentiste");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Dentista");
		multiTextManager.create(nameMulti);

		taskClassDentist.setTclNameMulti(nameKey);

		taskClassDentist = taskClassManager.create(taskClassDentist);

		// Task

		// Tenis
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "sport_tennis";

		TaskDTO task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassSport);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Tenis");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Tennis");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Tênis");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Tennis");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Tenisa");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Padel
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "sport_padel";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassSport);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Pádel");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Padel");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Padel");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Padel");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Padela");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Fisioterapia
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "physio_physiotherapy";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassPhysio);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Fisioterapia - Osteopatía");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Physiotherapy - Osteopathy");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Fisioterapia - Osteopatia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Physiothérapie - Ostéopathie");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Fisioterapia - Osteopatía");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Crioterapia
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "physio_cryotherapy";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassPhysio);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Crioterapia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cryotherapy");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Crioterapia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Cryothérapie");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Crioterapia");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Psicoterapia
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "psychology_psychotherapy";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassPsychology);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Psicoterapia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Psychotherapy");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Psicoterapia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Psychothérapie");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Psikoterapia");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Coaching

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "psychology_coaching";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassPsychology);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Coaching");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Coaching");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Coaching");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coaching");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Coaching");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Corte perro

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "canine_haircut_dog";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCanine);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Corte perro");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dog haircut");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corte de cabelo cão");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coupe de cheveux chien");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakurreko Ile mozketa");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Corte gato

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "canine_haircut_cat";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCanine);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Corte gato");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cat haircut");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corte de cabelo gato");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coupe de cheveux chat");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Katuko Ile mozketa");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Lavado perro

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "canine_wash_dog";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCanine);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Lavado perro");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dog wash");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Lavagem do cão");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Lavage de chien");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakur-garbiketa");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Lavado gato

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "canine_wash_cat";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCanine);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Lavado gato");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cat wash");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Lavagem do gato");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Lavage de chat");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Katu-garbiketa");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Deslanado perro

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "canine_brushing_dog";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCanine);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Cepillado - Deslanado perro");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dog brushing");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Escovação do cão");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Brossage de chien");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakur-eskuilatzea");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Deslanado gato

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "canine_brushing_cat";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCanine);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Cepillado - Deslanado gato");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cat brushing");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Escovação do gato");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Brossage de chat");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Katu-eskuilatzea");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Desparasitacion perro

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "canine_deworming_dog";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCanine);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Desparasitación perro");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dog deworming");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Desparasitação do cão");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Déparasitage de chien");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakur-desparasitación-a");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Desparasitacion gato

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "canine_deworming_cat";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCanine);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Desparasitación gato");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cat deworming");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Desparasitação do gato");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Déparasitage de chat");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Katu-desparasitación-a");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Tratamientos perro

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "canine_treatments_dog";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCanine);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Tratamientos perro");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dog treatments");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Tratamentos do cão");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Traitements de chien");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakur-tratamenduak");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Tratamientos gato

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "canine_treatments_cat";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCanine);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Tratamientos gato");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cat treatments");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Tratamentos do gato");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Traitements de chat");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Katu-tratamenduak");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Stripping perro

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "canine_stripping_dog";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCanine);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Stripping perro");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dog stripping");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Stripping do cão");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Stripping de chien");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakur-stripping");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Stripping gato

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "canine_stripping_cat";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassCanine);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Stripping gato");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cat stripping");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Stripping do gato");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Stripping de chat");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Katu-stripping");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Biomecánica

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "chiropody_biomechanics";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassChiropody);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Biomecánica");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Biomechanics");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Biomecânica");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Biomécanique");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Biomekanika");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Plantillas

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "chiropody_templates";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassChiropody);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Plantillas");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Templates");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Templates");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Modèles");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txantiloiak");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Patología del pie

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "chiropody_pathology";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassChiropody);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Patología del pie");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Foot pathology");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Patologia do pé");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Pathologie de pied");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Oinaren patologia");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Cirugía del pie

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "chiropody_surgery";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassChiropody);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Cirugía del pie");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Foot surgery");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Cirurgia do pé");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Chirurgie de pied");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Oinaren kirurgia");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Estética del pie

		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "chiropody_aesthetics";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassChiropody);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Estética del pie");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Foot aesthetics");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Estética do pé");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Esthétique de pied");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Oinaren estetika");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);
		
		// Consulta
		
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "dentist_consultation";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassDentist);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Consulta");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Consultation");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Consulta");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Consultation");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Kontsulta");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Limpieza de boca
		
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "dentist_cleaning";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassDentist);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Limpieza de boca");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Oral cleaning");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Limpeza bucal");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Nettoyage buccal");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Aho-garbiketa");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Empaste
		
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "dentist_filling";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassDentist);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Empaste");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Filling");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Enchimento");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Remplissage");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Enpastea");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Endodoncia
		
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "dentist_endodontics";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassDentist);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Endodoncia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Endodontics");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Endodontia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Endodontie");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Endodontzia");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Cirugía
		
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "dentist_surgery";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassDentist);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Cirugía");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Surgery");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Cirurgia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Chirurgie");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Kirurgia");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Periodoncia
		
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "dentist_periodontics";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassDentist);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Periodoncia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Periodontics");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Periodontia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Parodontie");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Periodoncia");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Ortodoncia
		
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "dentist_orthodontics";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassDentist);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Ortodoncia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Orthodontics");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Ortodontia");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Orthodontie");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Ortodontzia");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Blanqueamiento
		
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "dentist_whitening";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassDentist);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Blanqueamiento");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Whitening");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Branqueamento");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Blanchiment");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Blanqueamiento-a");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Implantes
		
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "dentist_implants";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassDentist);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Implantes");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Implants");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Implantes");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Implants");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Inplanteak");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		// Prótesis o fundas
		
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "dentist_prosthesis";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassDentist);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Prótesis o fundas");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Prosthesis or sleeves");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Prótese ou mangas");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Prothèse ou manches");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Protesia edo zorroak");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);

		
	}
		
	@RequestMapping(method = RequestMethod.GET, value = "/admin/classSystem2_no")
	@ResponseStatus(HttpStatus.OK)
	protected void classSystem2(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// TaskClass

		MultiTextDTO nameMulti = null;

		// Mercancia

		String nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskClassManager.KEY_MULTI_TASKCLASS_NAME + "goods";
		//TaskClassDTO taskClassGoods = taskClassDAO.getByName(nameKey);
		TaskClassDTO taskClassGoods = new TaskClassDTO();
		taskClassGoods.setEnabled(1);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Mercancía");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Goods");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Mercadorias");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Marchandises");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Salgaia");
		multiTextManager.create(nameMulti);

		taskClassGoods.setTclNameMulti(nameKey);

		taskClassGoods = taskClassManager.create(taskClassGoods);


		// Task

		// Descarga de mercancia
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "goods_unloading";

		TaskDTO task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassGoods);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Descarga de mercancía");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Unloading goods");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Descarga de mercadorias");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Marchandises de déchargement");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Salgai-deskarga");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);
		
		// Carga de mercancia
		nameKey = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "goods_loading";

		task = new TaskDTO();
		task.setEnabled(1);
		task.setTasClass(taskClassGoods);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("es");
		nameMulti.setMulText("Carga de mercancía");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Loading goods");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Carga de mercadorias");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Marchandises de chargement");
		multiTextManager.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Salgai-karga");
		multiTextManager.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskManager.create(task);
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/admin/createAdveoUsers_no")
	@ResponseStatus(HttpStatus.OK)
	protected void createAdveoUsers(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		//String domain = arg0.getParameter("adveo");
		String domain = "adveo";
		FirmDTO firm = firmManager.getFirmDomainAdmin(domain);
		
		if (firm != null) {
			
			TimeZone calendarTimeZone = TimeZone.getTimeZone("Europe/Madrid");
			GregorianCalendar calendarGreg = new GregorianCalendar();
			calendarGreg.set(Calendar.MILLISECOND, 0);
			Date bookingTime = calendarGreg.getTime();
			// Calculamos el desplazamiento de la zona horaria desde UTC
			bookingTime =  new Date(bookingTime.getTime() + calendarTimeZone.getOffset(bookingTime.getTime()));
			

			AppUser user = null;
						
			// User USER 1
			String cliEmail = "bkadveopilot@gmail.com";
			String cliName = "Pilot";
			String cliTelf1 = "623451";
			
			ClientDTO client = new ClientDTO();
			client.setEnabled(1);
			client.setWhoEmail(cliEmail);
			client.setWhoName(cliName);
			client.setWhoTelf1(cliTelf1);
			client.setCliCreationTime(bookingTime);
			client.setResFirId(firm.getId());
			clientManager.create(client);

			Set<AppRole> roles = EnumSet.noneOf(AppRole.class);
			roles.add(AppRole.USER);
			if(userRegistry.findUser(cliEmail, firm.getId())==null){
				user = new AppUser(cliEmail, roles, true, firm.getId());
				userRegistry.registerUser(user);
			}
						
			// User USER 2
			cliEmail = "bkadveotesa@gmail.com";
			cliName = "Tesa";
			cliTelf1 = "623218";
			
			client = new ClientDTO();
			client.setEnabled(1);
			client.setWhoEmail(cliEmail);
			client.setWhoName(cliName);
			client.setWhoTelf1(cliTelf1);
			client.setCliCreationTime(bookingTime);
			client.setResFirId(firm.getId());
			clientManager.create(client);
			
			if(userRegistry.findUser(cliEmail, firm.getId())==null){
				user = new AppUser(cliEmail, roles, true, firm.getId());
				userRegistry.registerUser(user);
			}
			
			// User USER 3
			cliEmail = "bkadveohp@gmail.com";
			cliName = "HP";
			cliTelf1 = "623423";
			
			client = new ClientDTO();
			client.setEnabled(1);
			client.setWhoEmail(cliEmail);
			client.setWhoName(cliName);
			client.setWhoTelf1(cliTelf1);
			client.setCliCreationTime(bookingTime);
			client.setResFirId(firm.getId());
			clientManager.create(client);
			
			if(userRegistry.findUser(cliEmail, firm.getId())==null){
				user = new AppUser(cliEmail, roles, true, firm.getId());
				userRegistry.registerUser(user);
			}

			// User OPERATOR_READ 1
			cliEmail = "bkadveoluis@gmail.com";
						
			roles = EnumSet.noneOf(AppRole.class);
			roles.add(AppRole.OPERATOR_READ);
			if(userRegistry.findUser(cliEmail, firm.getId())==null){
				user = new AppUser(cliEmail, roles, true, firm.getId());
				userRegistry.registerUser(user);
			}

			firm = firmManager.update(firm);
			
		}
	}

	public void setMessageSourceApp(MessageSource messageSourceApp) {
		this.messageSourceApp = messageSourceApp;
	}

	public void setGeneratorVelocity(Generator generatorVelocity) {
		this.generatorVelocity = generatorVelocity;
	}

	public void setLocalDAO(ILocalManager iLocalManager) {
		this.localManager = iLocalManager;
	}

	public void setMultiTextDAO(IMultiTextManager iMultiTextManager) {
		this.multiTextManager = iMultiTextManager;
	}

	public void setProfessionalDAO(IProfessionalManager iProfessionalManager) {
		this.professionalManager = iProfessionalManager;
	}

	public void setLangDAO(ILangManager iLangManager) {
		this.langManager = iLangManager;
	}

	public void setWhereDAO(IWhereManager iWhereManager) {
		this.whereManager = iWhereManager;
	}

	public void setTaskDAO(ITaskManager iTaskManager) {
		this.taskManager = iTaskManager;
	}

	public void setTaskClassDAO(ITaskClassManager iTaskClassManager) {
		this.taskClassManager = iTaskClassManager;
	}

	public void setSemanalDiaryDAO(ISemanalDiaryManager iSemanalDiaryManager) {
		this.semanalDiaryManager = iSemanalDiaryManager;
	}

	public void setLocalTaskDAO(ILocalTaskManager iLocalTaskManager) {
		this.localTaskManager = iLocalTaskManager;
	}

	public void setSincroDAO(ISincroManager iSincroManager) {
		this.sincroManager = iSincroManager;
	}

	public void setUserRegistry(UserRegistry userRegistry) {
		this.userRegistry = userRegistry;
	}
	
	
}
