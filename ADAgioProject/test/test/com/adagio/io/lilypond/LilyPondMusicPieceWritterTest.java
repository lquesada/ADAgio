package test.com.adagio.io.lilypond;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.modelcc.types.IntegerModel;

import test.com.adagio.InitTest;

import com.adagio.events.channels.MusicChannelIdentifierEvent;
import com.adagio.events.statements.MusicPlayStatementEvent;
import com.adagio.io.lilypond.LilyPondMusicPieceWriter;
import com.adagio.language.bars.chords.Chord;
import com.adagio.language.bars.chords.ChordIdentifier;
import com.adagio.language.bars.chords.intervals.Interval;
import com.adagio.language.bars.chords.intervals.OptionalInterval;
import com.adagio.language.channels.ChannelIdentifier;
import com.adagio.language.figures.Figure;
import com.adagio.language.musicnotes.AbsoluteMusicNote;
import com.adagio.language.musicnotes.AlteredNoteName;
import com.adagio.language.musicnotes.BasicNoteName;
import com.adagio.language.musicnotes.notealterations.Alteration;
import com.adagio.language.musicnotes.notealterations.DoubleFlatAlteration;
import com.adagio.language.musicnotes.notealterations.FlatAlteration;
import com.adagio.language.musicnotes.notealterations.SharpAlteration;
import com.adagio.language.times.Time;

public class LilyPondMusicPieceWritterTest extends InitTest {
	 
	ChordIdentifier M, m, add2;
	Interval M3, m3, P1, P5, M2, optM3;
	AbsoluteMusicNote fundamental, bass;
	BasicNoteName bNoteName;
	Chord chord;
	Alteration alteration;
	LilyPondMusicPieceWriter listener;
	
	ChannelIdentifier channelID;
	MusicChannelIdentifierEvent channelIdentifierEvent;
	MusicPlayStatementEvent playStatementEvent;
	 
	@Before
	public void setUp() throws Exception {
		super.setUp();
		M = new ChordIdentifier("M");
		m = new ChordIdentifier("m");
		add2 = new ChordIdentifier("add2");
		M3 = new Interval("M",3);
		m3 = new Interval("m",3);
		P1 = new Interval("P",1);
		P5 = new Interval("P",5);
		M2 = new Interval("M",2);
		optM3 = new OptionalInterval("M",3);
		bass = null;
		fundamental = null;
		chord = null;
		bNoteName = null;
		alteration = null;
		listener = new LilyPondMusicPieceWriter();
	
		
		List<Interval> intervals = new ArrayList<Interval>();
		
		intervals.add(P1);
		intervals.add(m3);
		intervals.add(P5);
		listener.getChordsDB().addChord(m, intervals);
		
		intervals = new ArrayList<Interval>();
		intervals.add(P1);
		intervals.add(M3);
		intervals.add(P5);
		listener.getChordsDB().addChord(M, intervals);
		
		intervals = new ArrayList<Interval>();
		intervals.add(P1);
		intervals.add(M2);
		intervals.add(optM3);
		intervals.add(P5);
		listener.getChordsDB().addChord(add2, intervals);
	}
	
	@Test
	public void translateChordTest() {
		
		//0CM --> <c e g>
		fundamental = new AbsoluteMusicNote(0, "C");
		chord = new Chord(fundamental,M,bass);
		assertEquals("<c e g>", listener.translateChord(chord, perfectInstrument));
		
		//0Dm --> <d f a>
		fundamental = new AbsoluteMusicNote(0, "D");
		chord = new Chord(fundamental,m,bass);
		assertEquals("<d f a>", listener.translateChord(chord, perfectInstrument));
		
		//3E#M --> <eis''' gisis''' bis'''>
		bNoteName = new BasicNoteName("E");
		alteration = new SharpAlteration();
		fundamental = new AbsoluteMusicNote(new IntegerModel(3), new AlteredNoteName(bNoteName, alteration));
		chord = new Chord(fundamental,M,bass);
		assertEquals("<eis''' gisis''' bis'''>", listener.translateChord(chord, perfectInstrument));
		
		//2F#m -->  <fis'' a'' cis'''>
		bNoteName = new BasicNoteName("F");
		alteration = new SharpAlteration();
		fundamental = new AbsoluteMusicNote(new IntegerModel(2), new AlteredNoteName(bNoteName, alteration));
		chord = new Chord(fundamental,m,bass);
		assertEquals("<fis'' a'' cis'''>", listener.translateChord(chord, perfectInstrument));
		
		//2F#m/2A -->  <fis'' cis''' a'>
		bass = new AbsoluteMusicNote(2,"A");
		bNoteName = new BasicNoteName("F");
		alteration = new SharpAlteration();
		fundamental = new AbsoluteMusicNote(new IntegerModel(2), new AlteredNoteName(bNoteName, alteration));
		chord = new Chord(fundamental,m,bass);
		assertEquals("<fis'' cis''' a'>", listener.translateChord(chord, perfectInstrument));
		
		//4GbM/4Cbb -->   <ges'''' bes'''' des''''' ceses''''>
		bNoteName = new BasicNoteName("C");
		alteration = new DoubleFlatAlteration();
		bass = new AbsoluteMusicNote(new IntegerModel(4), new AlteredNoteName(bNoteName, alteration));
		bNoteName = new BasicNoteName("G");
		alteration = new FlatAlteration();
		fundamental = new AbsoluteMusicNote(new IntegerModel(4), new AlteredNoteName(bNoteName, alteration));
		chord = new Chord(fundamental,M,bass);
		assertEquals("<ges'''' bes'''' des''''' ceses''''>", listener.translateChord(chord, perfectInstrument));
	}
	
	@Test
	public void createChannelTest(){
		channelID = new ChannelIdentifier("piano");
		channelIdentifierEvent = new MusicChannelIdentifierEvent(this,channelID);
		listener.createChannel(channelIdentifierEvent);	
		
		assertTrue(listener.existsChannel(channelIdentifierEvent));	
	}
	
	@Test
	public void destroyChannelTest(){
		channelID = new ChannelIdentifier("piano");
		channelIdentifierEvent = new MusicChannelIdentifierEvent(this,channelID);
		
		listener.createChannel(channelIdentifierEvent);	
		assertTrue(listener.existsChannel(channelIdentifierEvent));	
		listener.destroyChannel(channelIdentifierEvent);
		//Still Exist but erased
		assertTrue(listener.existsChannel(channelIdentifierEvent));
		assertTrue(listener.isErasedChannel(channelIdentifierEvent));
		//It must print a warning message
		listener.destroyChannel(channelIdentifierEvent);
	}
	
	@Test
	public void TranslateFigureTest(){
		Figure figure = new Figure(1.5);
		assertEquals("4.", listener.translateFigure(figure));
		figure = new Figure(4);
		assertEquals("1", listener.translateFigure(figure));
		figure = new Figure(7);
		assertEquals("1..", listener.translateFigure(figure));
	}
	
	@Test
	public void TranslateTimeTest(){
		Time time = new Time(new IntegerModel(4), new Figure(4,0));
		assertEquals("\\time 4/4", listener.translateTime(time));
		time = new Time(new IntegerModel(4), new Figure(2,2));;
		assertEquals("\\time 4/2..", listener.translateTime(time));
	}
	
	@Test
	public void barFiguresTest(){
		// TIME 4/4 - 4 chords
		Vector<Figure> figures = new Vector<Figure>();
		figures = listener.barFigures(4, new Time(new IntegerModel(4), new Figure(4,0)));
		assertEquals(4, figures.size());
		assertEquals(new Figure(4,0), figures.elementAt(0));
		
		// TIME 3/4 - 2 chords
		figures = new Vector<Figure>();
		figures = listener.barFigures(2, new Time(new IntegerModel(3), new Figure(4,0)));
		assertEquals(2, figures.size());
		assertEquals(new Figure(4, 1), figures.elementAt(0));
		
		// TIME 4/4 - 3 chords
		figures = new Vector<Figure>();
		figures = listener.barFigures(3, new Time(new IntegerModel(4), new Figure(4,0)));
		assertEquals(4, figures.size());
		assertEquals(new Figure(4, 0), figures.elementAt(0));
		
		// TIME 4/4 - 8 chords
		figures = new Vector<Figure>();
		figures = listener.barFigures(8, new Time(new IntegerModel(4), new Figure(4,0)));
		assertEquals(8, figures.size());
		assertEquals(new Figure(8,0), figures.elementAt(0));
	}

}
