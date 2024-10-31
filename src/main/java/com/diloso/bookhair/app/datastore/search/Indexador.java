package com.diloso.bookhair.app.datastore.search;

import java.util.List;

import com.diloso.bookhair.app.datastore.StringUtils;
import com.diloso.bookhair.app.datastore.UtilidadesData;
import com.diloso.bookhair.app.datastore.data.IStorable;
import com.google.appengine.api.search.DeleteException;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.StatusCode;
import com.google.apphosting.api.ApiProxy;

/**
 * 
 * Solo indexamos los campos por los que buscamos ya que la entidad completa la
 * recuperamos de DStore
 * 
 * Para indexar entidades de IdLong
 * 
 */
public class Indexador<T extends IStorable<?>> implements IIndexador {
    public static final int MAX_ATTEMPTS = 3;
    public static final int INI_ATTEMPT = 0;
    public static final String SEPARATOR_STR_LIST = "-";
    private final List<T> lstBeans;

    private SearchHandler searchHandler;
    IBeanToDocumentMapper<T> mapper;

    public static final String LOG_ERROR_TS = "Error en el acceso a TS. Superado el n�mero de intentos m�ximo ";
    public static final String LOG_ERROR_TS_ATTEMPT = "Reintentando .... Error en el acceso a TS. Intento ";

    private static final String LOG_DELETED_LIST = "Objects with IDs '%s' deleted in TextSearch";
    private static java.util.logging.Logger LOGGER = java.util.logging.Logger
            .getLogger(Indexador.class.getName());

    public Indexador(List<T> beans, IBeanToDocumentMapper<T> mapper,
                     String indice) {
        StringBuilder sb = new StringBuilder();
        if (beans != null) {
            for (T funcionalidad : beans) {
                sb.append(funcionalidad.getId()).append(" - ");
            }
            LOGGER.info("INDEXANDO " + sb.toString());
        }
        this.lstBeans = beans;
        this.searchHandler = new SearchHandler(indice);
        this.mapper = mapper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bbva.mlean.core.funcionalidades.IIndexador#indexarFuncionalidad()
     */
    @Override
    public void indexar() {
        int attempt = INI_ATTEMPT;
        do {
            try {
                this.searchHandler.put(this.lstBeans, this.mapper);
                break;
            } catch (PutException e) {
                if (StatusCode.TRANSIENT_ERROR
                        .equals(e.getOperationResult().getCode())) { //
                    LOGGER.severe(LOG_ERROR_TS_ATTEMPT + attempt);
                    if (attempt == MAX_ATTEMPTS) {
                        UtilidadesData.printException(e.getMessage(), e);
                        throw e;
                    }
                    attempt = UtilidadesData.reintento(attempt,LOGGER);
                } else {
                    LOGGER.severe(StringUtils.getStringReemplazos("Error indexando... ", e));
                    throw e; // otherwise throw
                }
            } catch (ApiProxy.OverQuotaException e) {
                LOGGER.severe(LOG_ERROR_TS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
        LOGGER.info("FIN INDEXACION");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bbva.mlean.core.funcionalidades.IIndexador#eliminaFuncionalidad(java
     * .util.List)
     */
    @Override
    public void elimina(String id) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                this.searchHandler.deleteDocument(id);
                break;
            } catch (DeleteException | ApiProxy.OverQuotaException e) {
                LOGGER.severe(LOG_ERROR_TS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
    }

    @Override
    public void eliminaTodos() {
        int attempt = INI_ATTEMPT;
        do {
            try {
                this.searchHandler.deleteAll();
                LOGGER.info("Eliminadas todos los registros de TSearch");
                break;
            } catch (DeleteException | ApiProxy.OverQuotaException e) {
                LOGGER.severe(LOG_ERROR_TS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
    }

    /**
     * Borra la lista de ids de TS, con reintentos si es necesario
     *
     * @param ids
     */
    @Override
    public void elimina(List<String> ids) {
        int attempt = INI_ATTEMPT;
        do {
            try {
                this.searchHandler.borrarStrIdsTS(ids);
                LOGGER.fine(String.format(LOG_DELETED_LIST, StringUtils.join(SEPARATOR_STR_LIST, ids)));
                break;
            } catch (DeleteException | ApiProxy.OverQuotaException e) {
                LOGGER.severe(LOG_ERROR_TS_ATTEMPT + attempt);
                if (attempt == MAX_ATTEMPTS) {
                    UtilidadesData.printException(e.getMessage(), e);
                    throw e;
                }
                attempt = UtilidadesData.reintento(attempt,LOGGER);
            }
        } while (attempt <= MAX_ATTEMPTS);
    }
}
