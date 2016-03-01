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

package org.fossa.goods4refugees.views;

import java.util.ArrayList;
import java.util.List;

import org.fossa.goods4refugees.core.Abgabestelle;
import org.fossa.goods4refugees.core.BackendUser;
import org.fossa.goods4refugees.core.Hilfsgut;
import org.fossa.goods4refugees.core.Kategorie;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;

import io.dropwizard.views.View;

public class BackendHilfsgutView extends View {
	
	private final ImmutableMap<String,String> environmentConfig;
	private final Hilfsgut hilfsgut;
	private final ArrayList<KategorieSelectField> kategorieSelectField;
	private final ArrayList<AbgabestelleSelectField> abgabestelleSelectField;
	private final BackendUser backendUser;

	public enum Template{
    	MUSTACHE("mustache/backendHilfsgut.mustache");
    	
    	private String templateName;
    	private Template(String templateName){
    		this.templateName = templateName;
    	}
    	
    	public String getTemplateName(){
    		return templateName;
    	}
    }

    public BackendHilfsgutView(BackendHilfsgutView.Template template, ImmutableMap<String,String> environmentConfig, Hilfsgut hilfsgut, List<Kategorie> kategorien, List<Abgabestelle> meineAbgabestellen, BackendUser backendUser) {
        super(template.getTemplateName(), Charsets.UTF_8);
        this.environmentConfig = environmentConfig;
        this.hilfsgut = hilfsgut;
        this.backendUser = backendUser;
        
        this.kategorieSelectField = new ArrayList<KategorieSelectField>();
        if (hilfsgut == null || hilfsgut.getKategorie() == null) {
        	KategorieSelectField kategorieSelect = new KategorieSelectField();
        	kategorieSelect.setKategorie(null);
        	kategorieSelect.setSelected("selected");
        	kategorieSelectField.add(kategorieSelect);
        }
        for(Kategorie kategorie : kategorien) {
        	KategorieSelectField kategorieSelect = new KategorieSelectField();
        	kategorieSelect.setKategorie(kategorie);
        	if (hilfsgut != null && hilfsgut.getKategorie() != null && hilfsgut.getKategorie().getId() == kategorie.getId()) {
        		kategorieSelect.setSelected("selected");
    		}
        	kategorieSelectField.add(kategorieSelect);
        }
        
        this.abgabestelleSelectField = new ArrayList<AbgabestelleSelectField>();
        if (hilfsgut == null || hilfsgut.getAbgabestelle() == null) {
        	AbgabestelleSelectField abgabestelleSelect = new AbgabestelleSelectField();
        	abgabestelleSelect.setAbgabestelle(null);
        	abgabestelleSelect.setSelected("selected");
        	abgabestelleSelectField.add(abgabestelleSelect);
        }
        for(Abgabestelle abgabestelle : meineAbgabestellen) {
        	AbgabestelleSelectField abgabestelleSelect = new AbgabestelleSelectField();
        	abgabestelleSelect.setAbgabestelle(abgabestelle);
        	if (hilfsgut != null && hilfsgut.getAbgabestelle() != null && hilfsgut.getAbgabestelle().getId() == abgabestelle.getId()) {
        		abgabestelleSelect.setSelected("selected");
    		}
        	abgabestelleSelectField.add(abgabestelleSelect);
        }
    }
    
    public Hilfsgut getHilfsgut() {
		return hilfsgut;
	}

	public ArrayList<KategorieSelectField> getKategorieSelectField() {
		return kategorieSelectField;
	}

	public ArrayList<AbgabestelleSelectField> getAbgabestelleSelectField() {
		return abgabestelleSelectField;
	}

	public BackendUser getBackendUser() {
		return backendUser;
	}

	public String getUrlStylesheet() {
		return environmentConfig.get("urlStylesheet");
	}
	
	private class KategorieSelectField {
    	private Kategorie kategorie;
    	private String selected = "";
		@SuppressWarnings("unused")
		public Kategorie getKategorie() {
			return kategorie;
		}
		public void setKategorie(Kategorie kategorie) {
			this.kategorie = kategorie;
		}
		@SuppressWarnings("unused")
		public String getSelected() {
			return selected;
		}
		public void setSelected(String selected) {
			this.selected = selected;
		}
    }
	
	private class AbgabestelleSelectField {
    	private Abgabestelle abgabestelle;
    	private String selected = "";
		@SuppressWarnings("unused")
		public Abgabestelle getAbgabestelle() {
			return abgabestelle;
		}
		public void setAbgabestelle(Abgabestelle abgabestelle) {
			this.abgabestelle = abgabestelle;
		}
		@SuppressWarnings("unused")
		public String getSelected() {
			return selected;
		}
		public void setSelected(String selected) {
			this.selected = selected;
		}
    }
}