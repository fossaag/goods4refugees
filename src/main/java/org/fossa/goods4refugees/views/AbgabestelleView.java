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

import java.util.List;

import org.fossa.goods4refugees.core.Abgabestelle;
import org.fossa.goods4refugees.core.Hilfsgut;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;

import io.dropwizard.views.View;

public class AbgabestelleView extends View {
	
	private final ImmutableMap<String,String> environmentConfig;
	private final Abgabestelle abgabestelle;
	private final List<Hilfsgut> abgabestellenHilfsgueter;
    
    public enum Template{
    	MUSTACHE("mustache/abgabestelle.mustache");
    	
    	private String templateName;
    	private Template(String templateName){
    		this.templateName = templateName;
    	}
    	
    	public String getTemplateName(){
    		return templateName;
    	}
    }

    public AbgabestelleView(AbgabestelleView.Template template, ImmutableMap<String,String> environmentConfig, Abgabestelle abgabestelle, List<Hilfsgut> abgabestellenHilfsgueter) {
        super(template.getTemplateName(), Charsets.UTF_8);
        this.environmentConfig = environmentConfig;
        this.abgabestelle = abgabestelle;
        this.abgabestellenHilfsgueter = abgabestellenHilfsgueter;
    }
    
    public Abgabestelle getAbgabestelle() {
    	return abgabestelle;
    }
    
    public List<Hilfsgut> getAbgabestellenHilfsgueter() {
		return abgabestellenHilfsgueter;
	}

	public String getUrlStylesheet() {
		return environmentConfig.get("urlStylesheet");
	}
}