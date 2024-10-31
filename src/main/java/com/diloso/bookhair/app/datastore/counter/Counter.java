package com.diloso.bookhair.app.datastore.counter;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;

/**
 * A counter with a given name and optional initial value
 * @author aaranda
 *
 */
public final class Counter {

	private String name;

	private long initialValue;

	/**
	 * @param name
	 */
	Counter(String name) {
		this.name = name;
		this.initialValue = 1;
	}
	
	/**
	 * @param name
	 * @param initialValue
	 */
	Counter(String name, long initialValue) {
		this.name = name;
		this.initialValue = initialValue;
	}

	/**
	 * @return next value of this counter
	 */
	public Long nextValue() {
		return ObjectifyService.ofy().transact(new Work<Long>() {
			@Override
			public Long run() {
				CounterBean counter = ObjectifyService.ofy().load().type(CounterBean.class).id(name).now();
				if (counter == null) {
					counter = new CounterBean(name, initialValue);
				} else {
					counter.setValue(counter.getValue() + 1);
				}
				ObjectifyService.ofy().save().entity(counter);
				return counter.getValue();
			}
		});
	}
}
