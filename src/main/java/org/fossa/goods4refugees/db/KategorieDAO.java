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

package org.fossa.goods4refugees.db;

import java.util.List;

import org.fossa.goods4refugees.core.Kategorie;
import org.hibernate.SessionFactory;

import com.google.common.base.Optional;

import io.dropwizard.hibernate.AbstractDAO;

public class KategorieDAO extends AbstractDAO<Kategorie> {
    public KategorieDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Kategorie> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public List<Kategorie> findAll() {
        return list(namedQuery("org.fossa.goods4refugees.core.Kategorie.findAll"));
    }
}