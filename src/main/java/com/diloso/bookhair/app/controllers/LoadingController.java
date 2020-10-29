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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dao.CalendarDAO;
import com.diloso.bookhair.app.negocio.dao.ClientDAO;
import com.diloso.bookhair.app.negocio.dao.DiaryDAO;
import com.diloso.bookhair.app.negocio.dao.EventDAO;
import com.diloso.bookhair.app.negocio.dao.FirmDAO;
import com.diloso.bookhair.app.negocio.dao.LangDAO;
import com.diloso.bookhair.app.negocio.dao.LocalDAO;
import com.diloso.bookhair.app.negocio.dao.LocalTaskDAO;
import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dao.ProfessionalDAO;
import com.diloso.bookhair.app.negocio.dao.SemanalDiaryDAO;
import com.diloso.bookhair.app.negocio.dao.SincroDAO;
import com.diloso.bookhair.app.negocio.dao.TaskClassDAO;
import com.diloso.bookhair.app.negocio.dao.TaskDAO;
import com.diloso.bookhair.app.negocio.dao.WhereDAO;
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
import com.diloso.bookhair.app.negocio.utils.ApplicationContextProvider;
import com.diloso.bookhair.app.negocio.utils.ExtendMessageSource;
import com.diloso.bookhair.app.negocio.utils.templates.Generator;
import com.diloso.bookhair.app.persist.manager.LocalTaskManager;
import com.diloso.bookhair.app.persist.manager.MultiTextManager;
import com.diloso.bookhair.app.persist.manager.TaskClassManager;
import com.diloso.bookhair.app.persist.manager.TaskManager;
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

@Controller
@RequestMapping("/loading")
public class LoadingController {

	protected static final Logger log = Logger
			.getLogger(LoadingController.class.getName());

	//@Autowired
	protected MessageSource messageSourceApp;

	//@Autowired
	protected Generator generatorVelocity;

	//@Autowired
	protected LocalDAO localDAO;

	//@Autowired
	protected CalendarDAO calendarDAO;

	//@Autowired
	protected FirmDAO firmDAO;

	//@Autowired
	protected MultiTextDAO multiTextDAO;

	//@Autowired
	protected ProfessionalDAO professionalDAO;

	//@Autowired
	protected LangDAO langDAO;

	//@Autowired
	protected WhereDAO whereDAO;

	//@Autowired
	protected TaskDAO taskDAO;

	//@Autowired
	protected TaskClassDAO taskClassDAO;

	//@Autowired
	protected DiaryDAO diaryDAO;

	//@Autowired
	protected SemanalDiaryDAO semanalDiaryDAO;

	//@Autowired
	protected LocalTaskDAO localTaskDAO;

	//@Autowired
	protected SincroDAO sincroDAO;

	//@Autowired
	protected EventDAO eventDAO;

	//@Autowired
	protected ClientDAO clientDAO;

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
		lang = langDAO.create(lang);
		
		lang = new LangDTO();
		lang.setEnabled(1);
		lang.setLanCode("en");
		lang.setLanName("English");
		lang = langDAO.create(lang);
		
		lang = new LangDTO();
		lang.setEnabled(1);
		lang.setLanCode("pt");
		lang.setLanName("Portugues");
		lang = langDAO.create(lang);
		
		lang = new LangDTO();
		lang.setEnabled(1);
		lang.setLanCode("fr");
		lang.setLanName("Français");
		lang = langDAO.create(lang);

		lang = new LangDTO();
		lang.setEnabled(1);
		lang.setLanCode("eu");
		lang.setLanName("Euskera");
		lang = langDAO.create(lang);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Hairdresser");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Cabeleireiro");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coiffure");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Ileapaindegia");
		multiTextDAO.create(nameMulti);

		taskClassHair.setTclNameMulti(nameKey);
		taskClassHair = taskClassDAO.create(taskClassHair);

		
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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Beauty");
		multiTextDAO.create(nameMulti);
	
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Beleza");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Beauté");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Edertasuna");
		multiTextDAO.create(nameMulti);

		taskClassBeauty.setTclNameMulti(nameKey);

		taskClassBeauty = taskClassDAO.create(taskClassBeauty);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Celebration");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Celebrações");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Célébrations");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Ospakizunak");
		multiTextDAO.create(nameMulti);

		taskClassCelebration.setTclNameMulti(nameKey);

		taskClassCelebration = taskClassDAO.create(taskClassCelebration);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Haircut gentleman");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corte de cabelo cabellero");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coupe de cheveux homme");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gizonentzat ile mozketa");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Hairstyle gentleman");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Penteado cabellero");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coiffure homme");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gizonentzat orrazkera");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dye gentleman");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corante cabellero");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Colorant homme");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gizonentzat tindatzea");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Wicks gentleman");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Mechas cabellero");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Wicks homme");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gizonentzat metxak");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Beard");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Barba");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Barbe");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Bizarra");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Haircut lady");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corte de cabelo senhora");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coupe de cheveux femmes");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Emakumeen ile mozketa");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Hairstyle lady");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Penteado senhora");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coiffure femmes");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Emakumeen orrazkera");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dye lady");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corante senhora");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Colorant femmes");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Emakumeen tindatzea");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Wicks lady");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Mechas senhora");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Wicks femmes");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Emakumeen metxak");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Scrape");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Raspar o cabelo");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Grattez les cheveux");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Ilea Desugertu");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Extensions");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Extensões");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Extensions");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Luzapenak");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Wash hair");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Lavar o cabelo");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Laver les cheveux");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Ilea garbitu");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Haircut baby");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corte de cabelo criança");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coupe de cheveux enfant");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Umearen ile mozketa");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Hairstyle baby");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Penteado criança");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coiffure enfant");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Umearen orrazkera");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Manicure");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Manicure");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Manucure");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Manikura");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Pedicure");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Pedicure");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Pédicure");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Pedikuro");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Depilation lady");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Depilação senhora");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Epilation femmes");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Emakumeen depilazioa");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Depilation gentleman");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Depilação cabellero");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Epilation homme");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gizonentzat depilazioa");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Skin");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Pele");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Complexion");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Aurpegiko larruazala");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Eyebrows or lip");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Sobrancelhas ou lábio");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Sourcils ou lèvre");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Bekain edo ezpain");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Makeup");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Maquiagem");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Maquillage");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Makillajea");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Crotches or armpits");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Virilha ou axilas");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Anglais ou aisselles");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Izterondoak edo besapeak");
		multiTextDAO.create(nameMulti);
		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Eyelashes");
		multiTextDAO.create(nameMulti);
		
		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Pestanas");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Cils");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Betileal");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);
		
		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Bride");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Noiva");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Jeune mariée");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Emaztegaia");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Bridegroom");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Noivo");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Jeune marié");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Senargaia");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Godmother");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Madrinha");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Marraine");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Amabitxia");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Godfather");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Padrinho");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Parrain");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Aitabitxia");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Invited lady");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Convidado senhora");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Invité femmes");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gonbidatua emakumea");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Invited gentleman");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Convidado cabellero");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Invité homme");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Gonbidatua gizona");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Communion");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Comunhão");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Communion");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Jaunartzea");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Accessories");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Acessórios");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Accessoires");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Osagarriak");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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

		respon = professionalDAO.create(respon);
		firm.setFirRespon(respon);

		WhereDTO where = new WhereDTO();

		where.setEnabled(1);
		where.setWheAddress(firAddress);
		where.setWheCity(firCity);
		where.setWheState(firState);
		where.setWheCP(firCP);
		where.setWheCountry(firCountry);

		where = whereDAO.create(where);
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

		List<TaskClassDTO> firClassTasks = taskClassDAO.getTaskClass();
		List<Long> firClassTasksId = new ArrayList<Long>();
		for (TaskClassDTO taskClass : firClassTasks) {
			firClassTasksId.add(taskClass.getId());
		}

		firm.setFirClassTasks(firClassTasksId);

		firm.setEnabled(1);
		firm.setFirName(firName);
		firm.setFirDomain(firDomain);

		firm = firmDAO.create(firm);

		where.setResFirId(firm.getId());
		whereDAO.update(where);

		respon.setResFirId(firm.getId());
		professionalDAO.update(respon);

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

		where = whereDAO.create(where);

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

		DiaryDTO diaryCreatedMon = diaryDAO.create(diary);
		DiaryDTO diaryCreatedTue = diaryDAO.create(diary);
		DiaryDTO diaryCreatedWed = diaryDAO.create(diary);
		DiaryDTO diaryCreatedThu = diaryDAO.create(diary);
		DiaryDTO diaryCreatedFri = diaryDAO.create(diary);

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

		DiaryDTO diaryCreatedSat = diaryDAO.create(diary);
		semanalDiary.setSemSatDiary(diaryCreatedSat);

		diaTimes = new ArrayList<String>();
		diary.setDiaTimes(diaTimes);
		DiaryDTO diaryCreatedSun = diaryDAO.create(diary);
		semanalDiary.setSemSunDiary(diaryCreatedSun);

		semanalDiary = semanalDiaryDAO.create(semanalDiary);

		local.setLocSemanalDiary(semanalDiary);

		List<LangDTO> locLangs = new ArrayList<LangDTO>();
		locLangs.add(langDAO.getByCode("es"));
		locLangs.add(langDAO.getByCode("en"));
		locLangs.add(langDAO.getByCode("pt"));
		locLangs.add(langDAO.getByCode("fr"));
		locLangs.add(langDAO.getByCode("eu"));

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

		respon = professionalDAO.create(respon);
		local.setLocRespon(respon);
		
		local = localDAO.create(local);

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

		diaryCreatedMon = diaryDAO.create(diary);
		diaryCreatedTue = diaryDAO.create(diary);
		diaryCreatedWed = diaryDAO.create(diary);
		diaryCreatedThu = diaryDAO.create(diary);
		diaryCreatedFri = diaryDAO.create(diary);

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
		diaryCreatedSat = diaryDAO.create(diary);
		semanalDiary.setSemSatDiary(diaryCreatedSat);

		diaTimes = new ArrayList<String>();
		diary.setDiaTimes(diaTimes);
		diaryCreatedSun = diaryDAO.create(diary);
		semanalDiary.setSemSunDiary(diaryCreatedSun);

		semanalDiary = semanalDiaryDAO.create(semanalDiary);

		CalendarDTO calendar = new CalendarDTO();
		calendar.setEnabled(1);
		calendar.setResFirId(firm.getId());
		calendar.setCalName(calName);
		calendar.setCalDesc(calDesc);
		calendar.setCalLocalId(local.getId());
		calendar.setCalLocalTasksId(new ArrayList<Long>());
		calendar.setCalSemanalDiary(semanalDiary);

		calendarDAO.create(calendar);

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

		diaryCreatedMon = diaryDAO.create(diary);
		diaryCreatedTue = diaryDAO.create(diary);
		diaryCreatedWed = diaryDAO.create(diary);
		diaryCreatedThu = diaryDAO.create(diary);
		diaryCreatedFri = diaryDAO.create(diary);

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
		diaryCreatedSat = diaryDAO.create(diary);
		semanalDiary.setSemSatDiary(diaryCreatedSat);

		diaTimes = new ArrayList<String>();
		diary.setDiaTimes(diaTimes);
		diaryCreatedSun = diaryDAO.create(diary);
		semanalDiary.setSemSunDiary(diaryCreatedSun);

		semanalDiary = semanalDiaryDAO.create(semanalDiary);

		calendar.setCalSemanalDiary(semanalDiary);

		calendarDAO.create(calendar);

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

		where = whereDAO.create(where);

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

		diaryCreatedMon = diaryDAO.create(diary);
		diaryCreatedTue = diaryDAO.create(diary);
		diaryCreatedWed = diaryDAO.create(diary);
		diaryCreatedThu = diaryDAO.create(diary);
		diaryCreatedFri = diaryDAO.create(diary);

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
		diaryCreatedSat = diaryDAO.create(diary);
		semanalDiary.setSemSatDiary(diaryCreatedSat);

		diaTimes = new ArrayList<String>();
		diary.setDiaTimes(diaTimes);
		diaryCreatedSun = diaryDAO.create(diary);
		semanalDiary.setSemSunDiary(diaryCreatedSun);

		semanalDiary = semanalDiaryDAO.create(semanalDiary);

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

		respon = professionalDAO.create(respon);
		local.setLocRespon(respon);
		
		local = localDAO.create(local);

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

		diaryCreatedMon = diaryDAO.create(diary);
		diaryCreatedTue = diaryDAO.create(diary);
		diaryCreatedWed = diaryDAO.create(diary);
		diaryCreatedThu = diaryDAO.create(diary);
		diaryCreatedFri = diaryDAO.create(diary);

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
		diaryCreatedSat = diaryDAO.create(diary);
		semanalDiary.setSemSatDiary(diaryCreatedSat);

		diaTimes = new ArrayList<String>();
		diary.setDiaTimes(diaTimes);
		diaryCreatedSun = diaryDAO.create(diary);
		semanalDiary.setSemSunDiary(diaryCreatedSun);

		semanalDiary = semanalDiaryDAO.create(semanalDiary);

		calendar.setEnabled(1);
		calendar.setResFirId(firm.getId());
		calendar.setCalName(calName);
		calendar.setCalDesc(calDesc);
		calendar.setCalLocalId(local.getId());
		calendar.setCalLocalTasksId(new ArrayList<Long>());

		calendar.setCalSemanalDiary(semanalDiary);

		calendarDAO.create(calendar);

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

		diaryCreatedMon = diaryDAO.create(diary);
		diaryCreatedTue = diaryDAO.create(diary);
		diaryCreatedWed = diaryDAO.create(diary);
		diaryCreatedThu = diaryDAO.create(diary);
		diaryCreatedFri = diaryDAO.create(diary);

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
		diaryCreatedSat = diaryDAO.create(diary);
		semanalDiary.setSemSatDiary(diaryCreatedSat);

		diaTimes = new ArrayList<String>();
		diary.setDiaTimes(diaTimes);
		diaryCreatedSun = diaryDAO.create(diary);
		semanalDiary.setSemSunDiary(diaryCreatedSun);

		semanalDiary = semanalDiaryDAO.create(semanalDiary);

		calendar.setCalSemanalDiary(semanalDiary);

		calendarDAO.create(calendar);

	}
	
	protected void newLocalTask(Long localId, Long lotTaskId,
			String strLotTaskDuration, String strLotTaskPost, 
			String strLotTaskRate, boolean setLocalDefault, String keyNameMulti,
			Map<String, String> hashNamesParam) throws Exception {

		LocalTaskDTO localTask = new LocalTaskDTO();

		LocalDTO local = localDAO.getById(localId);

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
			multiTextDAO.create(nameMulti);
		}

		localTask.setEnabled(1);
		localTask.setLotLocalId(localId);
		localTask.setLotNameMulti(keyNameMulti);
		localTask.setLotTaskId(lotTaskId);
		localTask.setLotTaskDuration(lotTaskDuration);
		localTask.setLotTaskPost(lotTaskPost);
		localTask.setLotTaskRate(lotTaskRate);
		localTask.setLotVisible(1);
		
		localTask = localTaskDAO.create(localTask);

		// localTask.setEnabled(1);
		// localTask.setLotLocalId(localId);
		List<Long> lotTaskCombiId = new ArrayList<Long>();
		List<Integer> lotTaskCombiRes = new ArrayList<Integer>();
		localTask.setLotTaskCombiId(lotTaskCombiId);
		localTask.setLotTaskCombiRes(lotTaskCombiRes);
		localTask = localTaskDAO.create(localTask);

		if (setLocalDefault) {
			localTask.setLotDefault(1);
			local.setLocTaskDefaultId(localTask.getId());
			localDAO.update(local);
		}

	}

	protected void newLocalTaskMulti(Long localId, List<Long> lotTaskCombiId,
			List<Integer> lotTaskCombiRes, boolean setLocalDefault)
			throws Exception {

		LocalTaskDTO localTask = new LocalTaskDTO();

		LocalDTO local = localDAO.getById(localId);

		localTask.setEnabled(1);
		localTask.setLotLocalId(localId);
		localTask.setLotTaskCombiId(lotTaskCombiId);
		localTask.setLotTaskCombiRes(lotTaskCombiRes);
		localTask.setLotVisible(1);
		localTask = localTaskDAO.create(localTask);

		if (setLocalDefault) {
			localTask.setLotDefault(1);
			local.setLocTaskDefaultId(localTask.getId());
			localDAO.update(local);
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
		Long lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Peinado comunión");
		hashNamesParam.put("en", "Hairstyle communion");
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_baby";
		newLocalTask(localId, lotTaskId, "25", "0", "18", false, keyNameMulti,
				hashNamesParam);

		// Tinte señora
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_dye_lady";
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Lavar pelo");
		hashNamesParam.put("en", "Wash hair");
		keyNameMulti = keyNameMultiBase + "hairdresser_wash";
		newLocalTask(localId, lotTaskId, "10", "0", "6", false, keyNameMulti,
				hashNamesParam);

		// Manicura
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "beauty_manicure";
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		List<Integer> lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(20);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(20);
		lotTaskCombiRes.add(0);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(20);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(60);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(60);
		lotTaskCombiRes.add(0);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(60);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(0);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_lady";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_hairstyle_lady_molded";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(0);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "beauty_eyebrows";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "beauty_lip";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(0);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "beauty_manicure";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "beauty_pedicure";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(0);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "beauty_crotches";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "beauty_armpits";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
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
		Long lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Corte de pelo niño (hasta 8 años)");
		hashNamesParam.put("en", "Haircut child (up to 8 years)");
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_child";
		newLocalTask(localId, lotTaskId, "15", "0", "9", false, keyNameMulti,
				hashNamesParam);

		// Tinte caballero
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_dye_gentleman";
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

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
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Mechas caballero");
		hashNamesParam.put("en", "Hair wicks gentleman");
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_gentleman";
		newLocalTask(localId, lotTaskId, "40", "40", "30", false, keyNameMulti,
				hashNamesParam);

		// Barba
		taskMulti = MultiTextManager.KEY_MULTI_SYSTEM
				+ TaskManager.KEY_MULTI_TASK_NAME + "hairdresser_beard";
		lotTaskId = (taskDAO.getByName(taskMulti)).getId();

		hashNamesParam = new HashMap<String, String>();
		hashNamesParam.put("es", "Barba");
		hashNamesParam.put("en", "Beard");
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_beard";
		newLocalTask(localId, lotTaskId, "40", "0", "5", false, keyNameMulti,
				hashNamesParam);

		// Combinados

		List<Long> lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_dye_gentleman";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_gentleman";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		List<Integer> lotTaskCombiRes = new ArrayList<Integer>();
		lotTaskCombiRes.add(20);
		newLocalTaskMulti(localId, lotTaskCombiId, lotTaskCombiRes, false);

		lotTaskCombiId = new ArrayList<Long>();
		keyNameMulti = keyNameMultiBase + "hairdresser_wicks_gentleman";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
		lotTaskCombiId.add(lotTaskId);
		keyNameMulti = keyNameMultiBase + "hairdresser_haircut_gentleman";
		lotTaskId = (localTaskDAO.getByName(keyNameMulti)).getId();
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

		List<LocalTaskDTO> localTaskList = localTaskDAO.getLocalTaskSimpleInv(
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

		CalendarDTO calendar = calendarDAO.getById(new Long(calId));

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
			FirmDTO firm = firmDAO.getFirmDomainAdmin(domain);
			List<LocalDTO> localList = localDAO.getLocalAdmin(firm.getId());
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
			LocalDTO local = localDAO.getById(localId);

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
		List<FirmDTO> firms = firmDAO.getFirmAdmin();
		for (FirmDTO firm : firms) {
			listLocal = localDAO.getLocalAdmin(firm.getId());
			for (LocalDTO local : listLocal) {
				list = localTaskDAO.getLocalTaskAdmin(local.getId(),
						locale.getLanguage());
				for (LocalTaskDTO localTask : list) {
					if (localTask.getLotVisible() == null) {
						localTask.setLotVisible(1);
						localTaskDAO.update(localTask);
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
		List<FirmDTO> firms = firmDAO.getFirmAdmin();
		for (FirmDTO firm : firms) {
			listLocal = localDAO.getLocalAdmin(firm.getId());
			for (LocalDTO local : listLocal) {
				list = localTaskDAO.getLocalTaskAdmin(local.getId(), locale.getLanguage());
				for (LocalTaskDTO localTaskSearch : list) {
					List<Long> combi = localTaskSearch.getLotTaskCombiId();
					if (combi!=null && combi.size()>0){
						List<Integer> combiRes = localTaskSearch.getLotTaskCombiRes();
						int indx = 0;
						int taskPost = 0;
						for (Long localTaskComId : combi) {
							if (indx<combiRes.size()){
								LocalTaskDTO localTask = localTaskDAO.getById(localTaskComId);
								if (localTask.getLotTaskPost() == null && combiRes.get(indx)>0) {
									taskPost = combiRes.get(indx);
									localTask.setLotTaskPost(taskPost);
									localTaskDAO.update(localTask);
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
		List<FirmDTO> firms = firmDAO.getFirmAdmin();
		for (FirmDTO firm : firms) {
			listLocal = localDAO.getLocalAdmin(firm.getId());
			for (LocalDTO local : listLocal) {
				listCalendar = calendarDAO.getCalendarAdmin(local.getId());
				for (CalendarDTO calendar : listCalendar) {
					list = eventDAO.getEventAdmin(calendar);
					for (EventDTO event : list) {
						if (event.getEveEndTime() == null) {
							LocalTaskDTO localTask = event.getEveLocalTask();
							Calendar calendarGreg = new GregorianCalendar();
							calendarGreg.setTime(event.getEveStartTime());
							calendarGreg.add(Calendar.MINUTE,
									localTask.getLotTaskDuration());
							event.setEveEndTime(calendarGreg.getTime());
							eventDAO.update(event);
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
		return localTaskDAO.getById(id);
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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Sport");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Esporte");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Sport");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Kirola");
		multiTextDAO.create(nameMulti);

		taskClassSport.setTclNameMulti(nameKey);

		taskClassSport = taskClassDAO.create(taskClassSport);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Physio");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Fisio");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Physio");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Fisio");
		multiTextDAO.create(nameMulti);

		taskClassPhysio.setTclNameMulti(nameKey);

		taskClassPhysio = taskClassDAO.create(taskClassPhysio);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Psychology");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Psicologia");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Psychologie");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Psikologia");
		multiTextDAO.create(nameMulti);

		taskClassPsychology.setTclNameMulti(nameKey);

		taskClassPsychology = taskClassDAO.create(taskClassPsychology);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Canine");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Canino");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Canin");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakurrena");
		multiTextDAO.create(nameMulti);

		taskClassCanine.setTclNameMulti(nameKey);

		taskClassCanine = taskClassDAO.create(taskClassCanine);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Chiropody");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Quiropodia");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Podologie");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Podologia");
		multiTextDAO.create(nameMulti);

		taskClassChiropody.setTclNameMulti(nameKey);

		taskClassChiropody = taskClassDAO.create(taskClassChiropody);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dentist");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Dentista");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Dentiste");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Dentista");
		multiTextDAO.create(nameMulti);

		taskClassDentist.setTclNameMulti(nameKey);

		taskClassDentist = taskClassDAO.create(taskClassDentist);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Tennis");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Tênis");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Tennis");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Tenisa");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Padel");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Padel");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Padel");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Padela");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Physiotherapy - Osteopathy");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Fisioterapia - Osteopatia");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Physiothérapie - Ostéopathie");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Fisioterapia - Osteopatía");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cryotherapy");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Crioterapia");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Cryothérapie");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Crioterapia");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Psychotherapy");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Psicoterapia");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Psychothérapie");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Psikoterapia");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Coaching");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Coaching");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coaching");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Coaching");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dog haircut");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corte de cabelo cão");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coupe de cheveux chien");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakurreko Ile mozketa");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cat haircut");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Corte de cabelo gato");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Coupe de cheveux chat");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Katuko Ile mozketa");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dog wash");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Lavagem do cão");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Lavage de chien");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakur-garbiketa");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cat wash");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Lavagem do gato");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Lavage de chat");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Katu-garbiketa");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dog brushing");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Escovação do cão");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Brossage de chien");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakur-eskuilatzea");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cat brushing");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Escovação do gato");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Brossage de chat");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Katu-eskuilatzea");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dog deworming");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Desparasitação do cão");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Déparasitage de chien");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakur-desparasitación-a");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cat deworming");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Desparasitação do gato");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Déparasitage de chat");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Katu-desparasitación-a");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dog treatments");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Tratamentos do cão");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Traitements de chien");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakur-tratamenduak");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cat treatments");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Tratamentos do gato");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Traitements de chat");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Katu-tratamenduak");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Dog stripping");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Stripping do cão");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Stripping de chien");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txakur-stripping");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Cat stripping");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Stripping do gato");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Stripping de chat");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Katu-stripping");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Biomechanics");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Biomecânica");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Biomécanique");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Biomekanika");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Templates");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Templates");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Modèles");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Txantiloiak");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Foot pathology");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Patologia do pé");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Pathologie de pied");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Oinaren patologia");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Foot surgery");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Cirurgia do pé");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Chirurgie de pied");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Oinaren kirurgia");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Foot aesthetics");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Estética do pé");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Esthétique de pied");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Oinaren estetika");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);
		
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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Consultation");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Consulta");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Consultation");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Kontsulta");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Oral cleaning");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Limpeza bucal");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Nettoyage buccal");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Aho-garbiketa");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Filling");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Enchimento");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Remplissage");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Enpastea");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Endodontics");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Endodontia");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Endodontie");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Endodontzia");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Surgery");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Cirurgia");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Chirurgie");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Kirurgia");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Periodontics");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Periodontia");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Parodontie");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Periodoncia");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Orthodontics");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Ortodontia");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Orthodontie");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Ortodontzia");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Whitening");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Branqueamento");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Blanchiment");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Blanqueamiento-a");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Implants");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Implantes");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Implants");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Inplanteak");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Prosthesis or sleeves");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Prótese ou mangas");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Prothèse ou manches");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Protesia edo zorroak");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);

		
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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Goods");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Mercadorias");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Marchandises");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Salgaia");
		multiTextDAO.create(nameMulti);

		taskClassGoods.setTclNameMulti(nameKey);

		taskClassGoods = taskClassDAO.create(taskClassGoods);


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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Unloading goods");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Descarga de mercadorias");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Marchandises de déchargement");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Salgai-deskarga");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);
		
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
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("en");
		nameMulti.setMulText("Loading goods");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("pt");
		nameMulti.setMulText("Carga de mercadorias");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("fr");
		nameMulti.setMulText("Marchandises de chargement");
		multiTextDAO.create(nameMulti);

		nameMulti = new MultiTextDTO();
		nameMulti.setEnabled(1);
		nameMulti.setMulKey(nameKey);
		nameMulti.setMulLanCode("eu");
		nameMulti.setMulText("Salgai-karga");
		multiTextDAO.create(nameMulti);

		task.setTasNameMulti(nameKey);

		taskDAO.create(task);
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/admin/createAdveoUsers_no")
	@ResponseStatus(HttpStatus.OK)
	protected void createAdveoUsers(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		//String domain = arg0.getParameter("adveo");
		String domain = "adveo";
		FirmDTO firm = firmDAO.getFirmDomainAdmin(domain);
		
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
			clientDAO.create(client);

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
			clientDAO.create(client);
			
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
			clientDAO.create(client);
			
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

			firm = firmDAO.update(firm);
			
		}
	}

	public void setMessageSourceApp(MessageSource messageSourceApp) {
		this.messageSourceApp = messageSourceApp;
	}

	public void setGeneratorVelocity(Generator generatorVelocity) {
		this.generatorVelocity = generatorVelocity;
	}

	public void setLocalDAO(LocalDAO localDAO) {
		this.localDAO = localDAO;
	}

	public void setMultiTextDAO(MultiTextDAO multiTextDAO) {
		this.multiTextDAO = multiTextDAO;
	}

	public void setProfessionalDAO(ProfessionalDAO professionalDAO) {
		this.professionalDAO = professionalDAO;
	}

	public void setLangDAO(LangDAO langDAO) {
		this.langDAO = langDAO;
	}

	public void setWhereDAO(WhereDAO whereDAO) {
		this.whereDAO = whereDAO;
	}

	public void setTaskDAO(TaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	public void setTaskClassDAO(TaskClassDAO taskClassDAO) {
		this.taskClassDAO = taskClassDAO;
	}

	public void setSemanalDiaryDAO(SemanalDiaryDAO semanalDiaryDAO) {
		this.semanalDiaryDAO = semanalDiaryDAO;
	}

	public void setLocalTaskDAO(LocalTaskDAO localTaskDAO) {
		this.localTaskDAO = localTaskDAO;
	}

	public void setSincroDAO(SincroDAO sincroDAO) {
		this.sincroDAO = sincroDAO;
	}

	public void setUserRegistry(UserRegistry userRegistry) {
		this.userRegistry = userRegistry;
	}
	
	
}
