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

package org.fossa.goods4refugees.core;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "backend_user")
@NamedQueries({
        @NamedQuery(
                name = "org.fossa.goods4refugees.core.BackendUser.findAll",
                query = "SELECT bu FROM BackendUser bu"
        )
})
public class BackendUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "md5password", nullable = false)
    private String md5password;
    
    @Column(name = "isAdmin")
    private boolean isAdmin = false;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMd5password() {
		return md5password;
	}

	public void setMd5password(String md5password) {
		this.md5password = md5password;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BackendUser)) return false;

        final BackendUser that = (BackendUser) o;

        return Objects.equals(this.id, that.id) &&
        		Objects.equals(this.name, that.name) &&
        		Objects.equals(this.isAdmin, that.isAdmin) &&
        		Objects.equals(this.md5password, that.md5password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isAdmin, md5password);
    }
}