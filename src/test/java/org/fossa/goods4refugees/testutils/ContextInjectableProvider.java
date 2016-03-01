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

package org.fossa.goods4refugees.testutils;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ContextInjectableProvider<T> extends AbstractBinder {
    private final Class<T> clazz;
    private final T instance;

    public ContextInjectableProvider(Class<T> clazz, T instance) {
        this.clazz = clazz;
        this.instance = instance;
    }

    @Override
    protected void configure() {
        bind(instance).to(clazz);
    }
}