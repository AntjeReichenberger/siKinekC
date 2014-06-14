package org.webdev.kpoint.bl.api.mapper.response.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.set.ListOrderedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CollectionConstructor {
	private static final Logger logger = LoggerFactory
			.getLogger(CollectionConstructor.class);

	public static <T, F> List<T> construct(List<F> fromList,
			Class<T> targetClass) {
		if (fromList == null) {
			return null;
		}
		if (fromList.isEmpty()) {
			return Collections.emptyList();
		}

		@SuppressWarnings("unchecked")
		List<T> list = (List<T>) new ArrayList<Object>(fromList.size());
		for (F from : fromList) {
			list.add(from == null ? null : construct(from, targetClass));
		}

		return list;
	}

	public static <T, F> Set<T> construct(Set<F> fromSet, Class<T> targetClass) {
		if (fromSet == null) {
			return null;
		}
		if (fromSet.isEmpty()) {
			return Collections.emptySet();
		}

		@SuppressWarnings("unchecked")
		Set<T> list = (Set<T>) new ListOrderedSet();
		for (F from : fromSet) {
			list.add(from == null ? null : construct(from, targetClass));
		}

		return list;
	}

	private static <T, F> T construct(F from, Class<T> targetClass) {
		try {
			return targetClass.getConstructor(from.getClass())
					.newInstance(from);
		} catch (Exception e) {
			logger.error(
					"Couldn't find constructor while trying to convert a business layer collection; make sure the target class '{}' contains a single-param constructor that accepts the business layer object '{}'",
					new Object[] { targetClass, from.getClass(), e });
			throw new RuntimeException(e);
		}
	}
}
