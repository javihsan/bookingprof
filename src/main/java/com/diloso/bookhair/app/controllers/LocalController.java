package com.diloso.bookhair.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

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

import com.diloso.bookhair.app.negocio.dto.DiaryDTO;
import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.dto.LangDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.ProfessionalDTO;
import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.negocio.dto.WhereDTO;
import com.diloso.bookhair.app.negocio.manager.IDiaryManager;
import com.diloso.bookhair.app.negocio.manager.IFirmManager;
import com.diloso.bookhair.app.negocio.manager.ILangManager;
import com.diloso.bookhair.app.negocio.manager.ILocalManager;
import com.diloso.bookhair.app.negocio.manager.IMultiTextManager;
import com.diloso.bookhair.app.negocio.manager.IProfessionalManager;
import com.diloso.bookhair.app.negocio.manager.ISemanalDiaryManager;
import com.diloso.bookhair.app.negocio.manager.IWhereManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value={"/*/local", "/local"})
public class LocalController {
	
	protected static final Logger log = Logger.getLogger(LocalController.class.getName());
	
	@Autowired
	protected MessageSource messageSourceApp;
		
	@Autowired
	protected ILocalManager localManager;
	
	@Autowired
	protected IFirmManager firmManager;
	
	@Autowired
	protected IProfessionalManager professionalManager;
	
	@Autowired
	protected ILangManager langManager;
	
	@Autowired
	protected IMultiTextManager multiTextManager;
	
	@Autowired
	protected IWhereManager whereManager;
	
	@Autowired
	protected IDiaryManager diaryManager;
	
	@Autowired
	protected ISemanalDiaryManager semanalDiaryManager;
	
	@RequestMapping(method = RequestMethod.POST, value = "/admin/new")
	@ResponseStatus(HttpStatus.OK)
	protected void newObject(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		String domain = arg0.getParameter("domain");
		FirmDTO firm = firmManager.getFirmDomainAdmin(domain);
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

		where = whereManager.create(where);
		
		local.setLocWhere(where);

		ProfessionalDTO respon = new ProfessionalDTO();

		respon.setResFirId(resFirId);
		respon.setEnabled(1);
		respon.setWhoName(locResponName);
		respon.setWhoSurname(locResponSurname);
		respon.setWhoEmail(locResponEmail);
		respon.setWhoTelf1(locResponTelf1);
	
		respon = professionalManager.create(respon);
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
		
		localManager.create(local);
		
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/update")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	protected LocalDTO update(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		String localId = arg0.getParameter("localId");
		LocalDTO local = localManager.getById(new Long(localId));
		FirmDTO firm = firmManager.getById(local.getResFirId());
		
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
		
		where = whereManager.update(where);
		
		ProfessionalDTO respon = local.getLocRespon();
		
		respon.setWhoName(locResponName);
		respon.setWhoSurname(locResponSurname);
		respon.setWhoEmail(locResponEmail);
		respon.setWhoTelf1(locResponTelf1);
		
		respon = professionalManager.update(respon);
		
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
		
		return localManager.update(local);
		
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/changeLangs")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	protected LocalDTO changeLangs(HttpServletRequest arg0, @RequestParam("localId") String localId, @RequestParam("selectedLangs") String selectedLangs)
			throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		LocalDTO local = localManager.getById(new Long(localId));
		List<String> langsCode = new ArrayList<String>();
		for (LangDTO langActive : local.getLocLangs()) {
			langsCode.add(langActive.getLanCode());	
		}
		
		String[] a = selectedLangs.split(",");
		List<LangDTO> locLangs = new ArrayList<LangDTO>();

		// Obtenemos todos los multitext del local para idioma actual
		List<MultiTextDTO> listMultiDefault = multiTextManager.getMultiTextByLanCode(locale.getLanguage(), local.getId());
		for (String lanCode : a) {
			LangDTO lang = langManager.getByCode(lanCode);
			// Si no estaba habilitado, actualizamos los textos del local para este idioma
			if (!langsCode.contains(lanCode)){
				for (MultiTextDTO multiTextDefault : listMultiDefault) {
					MultiTextDTO multiTextKey = multiTextManager.getByLanCodeAndKey(lang.getLanCode(),multiTextDefault.getMulKey());
					if (multiTextKey==null){ // Si no existe en el nuevo idioma, insertar con nuevo idioma y el texto del idioma actual
						multiTextKey = new MultiTextDTO();
						multiTextKey.setEnabled(1);
						multiTextKey.setMulKey(multiTextDefault.getMulKey());
						multiTextKey.setMulLanCode(lang.getLanCode());
						multiTextKey.setMulText(multiTextDefault.getMulText());
						multiTextManager.create(multiTextKey);
					}
				}
			}
			locLangs.add(lang);
		}
		local.setLocLangs(locLangs);
		
		return localManager.update(local);

	}
	
	@RequestMapping("/get")
	protected @ResponseBody
	LocalDTO get(@RequestParam("id") String id) throws Exception {
		LocalDTO local = localManager.getById(new Long(id));
		if (local.getEnabled()==1){
			return local;
		} else {
			return null;
		}
	}
	
	@RequestMapping("/getClient")
	protected @ResponseBody
	LocalDTO getClient(@RequestParam("id") String id) throws Exception {

		LocalDTO local = localManager.getById(new Long(id));
		if (local.getEnabled()!=null && local.getEnabled()==1 && local.getLocBookingClient()==1){
			return local;
		} else {
			return null;
		}
	}
	
	@RequestMapping("/list")
	protected @ResponseBody
	List<Long> list(@RequestParam("domain") String domain) throws Exception {

		FirmDTO firm = firmManager.getFirmDomain(domain);

		return localManager.getLocal(firm.getId());

	}

	@RequestMapping("/listClient")
	protected @ResponseBody
	List<Long> listClient(@RequestParam("domain") String domain) throws Exception {

		FirmDTO firm = firmManager.getFirmDomain(domain);
		
		return localManager.getLocalClient(firm.getId());

	}
	
	@RequestMapping("/listAll")
	protected @ResponseBody
	List<LocalDTO> listAll(@RequestParam("domain") String domain) throws Exception {

		FirmDTO firm = firmManager.getFirmDomain(domain);
		
		return localManager.getLocalList(firm.getId());

	}

	@RequestMapping("/listAllClient")
	protected @ResponseBody
	List<LocalDTO> listAllClient(@RequestParam("domain") String domain) throws Exception {

		FirmDTO firm = firmManager.getFirmDomain(domain);
		
		return localManager.getLocalListClient(firm.getId());

	}
	
	@RequestMapping("/admin/list")
	protected @ResponseBody
	List<LocalDTO> listAdmin(@RequestParam("domain") String domain) throws Exception {

		FirmDTO firm = firmManager.getFirmDomainAdmin(domain);
		
		List<LocalDTO> listLocal = localManager.getLocalAdmin(firm.getId());
		return listLocal;
	}
	
	@RequestMapping("/admin/listLangsLocal")
	public @ResponseBody
	List<LangDTO> listLangsLocal(@RequestParam("localId") String localId) throws Exception {

		LocalDTO local = localManager.getById(new Long(localId));
		return local.getLocLangs();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/admin/enabled")
	@ResponseStatus(HttpStatus.OK)
	public void enabled(@RequestParam("id") Long id)
			throws Exception {
		LocalDTO local = localManager.getById(id);

		if (local!=null){
			if (local.getEnabled()==1){
				local.setEnabled(0);
				log.info("Local deshabilitado : "+local.getLocName());
			} else {
				local.setEnabled(1);
				log.info("Local habilitado : "+local.getLocName());
			}
			localManager.update(local);
		}	
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/defaultLocalTask")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	protected void defaultLocalTask(@RequestParam("localId") Long localId, @RequestParam("idLocalTask") Long idLocalTask)
			throws Exception {
		
		LocalDTO local = localManager.getById(new Long(localId));
		if (local!=null){
			local.setLocTaskDefaultId(idLocalTask);
			localManager.update(local);
		}
	}

	public void setMessageSourceApp(MessageSource messageSourceApp) {
		this.messageSourceApp = messageSourceApp;
	}

	public void setLocalDAO(ILocalManager iLocalManager) {
		this.localManager = iLocalManager;
	}

	public void setFirmDAO(IFirmManager iFirmManager) {
		this.firmManager = iFirmManager;
	}

	public void setProfessionalDAO(IProfessionalManager iProfessionalManager) {
		this.professionalManager = iProfessionalManager;
	}

	public void setLangDAO(ILangManager iLangManager) {
		this.langManager = iLangManager;
	}

	public void setMultiTextDAO(IMultiTextManager iMultiTextManager) {
		this.multiTextManager = iMultiTextManager;
	}

	public void setWhereDAO(IWhereManager iWhereManager) {
		this.whereManager = iWhereManager;
	}

	public void setDiaryDAO(IDiaryManager iDiaryManager) {
		this.diaryManager = iDiaryManager;
	}

	public void setSemanalDiaryDAO(ISemanalDiaryManager iSemanalDiaryManager) {
		this.semanalDiaryManager = iSemanalDiaryManager;
	}
		
	
}
