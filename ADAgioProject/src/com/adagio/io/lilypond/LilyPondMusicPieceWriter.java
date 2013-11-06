package com.adagio.io.lilypond;

import java.io.PrintWriter;
import java.util.List;

import com.adagio.io.MusicPieceWriter;
import com.adagio.language.MusicPiece;
import com.adagio.language.RunData;
import com.adagio.language.chords.Chord;
import com.adagio.language.chords.intervals.Interval;
import com.adagio.language.musicnotes.AbsoluteMusicNote;
import com.adagio.language.musicnotes.notealterations.Alteration;

public class LilyPondMusicPieceWriter extends MusicPieceWriter {

	@Override
	public void write(MusicPiece m, PrintWriter out) {
		String composition = "";
		RunData data = new RunData();
		
		m.run(data);
		
		composition = this.translate(data);
		System.out.print(composition);
		out.print(composition);
		
	}
	
	public static void writeMusicPiece(MusicPiece m,PrintWriter out){
	    LilyPondMusicPieceWriter writer = new LilyPondMusicPieceWriter();
	    writer.write(m,out);
	}
	
	private String translate(RunData data){
		String composition = "";
		
		composition += "{\n";
		composition += ("\\clef " + data.getClef() + "\n");
		composition += ("\\time " + data.getTime().toString() + "\n");
		for(int i = 0; i < data.notesBar.size(); i++){
			composition += this.translateAbsoluteMusicNote(data.notesBar.elementAt(i));
			composition += Integer.toString(data.getTime().defaultNoteDuration());
			composition += " ";
		}
		composition += "\n}\n";
		
		composition += "{\n";
		composition += ("\\clef " + data.getClef() + "\n");
		composition += ("\\time " + data.getTime().toString() + "\n");
		for(int i = 0; i < data.chordsBar.size(); i++){
			composition += this.translateChord(data.chordsBar.elementAt(i),data);
			composition += Integer.toString(data.getTime().defaultNoteDuration());
			composition += " ";
		}
		composition += "\n}\n";
		return composition;
	}
	
	/*
	 * Receives a chord with an absolute-fundamental-note and translates it 
	 * using its definition in "data".
	 */
	private String translateChord(Chord chord, RunData data){
		String composition = "";
		List<Interval> intervals = data.getChordsDB().get(chord.getIdentifier());
		AbsoluteMusicNote aNote;
		
		composition += "<";
		for(int i = 0; i < intervals.size();i++){
			aNote = intervals.get(i).Apply(chord.getNote(), data);
			composition += this.translateAbsoluteMusicNote(aNote);
			
			if(i != intervals.size()-1){
				composition += " ";
			}
		}
		composition += ">";
		
		return composition;
	}
	
	/**
	 * Receives and AbsoluteNote and translates it to lilyPond syntax.
	 * @param aNote Note to be translated
	 * @return A String that contains the translation
	 */
	private String translateAbsoluteMusicNote(AbsoluteMusicNote aNote){
		String composition = aNote.getBasicNoteNameString().toLowerCase();
		int intOctave = aNote.getOctave().intValue();
		Alteration alteration = aNote.getMusicNoteName().getAlteration();
		
		if(alteration != null){
			composition += this.translateAlteration(alteration);
		}
		
		for(int i = 0; i < Math.abs(intOctave); i++){
			if(intOctave > 0){
				composition += "'";
			}
			else{
				composition += ",";
			}
		}
				
		return composition;
	}
	
	/**
	 * Receive an alteration and translates it to LilyPond syntax.
	 * @param alteration Alteration to be translated
	 * @return A string with the translation
	 */
	private String translateAlteration(Alteration alteration){
		String composition = "";
		String value = alteration.getValue();
		
		if(value.equals("#")){
			composition = "is";
		}
		else if(value.equals("x")){
			composition = "isis";
		}
		else if (value.equals("b")){
			composition = "es";
		}
		else if (value.equals("bb")){
			composition = "eses";
		}
		return composition;
	}

}
