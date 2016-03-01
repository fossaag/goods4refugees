/**
 * Copyright (c) 2015 Frank Kaddereit, Anne Lachnitt, http://www.fossa.de/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.fossa.goods4refugees.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.fossa.goods4refugees.util.DateUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DateUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDatesWithinRangeDaysRoughCalculationTrue() {
		Calendar gc = GregorianCalendar.getInstance();
		gc.setTime(new Date());
		gc.set(Calendar.DAY_OF_YEAR, gc.get(Calendar.DAY_OF_YEAR) + 30);
		assertTrue(DateUtil.datesWithinRangeDaysRoughCalculation(new Date(), gc.getTime(), 30));
	}
	
	@Test
	public void testDatesWithinRangeDaysRoughCalculationFalse() {
		Calendar gc = GregorianCalendar.getInstance();
		gc.setTime(new Date());
		gc.set(Calendar.DAY_OF_YEAR, gc.get(Calendar.DAY_OF_YEAR) + 31);
		assertFalse(DateUtil.datesWithinRangeDaysRoughCalculation(gc.getTime(), new Date(), 30));
	}
}
