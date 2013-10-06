package com.adagio.language.sentences.chords.G;

import org.modelcc.IModel;
import org.modelcc.Pattern;

import com.adagio.language.sentences.chords.Chord;

@Pattern(regExp="GbM")
public class GFlatMajor extends Chord implements IModel {

	@Override
	public String getValue() {
		return "<ges bes des>";
	}
}