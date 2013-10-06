package com.adagio.language.sentences.chords.C;

import org.modelcc.IModel;
import org.modelcc.Pattern;

import com.adagio.language.sentences.chords.Chord;

@Pattern(regExp="C#m")
public class CSharpMinor extends Chord implements IModel {

	@Override
	public String getValue() {
		return "<cis e gis>";
	}
}