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

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.fossa.goods4refugees.util.DateUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "hilfsgut")
@NamedQueries({
        @NamedQuery(
                name = "org.fossa.goods4refugees.core.Hilfsgut.findAll",
                query = "SELECT h FROM Hilfsgut h"
        ),
        @NamedQuery(
                name = "org.fossa.goods4refugees.core.Hilfsgut.findAllByAngelegtDatum",
                query = "SELECT h FROM Hilfsgut h ORDER BY angelegtDatum DESC"
        )
})
public class Hilfsgut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "beschreibung", nullable = false, columnDefinition = "Text")
    private String beschreibung;
    
    @ManyToOne
    @JoinColumn(name = "kategorie_id")
    private Kategorie kategorie;
    
    @ManyToOne
    @JoinColumn(name = "abgabestelle_id")
    private Abgabestelle abgabestelle;
    
    @Column(name = "angelegtDatum")
    private Date angelegtDatum;
    
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

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public Kategorie getKategorie() {
		return kategorie;
	}

	public void setKategorie(Kategorie kategorie) {
		this.kategorie = kategorie;
	}

	public Abgabestelle getAbgabestelle() {
		return abgabestelle;
	}

	public void setAbgabestelle(Abgabestelle abgabestelle) {
		this.abgabestelle = abgabestelle;
	}

	public Date getAngelegtDatum() {
		return angelegtDatum;
	}
	
	@JsonIgnore
	public String getAngelegtDatumString() {
		return DateUtil.showDateStringGerman(angelegtDatum);
	}

	public void setAngelegtDatum(Date angelegtDatum) {
		this.angelegtDatum = angelegtDatum;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hilfsgut)) return false;

        final Hilfsgut that = (Hilfsgut) o;

        return Objects.equals(this.id, that.id) &&
        		Objects.equals(this.name, that.name) && 
        		Objects.equals(this.angelegtDatum, that.angelegtDatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}