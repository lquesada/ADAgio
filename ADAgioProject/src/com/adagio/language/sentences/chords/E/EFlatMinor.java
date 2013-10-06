package com.adagio.language.sentences.chords.E;

import org.modelcc.IModel;
import org.modelcc.Pattern;

import com.adagio.language.sentences.chords.Chord;

@Pattern(regExp="Ebm")
public class EFlatMinor extends Chord implements IModel {

	@Override
	public String getValue() {
		return "<ees ges bes>";
	}
}