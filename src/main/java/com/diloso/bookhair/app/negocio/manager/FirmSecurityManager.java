package com.diloso.bookhair.app.negocio.manager;

import com.diloso.bookhair.app.negocio.config.impl.ConfigFirm;
import com.diloso.bookhair.app.negocio.utils.ApplicationContextProvider;
import com.diloso.bookhair.app.persist.mapper.FirmSecurityMapper;
import com.diloso.weblogin.aut.FirmSecurityDAO;
import com.google.appengine.api.datastore.Entity;

public class FirmSecurityManager implements IFirmSecurityManager {

	protected static final String FIR_ID = "id";
	protected static final String FIR_CONFIG_NUM = "firConfigNum";
	protected static final String FIR_SERVER = "firServer";
	protected static final String FIR_DOMAIN = "firDomain";
	protected static final String ENABLED = "enabled";
	protected static final Integer FIR_ENABLED_VALUE = Integer.decode("1");
	
	private FirmSecurityDAO firmSecurityDAO = new FirmSecurityDAO();
	
	protected FirmSecurityMapper mapper;
	
	public void setFirmSecurityDAO(FirmSecurityDAO firmSecurityDAO) {
		this.firmSecurityDAO = firmSecurityDAO;
	}
	
	public void setMapper(FirmSecurityMapper mapper) {
		this.mapper = mapper;
	}
	
	public FirmSecurityManager() {
	}

	@Override
	public String getDomainServer(String server) {
		Entity firm = firmSecurityDAO.findFirmServer(server);
		if (firm != null) {
			return (String)firm.getProperty(FIR_DOMAIN);
		}
		return null;
	}

	@Override
	public Long findId(String domain) {
		Entity firm = firmSecurityDAO.findFirmDomain(domain);
		if (firm != null) {
			return firm.getKey().getId();
		}
		return null;
	}

	@Override
	public boolean isRestrictedNivelUser(String domain) {
		boolean result = false;
		Entity firm = firmSecurityDAO.findFirmDomain(domain);
		if (firm!=null) {
			String numConfig = ConfigFirm.IDENT_DEFAULT;
			String firConfigNum = (String) firm.getProperty(FIR_CONFIG_NUM);
			if (firConfigNum != null && !firConfigNum.isEmpty()) {
				ConfigFirm configFirm = (ConfigFirm) ApplicationContextProvider.getApplicationContext().getBean(ConfigFirm.PRE_IDENT_FIRM+numConfig);
				result = configFirm.getConfigAut().getConfigAutNivelUser()==1;
			}
		}
		return result;
	}
	
}