package com.diloso.bookhair.app.datastore.counter;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;

/**
 * A factory to retrieve counters. A counter is a datastore entity with a long value increased when a new value is needed
 * Use case:
 * <code>
 *   CounterFactory.getCounter("counter").nextValue();
 * </code>
 * 
 * Optionally you can define a property "counter.{counter_name}.initial" defining initial value. Default value would be 1 otherwise.
 * 
 * For example, in app.properties:
 * 
 * <code>
 * counter.counter.initial=100
 * </code>
 * 
 * @author aaranda
 *
 */
public class CounterFactory {

	private static Map<String, Counter> counters = new HashMap<String, Counter>();

	static {
		ObjectifyService.register(CounterBean.class);
	}

	/**
	 * @param name
	 * @return counter with given name
	 */
	public static Counter getCounter(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Invalid counter name");
		}
		String normalizedCounter = name.toLowerCase();
		Counter counter = counters.get(normalizedCounter);
		if (counter != null) {
			return counter;
		}
		String initialValueProperty = "counter." + normalizedCounter + ".initial";
		//String initialString = PropertiesFactory.getProperties().getProperty(initialValueProperty, "");
		String initialString = "";
		if (!StringUtils.isEmpty(initialString)) {
			try {
				long initialValue = Long.parseLong(initialString);
				counter = new Counter(normalizedCounter, initialValue);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(initialValueProperty + " has wrong value: " + initialString);
			}
		} else {
			counter = new Counter(normalizedCounter);
		}
		counters.put(normalizedCounter, counter);
		return counter;
	}

    /**
     * Resets all counters
     */
    public static void resetAll() {
        final List<CounterBean> list = ObjectifyService.ofy().load().type(CounterBean.class).list();
        for (final CounterBean counter : list) {
            ObjectifyService.ofy().transact(new Work<Void>() {
                @Override
                public Void run() {
                    ObjectifyService.ofy().delete().entity(counter);
                    return null;
                }
            });
        }
    }

}
