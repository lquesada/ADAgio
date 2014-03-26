package com.adagio.language.definitions;

import java.util.ArrayList;
import java.util.List;

import org.modelcc.Constraint;
import org.modelcc.IModel;
import org.modelcc.Multiplicity;
import org.modelcc.Prefix;
import org.modelcc.Separator;
import org.modelcc.Suffix;

import com.adagio.events.MusicEventListener;
import com.adagio.events.chords.MusicChordAddEvent;
import com.adagio.language.bars.chords.ChordIdentifier;
import com.adagio.language.bars.chords.intervals.Interval;
import com.adagio.language.musicnotes.BasicNoteName;
import com.adagio.language.musicnotes.notealterations.DoubleFlatAlteration;
import com.adagio.language.musicnotes.notealterations.DoubleSharpAlteration;
import com.adagio.language.musicnotes.notealterations.FlatAlteration;
import com.adagio.language.musicnotes.notealterations.SharpAlteration;

@Prefix({"(?i)Define", "[ |\n|\r|\t]+" ,"(?i)Chord", "[ |\n|\r|\t]+"})
public class ChordDefinition extends Definition implements IModel {
	
	@Prefix("\"")
	@Suffix("\"")
	ChordIdentifier identifier;
	@Constraint
	boolean allowedIdentifier(){
		boolean allowed = true;
		
		if(!identifier.getValue().equals("")){
			if(this.beginWithAlteration()){
				// Nunca llegar� a ser informado. El patr�n de ChorIdentifier no permite estos casos.
				// Modificar el patr�n de ChordIdentifier genera conflictos que no se pueden resolver con priority.
				// TODO Solucionar esto

				System.err.println("Error 2: \"" + identifier.getValue() +"\" --> The identifier can't begin with an Alteration");
				allowed = false;
				//System.exit(2);
			}
			else if(this.beginWithNumber()){
				System.err.println("Error 3: \"" + identifier.getValue() +"\" --> The identifier can't begin with a number");
				allowed = false;
				//System.exit(3);
			}
			else if(this.beginWithBasicNoteName()){
				System.err.println("Error 4: \"" + identifier.getValue() +"\" --> The identifier can't begin with a BasicNoteName");
				allowed = false;
				//System.exit(3);
			}
		}
		return allowed;
	}
	
	@Prefix({"[ |\n|\r|\t]+","(?i)NOTES", "[ |\n|\r|\t]+"})
	@Separator("[ |\n|\r|\t]+")
	@Multiplicity(minimum = 1)
	Interval[] intervals;
	
	@Override
	public void run(MusicEventListener listener) {
		List<Interval> auxInterval = new ArrayList<Interval>();
		for(int i = 0; i < intervals.length; i++){
			auxInterval.add(intervals[i]);
		}
		listener.addChord(new MusicChordAddEvent(this,identifier,auxInterval));
	}
	
	private boolean beginWithNumber(){
		
		int head = Character.getNumericValue(identifier.getValue().charAt(0));
		
		if(head >= 0 && head <= 9){
			return true;
		}
		
		return false;
	}
	
	boolean beginWithAlteration(){
		
		String head = String.valueOf(identifier.getValue().charAt(0));
		
		if(head.equals(FlatAlteration.getPatern())){
			return true;
		}
		else if (head.equals(DoubleFlatAlteration.getPatern())){
			return true;
		}
		else if (head.equals(SharpAlteration.getPatern())){
			return true;
		}
		else if (head.equals(DoubleSharpAlteration.getPatern())){
			return true;
		}
		return false;
	}
	
	boolean beginWithBasicNoteName(){
		
		String head = String.valueOf(identifier.getValue().charAt(0));
		
		for(int i = 0; i < BasicNoteName.getPattern().size(); i++){
			if(head.equals(BasicNoteName.getPattern().elementAt(i))){
				return true;
			}
		}
		return false;
	}

}
