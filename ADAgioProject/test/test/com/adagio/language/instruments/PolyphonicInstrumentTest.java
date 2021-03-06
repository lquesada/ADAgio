package test.com.adagio.language.instruments;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import test.com.adagio.InitTest;

import com.adagio.language.musicnotes.AbsoluteMusicNote;

public class PolyphonicInstrumentTest extends InitTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	
	
	@Test
	public void aNotesToRegisterstest() {
		List<AbsoluteMusicNote> aNotes = new ArrayList<AbsoluteMusicNote>();
		List<AbsoluteMusicNote> expected = new ArrayList<AbsoluteMusicNote>();
		List<AbsoluteMusicNote> result;
		
		//piano(0C,0E,0G) == (1C,1E,1G)
		aNotes.add(C0);
		aNotes.add(E0);
		aNotes.add(G0);
		
		expected.add(C1);
		expected.add(E1);
		expected.add(G1);
		result = realPiano.apply(aNotes);
		assertEquals(expected, result);
		aNotes.clear();
		expected.clear();
		
		//piano(-1C,-1E,-1G) == (1C,1E,1G)
		aNotes.add(Cm1);
		aNotes.add(Em1);
		aNotes.add(Gm1);

		expected.add(C1);
		expected.add(E1);
		expected.add(G1);
		result = realPiano.apply(aNotes);
		assertEquals(expected, result);
		aNotes.clear();
		expected.clear();
		
		// piano(2F#, A2, C3Sharp) == (2F#, A2, C3Sharp);
		aNotes.add(F2Sharp);
		aNotes.add(A2);
		aNotes.add(C3Sharp);
	
		expected.add(F2Sharp);
		expected.add(A2);
		expected.add(C3Sharp);
		result = realPiano.apply(aNotes);
		assertEquals(expected, result);	
		aNotes.clear();
		expected.clear();
		
		//polyInstrument(4C,4E,4G) == (2C,2E,2G)
		aNotes.add(C4);
		aNotes.add(E4);
		aNotes.add(G4);

		expected.add(C2);
		expected.add(E2);
		expected.add(G2);
		result = polyInstrument.apply(aNotes);
		assertEquals(expected, result);
		aNotes.clear();
		expected.clear();
	}

}
