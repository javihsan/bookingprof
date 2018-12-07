package com.diloso.bookhair.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dao.DiaryDAO;
import com.diloso.bookhair.app.negocio.dao.FirmDAO;
import com.diloso.bookhair.app.negocio.dao.LangDAO;
import com.diloso.bookhair.app.negocio.dao.LocalDAO;
import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dao.ProfessionalDAO;
import com.diloso.bookhair.app.negocio.dao.SemanalDiaryDAO;
import com.diloso.bookhair.app.negocio.dao.WhereDAO;
import com.diloso.bookhair.app.negocio.dto.DiaryDTO;
import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.dto.LangDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.negocio.dto.WhereDTO;

@Controller
@RequestMapping(value={"/*/local", "/local"})
public class LocalController {
	
	protected static final Logger log = Logger.getLogger(LocalController.class.getName());
	
	@Autowired
	protected MessageSource messageSourceApp;
		
	@Autowired
	protected LocalDAO localDAO;
	
	@Autowired
	protected FirmDAO firmDAO;
	
	@Autowired
	protected ProfessionalDAO professionalDAO;
	
	@Autowired
	protected LangDAO langDAO;
	
	@Autowired
	protected MultiTextDAO multiTextDAO;
	
	@Autowired
	protected WhereDAO whereDAO;
	
	@Autowired
	protected DiaryDAO diaryDAO;
	
	@Autowired
	protected SemanalDiaryDAO semanalDiaryDAO;
	
	@RequestMapping(method = RequestMethod.POST, value = "/admin/new")
	@ResponseStatus(HttpStatus.OK)
	protected void newObject(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		String domain = arg0.getParameter("domain");
		FirmDTO firm = firmDAO.getFirmDomainAdmin(domain);
		Long resFirId = firm.getId();
		
		String locName = arg0.getParameter("locName");
		
		String locAddress = arg0.getParameter("locAddress");
		String locCity = arg0.getParameter("locCity");
		String locState = arg0.getParameter("locState");
		String locCP = arg0.getParameter("locCP");
		String locCountry = "España";//arg0.getParameter("locCountry");
		String locTimeZone = "Europe/Madrid"/*America/New_York*/;//arg0.getParameter("locTimeZone");
		String locGoogleHoliday = "es.spain";//arg0.getParameter("locGoogleHoliday");
		String locCurrency = "€";//arg0.getParameter("locCurrency");
		
		int locBookingClient = 1;
		
		String locResponName = arg0.getParameter("locResponName");
		String locResponSurname = arg0.getParameter("locResponSurname");
		String locResponEmail = arg0.getParameter("locResponEmail");
		String locResponTelf1 = arg0.getParameter("locResponTelf1");
		
	    int locTimeRestricted = Integer.parseInt(arg0.getParameter("locTimeRestricted"));
	    int locApoDuration = Integer.parseInt(arg0.getParameter("locApoDuration"));
	    int locOpenDays = Integer.parseInt(arg0.getParameter("locOpenDays"));
	    int locNumPersonsApo = Integer.parseInt(arg0.getParameter("locNumPersonsApo"));
	    int locMulServices = Integer.parseInt(arg0.getParameter("locMulServices"));
	    int locCacheTasks = Integer.parseInt(arg0.getParameter("locCacheTasks"));
	    int locSelCalendar = Integer.parseInt(arg0.getParameter("locSelCalendar"));
	    int locNumUsuDays = Integer.parseInt(arg0.getParameter("locNumUsuDays"));
		
	    if (firm.getFirConfig().getConfigLocal().getConfigLocNumPer() == 0){
	    	locNumPersonsApo = 1;
	    }
	    if (firm.getFirConfig().getConfigLocal().getConfigLocMulSer() == 0){
		    locMulServices = 0;
	    }
	    if (firm.getFirConfig().getConfigLocal().getConfigLocSelCal() == 0){
	    	locSelCalendar = 0;
	    }
	    
		LocalDTO local = new LocalDTO();

		WhereDTO where = new WhereDTO();
		
		where.setEnabled(1);
		where.setResFirId(resFirId);
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

		ProfessionalDTO respon = new ProfessionalDTO();

		respon.setResFirId(resFirId);
		respon.setEnabled(1);
		respon.setWhoName(locResponName);
		respon.setWhoSurname(locResponSurname);
		respon.setWhoEmail(locResponEmail);
		respon.setWhoTelf1(locResponTelf1);
	
		respon = professionalDAO.create(respon);
		local.setLocRespon(respon);
		
		local.setLocMailBookign(0);
		
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
		
		local.setLocLangs(locLangs);
		
		local.setEnabled(1);
		local.setLocBookingClient(locBookingClient);
		local.setResFirId(resFirId);
		local.setLocNewClientDefault(1);
		local.setLocName(locName);
		local.setLocApoDuration(locApoDuration);
		local.setLocTimeRestricted(locTimeRestricted);
		local.setLocOpenDays(locOpenDays);
		local.setLocNumPersonsApo(locNumPersonsApo);
		local.setLocMulServices(locMulServices);
		local.setLocSelCalendar(locSelCalendar);
		local.setLocNumUsuDays(locNumUsuDays);
		local.setLocCacheTasks(locCacheTasks);
		//local.setLocSinGCalendar(null);
		//local.setLocSinMChimp(null);
		
		localDAO.create(local);
		
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/update")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	protected LocalDTO update(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		String localId = arg0.getParameter("localId");
		LocalDTO local = localDAO.getById(new Long(localId));
		FirmDTO firm = firmDAO.getById(local.getResFirId());
		
		int locBookingClient = Integer.parseInt(arg0.getParameter("locBookingClient"));
		
		String locName = arg0.getParameter("locName");
		
		String locAddress = arg0.getParameter("locAddress");
		String locCity = arg0.getParameter("locCity");
		String locState = arg0.getParameter("locState");
		String locCP = arg0.getParameter("locCP");
		/*String locCountry = arg0.getParameter("locCountry");
		String locTimeZone = arg0.getParameter("locTimeZone");
		String locGoogleHoliday = arg0.getParameter("locGoogleHoliday");
		String locCurrency =  arg0.getParameter("locCurrency");*/
		
		String locResponName = arg0.getParameter("locResponName");
		String locResponSurname = arg0.getParameter("locResponSurname");
		String locResponEmail = arg0.getParameter("locResponEmail");
		String locResponTelf1 = arg0.getParameter("locResponTelf1");
		
		int locMailBookign = Integer.parseInt(arg0.getParameter("locMailBookign"));
		
	    int locTimeRestricted = Integer.parseInt(arg0.getParameter("locTimeRestricted"));
	    int locApoDuration = Integer.parseInt(arg0.getParameter("locApoDuration"));
	    int locOpenDays = Integer.parseInt(arg0.getParameter("locOpenDays"));
	    int locNumPersonsApo = Integer.parseInt(arg0.getParameter("locNumPersonsApo"));
	    int locMulServices = Integer.parseInt(arg0.getParameter("locMulServices"));
	    int locSelCalendar = Integer.parseInt(arg0.getParameter("locSelCalendar"));
	    int locNumUsuDays = Integer.parseInt(arg0.getParameter("locNumUsuDays"));
	    int locNewClientDefault = Integer.parseInt(arg0.getParameter("locNewClientDefault"));
		
	    if (firm.getFirConfig().getConfigLocal().getConfigLocNumPer() == 0){
	    	locNumPersonsApo = 1;
	    }
	    if (firm.getFirConfig().getConfigLocal().getConfigLocMulSer() == 0){
		    locMulServices = 0;
	    }
	    if (firm.getFirConfig().getConfigLocal().getConfigLocSelCal() == 0){
	    	locSelCalendar = 0;
	    }
	    
		WhereDTO where = local.getLocWhere();
		
		where.setWheAddress(locAddress);
		where.setWheCity(locCity);
		where.setWheState(locState);
		where.setWheCP(locCP);
		/*where.setWheCountry(locCountry);
		where.setWheTimeZone(locTimeZone);
		where.setWheGoogleHoliday(locGoogleHoliday);
		where.setWheCurrency(locCurrency);*/
		
		where = whereDAO.update(where);
		
		ProfessionalDTO respon = local.getLocRespon();
		
		respon.setWhoName(locResponName);
		respon.setWhoSurname(locResponSurname);
		respon.setWhoEmail(locResponEmail);
		respon.setWhoTelf1(locResponTelf1);
		
		respon = professionalDAO.update(respon);
		
		local.setLocRespon(respon);
		
		local.setLocMailBookign(locMailBookign);

		local.setLocBookingClient(locBookingClient);
		local.setLocName(locName);
		local.setLocApoDuration(locApoDuration);
		local.setLocTimeRestricted(locTimeRestricted);
		local.setLocOpenDays(locOpenDays);
		local.setLocNumPersonsApo(locNumPersonsApo);
		local.setLocNumUsuDays(locNumUsuDays);
		local.setLocMulServices(locMulServices);
		local.setLocSelCalendar(locSelCalendar);
		local.setLocNewClientDefault(locNewClientDefault);
		
		return localDAO.update(local);
		
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/changeLangs")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	protected LocalDTO changeLangs(HttpServletRequest arg0, @RequestParam("localId") String localId, @RequestParam("selectedLangs") String selectedLangs)
			throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		LocalDTO local = localDAO.getById(new Long(localId));
		List<String> langsCode = new ArrayList<String>();
		for (LangDTO langActive : local.getLocLangs()) {
			langsCode.add(langActive.getLanCode());	
		}
		
		String[] a = selectedLangs.split(",");
		List<LangDTO> locLangs = new ArrayList<LangDTO>();

		// Obtenemos todos los multitext del local para idioma actual
		List<MultiTextDTO> listMultiDefault = multiTextDAO.getMultiTextByLanCode(locale.getLanguage(), local.getId());
		for (String lanCode : a) {
			LangDTO lang = langDAO.getByCode(lanCode);
			// Si no estaba habilitado, actualizamos los textos del local para este idioma
			if (!langsCode.contains(lanCode)){
				for (MultiTextDTO multiTextDefault : listMultiDefault) {
					MultiTextDTO multiTextKey = multiTextDAO.getByLanCodeAndKey(lang.getLanCode(),multiTextDefault.getMulKey());
					if (multiTextKey==null){ // Si no existe en el nuevo idioma, insertar con nuevo idioma y el texto del idioma actual
						multiTextKey = new MultiTextDTO();
						multiTextKey.setEnabled(1);
						multiTextKey.setMulKey(multiTextDefault.getMulKey());
						multiTextKey.setMulLanCode(lang.getLanCode());
						multiTextKey.setMulText(multiTextDefault.getMulText());
						multiTextDAO.create(multiTextKey);
					}
				}
			}
			locLangs.add(lang);
		}
		local.setLocLangs(locLangs);
		
		return localDAO.update(local);

	}
	
	@RequestMapping("/get")
	protected @ResponseBody
	LocalDTO get(@RequestParam("id") String id) throws Exception {

		LocalDTO local = localDAO.getById(new Long(id));
		if (local.getEnabled()==1){
			return local;
		} else {
			return null;
		}
	}
	
	@RequestMapping("/getClient")
	protected @ResponseBody
	LocalDTO getClient(@RequestParam("id") String id) throws Exception {

		LocalDTO local = localDAO.getById(new Long(id));
		if (local.getEnabled()!=null && local.getEnabled()==1 && local.getLocBookingClient()==1){
			return local;
		} else {
			return null;
		}
	}
	
	@RequestMapping("/list")
	protected @ResponseBody
	List<Long> list(@RequestParam("domain") String domain) throws Exception {

		FirmDTO firm = firmDAO.getFirmDomain(domain);
		
		return localDAO.getLocal(firm.getId());

	}

	@RequestMapping("/listClient")
	protected @ResponseBody
	List<Long> listClient(@RequestParam("domain") String domain) throws Exception {

		FirmDTO firm = firmDAO.getFirmDomain(domain);
		
		return localDAO.getLocalClient(firm.getId());

	}
	
	@RequestMapping("/listAll")
	protected @ResponseBody
	List<LocalDTO> listAll(@RequestParam("domain") String domain) throws Exception {

		FirmDTO firm = firmDAO.getFirmDomain(domain);
		
		return localDAO.getLocalList(firm.getId());

	}

	@RequestMapping("/listAllClient")
	protected @ResponseBody
	List<LocalDTO> listAllClient(@RequestParam("domain") String domain) throws Exception {

		FirmDTO firm = firmDAO.getFirmDomain(domain);
		
		return localDAO.getLocalListClient(firm.getId());

	}
	
	@RequestMapping("/admin/list")
	protected @ResponseBody
	List<LocalDTO> listAdmin(@RequestParam("domain") String domain) throws Exception {

		FirmDTO firm = firmDAO.getFirmDomainAdmin(domain);
		
		List<LocalDTO> listLocal = localDAO.getLocalAdmin(firm.getId());
		return listLocal;
	}
	
	@RequestMapping("/admin/listLangsLocal")
	public @ResponseBody
	List<LangDTO> listLangsLocal(@RequestParam("localId") String localId) throws Exception {

		LocalDTO local = localDAO.getById(new Long(localId));
		return local.getLocLangs();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/admin/enabled")
	@ResponseStatus(HttpStatus.OK)
	public void enabled(@RequestParam("id") Long id)
			throws Exception {
		LocalDTO local = localDAO.getById(id);

		if (local!=null){
			if (local.getEnabled()==1){
				local.setEnabled(0);
				log.info("Local deshabilitado : "+local.getLocName());
			} else {
				local.setEnabled(1);
				log.info("Local habilitado : "+local.getLocName());
			}
			localDAO.update(local);
		}	
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/defaultLocalTask")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	protected void defaultLocalTask(@RequestParam("localId") Long localId, @RequestParam("idLocalTask") Long idLocalTask)
			throws Exception {
		
		LocalDTO local = localDAO.getById(new Long(localId));
		if (local!=null){
			local.setLocTaskDefaultId(idLocalTask);
			localDAO.update(local);
		}
	}
	
}
