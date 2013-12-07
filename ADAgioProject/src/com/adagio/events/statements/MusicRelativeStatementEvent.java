package com.adagio.events.statements;

import java.util.EventObject;

import com.adagio.language.musicnotes.AbsoluteMusicNote;

public class MusicRelativeStatementEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AbsoluteMusicNote aNote;
	
	public MusicRelativeStatementEvent(Object arg0, AbsoluteMusicNote aNote) {
		super(arg0);
		this.aNote = aNote;
	}

	public AbsoluteMusicNote getaNote() {
		return aNote;
	}
}
