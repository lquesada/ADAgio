package com.adagio.language;
import org.modelcc.*;

import com.adagio.events.MusicEventListener;
import com.adagio.language.definitions.Definition;
import com.adagio.language.statements.Statement;

/**
 * Main Class. This Class constructs the lilypond code.
 * A MusicPiece is sequence of statements. (or 0)
 * @author Wungo
 */
public class MusicPiece implements IModel {
	@Multiplicity(minimum = 0)
	private Definition[] definitions;
	@Multiplicity(minimum = 0)
	private Statement[] statements;
	
	/**
	 * Charges in data the information needed to translate
	 */

	public void run(MusicEventListener listener){
		for(Definition current: this.getDefinitions()){
			current.run(listener);
		}
		
		for(Statement current: this.getSentences()){
			current.run(listener);
		}
	}
	
	public Statement[] getSentences() {
		return statements;
	}

	public void setSentences(Statement[] sentences) {
		this.statements = sentences;
	}

	public Definition[] getDefinitions() {
		return definitions;
	}

	public void setDefinitions(Definition[] definitions) {
		this.definitions = definitions;
	}
	
	
	
}
