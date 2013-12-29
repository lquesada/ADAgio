package test.com.adagio.language;

import static org.modelcc.test.ModelAssert.assertAmbiguityFree;

import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;
import org.modelcc.io.java.JavaModelReader;
import org.modelcc.metamodel.Model;
import org.modelcc.parser.Parser;
import org.modelcc.parser.ParserFactory;

import com.adagio.ADAgioCLI;
import com.adagio.language.MusicPiece;

public class MusicPieceTest {
	
	Model model;
	Parser<MusicPiece> parser;
	String path = ".\\input_examples";
	String program0 =  path + "\\0_DefaultChannelProgram.adg";
	String program1 = path + "\\1_TempoProgram.adg";
	String program2 = path + "\\2_EmeProgram.adg";
	String program3 = path +  "\\3_MultiplicityProgram.adg";
	String program4 = path + "\\4_BarsProgram.adg";
	String program5 = path + "\\5_TimesProgram.adg";
	
	String test0, test1, test2, test3, test4, test5;
	

	  @SuppressWarnings("unchecked")
	@Before
	  public void setUp() throws Exception {
		
	    model = JavaModelReader.read(MusicPiece.class);
	    parser = ParserFactory.create(model,ParserFactory.WHITESPACE);
	    test0 = ADAgioCLI.fileToString(program0, StandardCharsets.UTF_8);
	    test1 = ADAgioCLI.fileToString(program1, StandardCharsets.UTF_8);
	    test2 = ADAgioCLI.fileToString(program2, StandardCharsets.UTF_8);
	    test3 = ADAgioCLI.fileToString(program3, StandardCharsets.UTF_8);
	    test4 = ADAgioCLI.fileToString(program4, StandardCharsets.UTF_8);
	    test5 = ADAgioCLI.fileToString(program5, StandardCharsets.UTF_8);
	  }

	@Test
	public void MusicPieceValidTest() {
		assertAmbiguityFree(parser, test0);
		assertAmbiguityFree(parser, test1);
		assertAmbiguityFree(parser, test2);
		assertAmbiguityFree(parser, test3);
		assertAmbiguityFree(parser, test4);
		assertAmbiguityFree(parser, test5);
		
	}

}