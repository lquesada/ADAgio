package com.adagio.language.figures;

import java.util.Arrays;

import org.modelcc.Constraint;
import org.modelcc.IModel;
import org.modelcc.Multiplicity;
import org.modelcc.Optional;
import org.modelcc.types.IntegerModel;


public class Figure implements IModel {
	
	IntegerModel shape;

	/**
	 * @return True is duration is pow of 2. False in other case.
	 */
	@Constraint
	boolean validShape() {
		int result = 0;
		boolean isValid = false;
		
		if(shape.intValue() <= 0 || shape.intValue() > 128){
			isValid = false;
		}
		else{
			for (int i = 0; result < shape.intValue(); i++) {
				result = (int) Math.pow(2.0, i);
				if (result == shape.intValue()) {
					isValid = true;
				}
			}
		}
		
		if(isValid == false){
			System.err.println("Error 11: \"" + shape.intValue() +"\" it's not a valid figure shape.\n");
			System.exit(11);
		}
		return isValid;
	}

	@Optional
	@Multiplicity(minimum = 1)
	FigureDot [] dots;
	
	/**
	 * Default constructor.
	 */
	public Figure(){
		shape = new IntegerModel(4);
		dots = null;
	}
	
	public Figure(Figure figure){
		this.shape = figure.shape;
		if(figure.dots != null){
			this.dots = Arrays.copyOf(figure.dots, figure.dots.length);
		}
	}
	
	/**
	 * Attributes constructor.
	 * @param shape duration of the shape 
	 * @param dots Vector of dots
	 */
	public Figure(IntegerModel shape, FigureDot [] dots){
		
		this.shape = shape;
		if(dots != null){
			this.dots= dots;
		}
	}
	
	/**
	 * Primitive constructor.
	 * @param shape int value duration of the shape
	 * @param dotsNum Number of dots
	 */
	public Figure(int shape, int dotsNum) {

		this.shape = new IntegerModel(shape);
		if(dotsNum > 0){
			this.dots = new FigureDot[dotsNum];
			for(int i = 0; i < dotsNum; i++){
				dots[i] = new FigureDot();
			}
		}
		if (!this.validShape()) {
			System.err.println("Error 11: \"" + shape + "\" it's not a valid shape duration.\n");
			System.exit(11);
		}
	}
	

	/**
	 * If duration is valid, it will create a figure with the indicated duration.
	 * If duration is not valid, it will create a figure with null attributes;
	 * @param duration Figure's duration. (4 whole note, 2 half note, ...)
	 */
	public Figure(double duration){
		double figDur = Double.MAX_VALUE;
		int dotsNum = 0;
		Figure figure = null;
		
		for(int i = 0; figDur > duration; i++){
			figure = (new Figure((int)Math.pow(2.0, i),0));
			figDur = figure.duration();
		}
		
		double originalFigDur = figDur;
		for(int i = 0; figDur < duration && i < 8; i++){
			figDur += originalFigDur / (Math.pow(2.0, i+1));
			dotsNum++;
		}
		
		if(figDur == duration){
			this.shape = figure.shape;
			if(dotsNum > 0){
				this.dots = new FigureDot[dotsNum];
			}
			for(int i = 0; i < dotsNum; i++){
				dots[i] = new FigureDot();
			}
		}
		else{
			this.shape = null;
			this.dots = null;
			System.err.println("Warning 12: \"" + duration+ "\" it's not a valid duration");
		}
	}
	
	/**
	 * Calculates the figure's duration (4 whole note, 2 half note, ...)
	 * @return
	 */
	public double duration(){
		double baseDuration = 4.0/this.shape.intValue();
		double duration = baseDuration;
		
		if(this.dots != null){
			for(int i = 0; i < this.dots.length;i++){
				duration += baseDuration/(Math.pow(2.0, i+1));
			}
		}
		return duration;
	}
	
	/**
	 * Checks if a duration is a valid one
	 * @param duration Figure's duration. (4 whole note, 2 half note, ...)
	 * @return True if is valid. False in other case.
	 */
 	static public boolean validDuration(double duration){
 		double figDur = Double.MAX_VALUE;
		
		for(int i = 0; figDur > duration; i++){
			figDur = (new Figure((int)Math.pow(2.0, i),0)).duration();
		}
		
		double originalFigDur = figDur;
		for(int i = 0; figDur < duration; i++){
			figDur += originalFigDur / (Math.pow(2.0, i+1));
		}
		
		if(figDur == duration){
			return true;
		}
		else{
			return false;
		}
	}
	
	public IntegerModel getShape() {
		return shape;
	}

	public void setShape(IntegerModel shapeDuration) {
		this.shape = shapeDuration;
	}

	public FigureDot[] getDots() {
		return dots;
	}

	public int getNumDots(){
		if (dots == null){
			return 0;
		}
		return dots.length;
	}
	
	public void setDots(FigureDot[] dots) {
		this.dots = dots;
	}
	
	public Figure clone(){
		Figure cloned = new Figure(this.shape,this.dots);
		return cloned;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(dots);
		result = prime * result + ((shape == null) ? 0 : shape.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Figure other = (Figure) obj;
		if (!Arrays.equals(dots, other.dots))
			return false;
		if (shape == null) {
			if (other.shape != null)
				return false;
		} else if (shape.intValue() != other.shape.intValue())
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		String composition = "";
		composition += shape.toString();
		if(dots != null){
			for(FigureDot current: dots){
				composition += current.toString(); 
			}
		}
		return composition;
	}
	
	/**
	 * Creates a figure with the specific duration if is possible. If is not
	 * possible, create the closer and immediately lower one.
	 * @param duration Figure's duration. (4 whole note, 2 half note, ...)
	 * @return The figure with the specific duration or the closer lower one, with
	 * a maximum of 2 dots
	 * It will return null if the duration it's smaller than the duration of 128Figure
	 */
	public static Figure closerFigure(double duration){
		double figDur = Double.MAX_VALUE;
		int dotsNum = 0;
		Figure figure = null;
		
		for(int i = 0; figDur > duration && i < 8; i++){
			figure = (new Figure((int)Math.pow(2.0, i),0));
			figDur = figure.duration();
		}
		
		if(figDur > duration){
			return null;
		}
		
		double originalFigDur = figDur;
		double lastIncrement = 0;
		for(int i = 0; figDur < duration && dotsNum < 2; i++){
			lastIncrement = originalFigDur / (Math.pow(2.0, i+1));
			figDur += lastIncrement;
			dotsNum++;
		}
		
		if(figDur > duration){
			dotsNum--;
		}
		
		return new Figure(figure.shape.intValue(), dotsNum);
	}
	
	
}

