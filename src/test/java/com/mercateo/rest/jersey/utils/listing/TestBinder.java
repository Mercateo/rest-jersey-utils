package com.mercateo.rest.jersey.utils.listing;

import java.lang.reflect.Field;
import java.util.Set;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.hk2.utilities.binding.ServiceBindingBuilder;
import org.mockito.Mock;
import org.mockito.Spy;
import org.reflections.ReflectionUtils;

import com.google.common.base.Preconditions;

/**
 * Binder that provides a method to easily binds a given instance to a class.
 * 
 * @author usr
 * 
 */
public abstract class TestBinder extends AbstractBinder {
	private final int rank;

	TestBinder() {
		this(0);
	}

	TestBinder(int rank) {
		this.rank = rank;
	}

	public <T> ServiceBindingBuilder<T> bindInstance(final T t, final Class<T> serviceClass) {
		return bindFactory(instanceFactory(t)).to(serviceClass);
	}

	protected void bindReflectedInstance(final Object t, final Class<?> serviceClass) {
		bindFactory(instanceFactory(t)).to(serviceClass).ranked(rank);
	}

	private <T> Factory<T> instanceFactory(final T object) {
		return new Factory<T>() {

			@Override
			public T provide() {
				return object;
			}

			@Override
			public void dispose(T instance) {
				//
			}
		};
	}

	static class MockFieldsBinder extends TestBinder {
		private Object instanceToReadMocksFrom;

		MockFieldsBinder(Object instanceToReadMocksFrom, int rank) {
			super(rank);
			this.instanceToReadMocksFrom = Preconditions.checkNotNull(instanceToReadMocksFrom);
		}

		@Override
		protected void configure() {
			Set<Field> f = ReflectionUtils.getAllFields(instanceToReadMocksFrom.getClass());
			for (Field field : f) {
				if (field.getAnnotation(Mock.class) != null || field.getAnnotation(Spy.class) != null) {
					try {
						field.setAccessible(true);
						bindReflectedInstance(field.get(instanceToReadMocksFrom), field.getType());
					} catch (Exception e) {
						throw new IllegalArgumentException("Unable to bind mock field " + field.getName() + " from "
								+ instanceToReadMocksFrom.getClass().getName(), e);
					}
				}
			}
		}

	}

	public static TestBinder forAllMocksOf(Object instanceWithMockFields) {
		return new MockFieldsBinder(instanceWithMockFields, 0);
	}

	public static TestBinder forAllMocksOf(Object instanceWithMockFields, int rank) {
		return new MockFieldsBinder(instanceWithMockFields, rank);
	}

	public static <T> TestBinder bindInstance(final T instance) {
		return new TestBinder() {
			@SuppressWarnings("unchecked")
			@Override
			protected void configure() {
				bindInstance(instance, (Class<T>) instance.getClass());
			}
		};
	}

}