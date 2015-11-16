package org.salespointframework.quantity;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.persistence.Embeddable;

import org.springframework.util.Assert;

/**
 * A value object to represent a quantity.
 * 
 * @author Oliver Gierke
 */
@Embeddable
@Value(staticConstructor = "of")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Quantity {

	private static final String INCOMPATIBLE = "Quantity %s is incompatible to quantity %s!";

	// tag::attributes[]
	private @NonNull final BigDecimal amount;
	private @NonNull final Metric metric;
	// end::attributes[]

	Quantity() {
		this.amount = null;
		this.metric = null;
	}

	/**
	 * Creates a new {@link Quantity} of the given amount. Defaults the metric to {@value Metric#UNIT}.
	 * 
	 * @param amount must not be {@literal null}.
	 * @return
	 */
	public static Quantity of(long amount) {
		return of(amount, Metric.UNIT);
	}

	/**
	 * Creates a new {@link Quantity} of the given amount. Defaults the metric to {@value Metric#UNIT}.
	 * 
	 * @param amount must not be {@literal null}.
	 * @return
	 */
	public static Quantity of(double amount) {
		return of(amount, Metric.UNIT);
	}

	/**
	 * Creates a new {@link Quantity} of the given amount and {@link Metric}.
	 * 
	 * @param amount must not be {@literal null}.
	 * @param metric must not be {@literal null}.
	 * @return
	 */
	public static Quantity of(long amount, Metric metric) {
		return new Quantity(BigDecimal.valueOf(amount), metric);
	}

	/**
	 * Creates a new {@link Quantity} of the given amount and {@link Metric}.
	 * 
	 * @param amount must not be {@literal null}.
	 * @param metric must not be {@literal null}.
	 * @return
	 */
	public static Quantity of(double amount, Metric metric) {
		return new Quantity(BigDecimal.valueOf(amount), metric);
	}

	/**
	 * Returns whether the {@link Quantity} is compatible with the given {@link Metric}.
	 * 
	 * @param metric must not be {@literal null}.
	 * @return
	 */
	public boolean isCompatibleWith(Metric metric) {

		Assert.notNull(metric, "Metric must not be null!");
		return this.metric.isCompatibleWith(metric);
	}

	/**
	 * Adds the given {@link Quantity} to the current one.
	 * 
	 * @param other the {@link Quantity} to add. The given {@link Quantity}'s {@link Metric} must be compatible with the
	 *          current one.
	 * @return
	 * @see #isCompatibleWith(Metric)
	 */
	public Quantity add(Quantity other) {

		assertCompatibility(other);
		return new Quantity(this.amount.add(other.amount), this.metric);
	}

	/**
	 * Subtracts the given Quantity from the current one.
	 * 
	 * @param other the {@link Quantity} to add. The given {@link Quantity}'s {@link Metric} must be compatible with the
	 *          current one.
	 * @return
	 * @see #isCompatibleWith(Metric)
	 */
	public Quantity subtract(Quantity other) {

		assertCompatibility(other);
		return new Quantity(this.amount.subtract(other.amount), this.metric);
	}

	/**
	 * Returns whether the given {@link Quantity} is less than the current one.
	 * 
	 * @param other must not be {@literal null}. The given {@link Quantity}'s {@link Metric} must be compatible with the
	 *          current one.
	 * @return
	 * @see #isCompatibleWith(Metric)
	 */
	public boolean isLessThan(Quantity other) {

		assertCompatibility(other);
		return this.amount.compareTo(other.amount) < 0;
	}

	/**
	 * Returns whether the given {@link Quantity} is greater than the current one.
	 * 
	 * @param other must not be {@literal null}. The given {@link Quantity}'s {@link Metric} must be compatible with the
	 *          current one.
	 * @return
	 * @see #isCompatibleWith(Metric)
	 */
	public boolean isGreaterThan(Quantity other) {

		assertCompatibility(other);
		return this.amount.compareTo(other.amount) > 0;
	}

	/**
	 * Returns whether the given {@link Quantity} is greater than or equal to the current one.
	 * 
	 * @param other must not be {@literal null}. The given {@link Quantity}'s {@link Metric} must be compatible with the
	 *          current one.
	 * @return
	 * @see #isCompatibleWith(Metric)
	 */
	public boolean isGreaterThanOrEqualTo(Quantity other) {

		assertCompatibility(other);
		return this.amount.compareTo(other.amount) >= 0;
	}

	/**
	 * Returns whether the current {@link Quantity} is negative.
	 * 
	 * @return
	 */
	public boolean isNegative() {
		return this.amount.compareTo(BigDecimal.ZERO) < 0;
	}

	private void assertCompatibility(Quantity quantity) {

		Assert.notNull(quantity, "Quantity must not be null!");
		Assert.isTrue(quantity.isCompatibleWith(metric), String.format(INCOMPATIBLE, this, quantity));
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new DecimalFormat().format(amount).concat(metric.getAbbreviation());
	}
}