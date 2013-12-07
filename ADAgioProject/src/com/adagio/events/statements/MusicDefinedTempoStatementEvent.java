package com.adagio.events.statements;

import java.util.EventObject;

import com.adagio.language.tempos.TempoIdentifier;

public class MusicDefinedTempoStatementEvent extends EventObject {

	private static final long serialVersionUID = 6328874304911223744L;
	TempoIdentifier identifier;
	
	public MusicDefinedTempoStatementEvent(Object arg0, TempoIdentifier identifier) {
		super(arg0);
		this.identifier = identifier;
	}

	public TempoIdentifier getIdentifier() {
		return identifier;
	}
}
