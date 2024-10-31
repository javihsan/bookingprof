package com.diloso.bookhair.app.persist.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import com.diloso.bookhair.app.datastore.UtilidadesData;
import com.diloso.bookhair.app.datastore.data.IStorable;
import com.diloso.bookhair.app.datastore.data.LongIdCRUDService;
import com.diloso.bookhair.app.datastore.data.ObjectifyServiceMod;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.cmd.QueryKeys;

/**
 * DAO abstracto del que extenderán el resto de los de la aplicación.
 *
 * @author emondelo
 */
public abstract class AbstractDAO<T extends IStorable<Long>> extends LongIdCRUDService<T> {

	public static final int MAX_QUOTA_DS_DEFAULT = 1000;
	
	/**
	 * Actualiza todos los elementos haciendo uso de ObjectifyServiceMod que
	 * deshabilita el uso de memcache para esta escritura.
	 *
	 * @param objects
	 * @return
	 */
	public List<T> updateAllWithoutMemcache(List<T> objects) {
		DateTime now = UtilidadesData.now();
		for (T o : objects) {
			setTimestamps(o, now);
		}
		ObjectifyServiceMod.getService().updateAllWithoutMemcache(objects);
		return objects;
	}

	/**
	 * Iterador de todas las entidades
	 * <p/>
	 *
	 * @return
	 */
	public Iterator<T> iterator() {
		return new DataStoreCursorIterator<>(getDataClass());
	}

	/**
	 * Iterador de todos los ids
	 * <p/>
	 *
	 * @return
	 */
	public Iterator<String> idsIterator() {
		return new DataStoreCursorIdsIterator<>(getDataClass());
	}

	/**
	 * Iterador con filtro
	 * <p/>
	 *
	 * @return
	 */
	public Iterator<T> iterator(Map<String, Object> filters) {
		return new DataStoreCursorIterator<>(getDataClass(), filters);
	}

	/**
	 * Iterador con filtro que devuelve ids
	 * <p/>
	 *
	 * @return
	 */
	public Iterator<String> idsIterator(Map<String, Object> filters) {
		return new DataStoreCursorIdsIterator<>(getDataClass(), filters);
	}

	/**
	 * Recupera el detalle de un filesystem a partir de su clave (nombre del
	 * filesystem)
	 *
	 * @param clave
	 * @return
	 */
	public T getEntidad(Long clave) {
		return this.get(clave);
	}

	/**
	 * Dada una lista de identificadores, devuelve aquellos que no están en DS
	 *
	 * @param ids
	 * @return los que no están en DS
	 */
	public List<String> notInDatastore(Set<String> ids) {
		Map<String, T> data = ObjectifyServiceMod.ofy().cache(false).load().type(getDataClass()).ids(ids);
		List<String> result = new ArrayList<>();
		for (String id : ids) {
			if (data.get(id) == null) {
				result.add(id);
			}
		}
		return result;
	}

	public static class DataStoreCursorIterator<T> implements Iterator<T> {

		private Class<T> clazz;

		private Query<T> query;

		private String cursor;

		private QueryResultIterator<T> iterator;

		private boolean newIterator = false;

		private Map<String, Object> filters;

		public DataStoreCursorIterator(Class<T> clazz) {
			this(clazz, null);
		}

		public DataStoreCursorIterator(Class<T> dataClass, Map<String, Object> filters) {
			this.clazz = dataClass;
			this.filters = filters;
			fetchData();
		}

		private Query createQuery(Class<T> clazz, String cursor) {
			Query<T> query = ObjectifyService.ofy().load().type(clazz).limit(MAX_QUOTA_DS_DEFAULT)
					.chunk(MAX_QUOTA_DS_DEFAULT);
			if (cursor != null && !"".equals(cursor)) {
				query = query.startAt(Cursor.fromWebSafeString(cursor));
			}
			if (filters != null) {
				for (Map.Entry<String, Object> filter : filters.entrySet()) {
					query = query.filter(filter.getKey(), filter.getValue());
				}
			}
			return query;
		}

		private void fetchData() {
			this.query = createQuery(clazz, this.cursor);
			this.iterator = this.query.iterator();
			this.newIterator = true; // Nuevo iterador
		}

		@Override
		public boolean hasNext() {
			// Si hay más datos con la última consulta, se devuelve que sí
			if (this.iterator.hasNext()) {
				return true;
			}
			// No hay más datos con la última consulta, hay cursor?
			this.cursor = this.iterator.getCursor().toWebSafeString();
			if (this.cursor != null && !"".equals(this.cursor)) {
				// Hay cursor, pedimos más datos y volvemos a comprobar si hay
				// más datos
				fetchData();
				return this.iterator.hasNext();
			} else {
				// No hay más datos ni más cursores
				return false;
			}
		}

		@Override
		public T next() {
			T t = this.iterator.next();
			if (this.newIterator) {
				// Actualización de cursor después de next, antes del primer
				// elemento no tiene valor
				this.cursor = this.iterator.getCursor().toWebSafeString();
				this.newIterator = false;
			}
			return t;
		}

		@Override
		public void remove() {
		}
	}

	public static class DataStoreCursorIdsIterator<T> implements Iterator<String> {

		private Class<T> clazz;

		private QueryKeys<T> query;

		private String cursor;

		private QueryResultIterator<Key<T>> iterator;

		private boolean newIterator = false;

		private Map<String, Object> filters;

		public DataStoreCursorIdsIterator(Class<T> clazz) {
			this(clazz, null);
		}

		public DataStoreCursorIdsIterator(Class<T> dataClass, Map<String, Object> filters) {
			this.clazz = dataClass;
			this.filters = filters;
			fetchData();
		}

		private QueryKeys<T> createQuery(Class<T> clazz, String cursor) {
			Query<T> query = ObjectifyService.ofy().load().type(clazz).limit(MAX_QUOTA_DS_DEFAULT)
					.chunk(MAX_QUOTA_DS_DEFAULT);
			if (cursor != null && !"".equals(cursor)) {
				query = query.startAt(Cursor.fromWebSafeString(cursor));
			}
			if (filters != null) {
				for (Map.Entry<String, Object> filter : filters.entrySet()) {
					query = query.filter(filter.getKey(), filter.getValue());
				}
			}
			return query.keys();
		}

		private void fetchData() {
			this.query = createQuery(clazz, this.cursor);
			this.iterator = this.query.iterator();
			this.newIterator = true; // Nuevo iterador
		}

		@Override
		public boolean hasNext() {
			// Si hay más datos con la última consulta, se devuelve que sí
			if (this.iterator.hasNext()) {
				return true;
			}
			// No hay más datos con la última consulta, hay cursor?
			this.cursor = this.iterator.getCursor().toWebSafeString();
			if (this.cursor != null && !"".equals(this.cursor)) {
				// Hay cursor, pedimos más datos y volvemos a comprobar si hay
				// más datos
				fetchData();
				return this.iterator.hasNext();
			} else {
				// No hay más datos ni más cursores
				return false;
			}
		}

		@Override
		public String next() {
			Key<T> t = this.iterator.next();
			if (this.newIterator) {
				// Actualización de cursor después de next, antes del primer
				// elemento no tiene valor
				this.cursor = this.iterator.getCursor().toWebSafeString();
				this.newIterator = false;
			}
			return t.getName();
		}

		@Override
		public void remove() {
		}
	}
}
