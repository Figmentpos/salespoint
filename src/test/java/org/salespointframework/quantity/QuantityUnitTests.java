/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.quantity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Unit tests for {@link Quantity}.
 *
 * @author Oliver Gierke
 * @author Paul Henke
 */
public class QuantityUnitTests {

	@Test // #9
	public void defaultsToUnitAsMetric() {
		assertThat(Quantity.of(1).getMetric(), is(Metric.UNIT));
	}

	@Test(expected = IllegalArgumentException.class) // #9
	public void rejectsNullMetric() {
		Quantity.of(0, null);
	}

	@Test // #9
	public void rejectsIncompatibleMetric() {

		assertThat(Quantity.of(1).isCompatibleWith(Metric.UNIT), is(true));
		assertThat(Quantity.of(1).isCompatibleWith(Metric.KILOGRAM), is(false));
	}

	@Test(expected = MetricMismatchException.class) // #9
	public void rejectsIncompatibleQuantityOnAddition() {
		Quantity.of(1).add(Quantity.of(1, Metric.KILOGRAM));
	}

	@Test // #9
	public void addsQuantitiesCorrectly() {

		assertThat(Quantity.of(1).add(Quantity.of(1)), is(Quantity.of(2)));
		assertThat(Quantity.of(1.5).add(Quantity.of(1.5)), is(Quantity.of(3.0)));
	}

	@Test(expected = MetricMismatchException.class) // #9
	public void rejectsIncompatibleQuantityOnSubtraction() {
		Quantity.of(1).subtract(Quantity.of(1, Metric.KILOGRAM));
	}

	@Test // #64, #65, #9
	public void subtractsQuantitiesCorrectly() {

		assertThat(Quantity.of(10).subtract(Quantity.of(1)), is(Quantity.of(9)));
		assertThat(Quantity.of(10.5).subtract(Quantity.of(7.25)), is(Quantity.of(3.25)));
	}

	@Test // #34, #9
	public void comparesQuantitiesCorrectly() {

		Quantity five = Quantity.of(5);
		Quantity ten = Quantity.of(10);

		assertThat(five.isLessThan(ten), is(true));
		assertThat(five.isGreaterThan(ten), is(false));

		assertThat(ten.isGreaterThan(five), is(true));
		assertThat(ten.isLessThan(five), is(false));

		assertThat(ten.isGreaterThanOrEqualTo(ten), is(true));
		assertThat(ten.isGreaterThanOrEqualTo(five), is(true));
	}

	@Test // #9
	public void discoversNegativeQuantity() {

		assertThat(Quantity.of(-1).isNegative(), is(true));
		assertThat(Quantity.of(0).isNegative(), is(false));
		assertThat(Quantity.of(1).isNegative(), is(false));
	}

	@Test // #99, #184
	public void printsReasonableToString() {

		assertThat(Quantity.of(5).toString()).isEqualTo("5");
		assertThat(Quantity.of(5, Metric.LITER).toString()).containsSequence("5", "l");
		assertThat(Quantity.of(5.0, Metric.LITER).toString()).containsSequence("5", "l").doesNotContain("0");
		assertThat(Quantity.of(5.1, Metric.LITER).toString()).containsSequence("5", "1", "l");
		assertThat(Quantity.of(5.11, Metric.LITER).toString()).containsSequence("5", "11", "l");
	}

	@Test // #129
	public void comparesToZero() {

		Quantity quantity = Quantity.of(5);
		Quantity zero = Quantity.of(0, Metric.LITER);

		assertThat(quantity.isGreaterThan(quantity.toZero()), is(true));
		assertThat(zero.equals(zero.toZero()), is(true));
	}
}
