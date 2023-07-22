package de.p2l.service.parser.test;

import java.util.ArrayList;

import de.p2l.service.parser.commands.Command;
import de.p2l.service.parser.parser.Input;
import de.p2l.service.parser.parser.Parser;
import de.p2l.service.parser.visitor.CommandTestVisitor;

public class Test1 {

	public static void main(String[] args) {
		//System.out.println("Test simple Input: F-B-L-R-I");
		//testInput(simpleInput());
		//System.out.println();
		
		/*Don't test until Input-Check is implemented! (while-true-loop); der parse stimmt aber*/
//		System.out.println("Test simple Loop with True-Condition");
//		testInput(simpleLoopTrue());
//		System.out.println();
		
		/*false-part won't be printed twice!*/
		/*System.out.println("Test simple branch with 2 parts, true-condition");
		testInput(simpleBranchTrueBothparts());
		System.out.println();
	
		System.out.println("Test simple branch with single part, true-condition");
		testInput(simpleBranchTrueSinglepart());
		System.out.println();
		
		/*Don't test until Input-Check is implemented! (while-true-loop); der parse stimmt aber*/
//		System.out.println("Test blocked/free-conditions with loop");
//		testInput(conditionsLoop());
//		System.out.println();;
		
		/*System.out.println("Test blocked/free-condiitons with branch");
		testInput(conditionsBranch());
		System.out.println();*/

//		System.out.println("Test distorted ControlBlocks");
//		testInput(distControlBlocks());
//		System.out.println();

		//Tests nach Sprachänderungen
		/*System.out.println("Test new language Loop: L_Fr-F");
		testInput(loopNew1());
		System.out.println();

		System.out.println("Test new language Branch: B_Fr-F-B:-B");
		testInput(branchNew1());
		System.out.println();

		System.out.println("Test Fail with Loop: No Condition");
		try{
			testInput(failLoop1());
		} catch (Exception e){
			System.out.println(e);
		}
		System.out.println();

		System.out.println("Test Fail: Block not closed - Loop");
		try{
			testInput(failLoop2());
		} catch (Exception e){
			System.out.println(e);
		}

		System.out.println("Test Fail with Branch: No Condition");
		try{
			testInput(failBranch1());
		} catch (Exception e){
			System.out.println(e);
		}
		System.out.println();

		System.out.println("Test Fail: Block not closed - Branch");
		try{
			testInput(failBranch2());
		} catch (Exception e){
			System.out.println(e);
		}
		System.out.println();*/

		System.out.println("Test new Bug 1");
		testInput(testNewBug1());
	}

	private static ArrayList<Input> testNewBug1(){
		ArrayList<Input> in = new ArrayList<>();
		in.add(Input.LOOPKEY);
		in.add(Input.GOALIN);
		in.add(Input.FORWARD);
		in.add(Input.BLOCKEND);
		in.add(Input.FORWARD);
		return in;
	}

	private static ArrayList<Input> loopNew1(){
		ArrayList<Input> in = new ArrayList<Input>();
		in.add(Input.LOOPKEY);
		in.add(Input.FREEIN);
		in.add(Input.FORWARD);
		in.add(Input.BLOCKEND);

		return in;
	}

	private static ArrayList<Input> branchNew1(){
		ArrayList<Input> in = new ArrayList<Input>();
		in.add(Input.BRANCHKEY);
		in.add(Input.FREEIN);
		in.add(Input.FORWARD);
		in.add(Input.BRANCHKEY);
		in.add(Input.BACKWARD);
		in.add(Input.BLOCKEND);

		return in;
	}

	private static ArrayList<Input> failLoop1(){
		ArrayList<Input> in = new ArrayList<Input>();
		in.add(Input.LOOPKEY);
		in.add(Input.FORWARD);
		in.add(Input.BLOCKEND);

		return in;
	}

	private static ArrayList<Input> failLoop2(){
		ArrayList<Input> in = new ArrayList<Input>();
		in.add(Input.LOOPKEY);
		in.add(Input.FREEIN);
		in.add(Input.FORWARD);

		return in;
	}

	private static ArrayList<Input> failBranch1(){
		ArrayList<Input> in = new ArrayList<Input>();
		in.add(Input.BRANCHKEY);
		in.add(Input.FORWARD);
		in.add(Input.BRANCHKEY);
		in.add(Input.BACKWARD);
		in.add(Input.BLOCKEND);

		return in;
	}

	private static ArrayList<Input> failBranch2(){
		ArrayList<Input> in = new ArrayList<Input>();
		in.add(Input.BRANCHKEY);
		in.add(Input.GOALIN);
		in.add(Input.FORWARD);
		in.add(Input.BACKWARD);

		return in;
	}

	private static ArrayList<Input> simpleInput() {
		ArrayList<Input> input = new ArrayList<Input>();
		input.add(Input.FORWARD);
		input.add(Input.BACKWARD);
		input.add(Input.LEFTTURN);
		input.add(Input.RIGHTTURN);
		input.add(Input.INTERACT);
		
		return input;
	}
	
	private static ArrayList<Input> simpleLoopTrue() {
		ArrayList<Input> input = new ArrayList<Input>();
		input.add(Input.LOOPKEY);
		input.add(Input.LOOPKEY);
		input.add(Input.FORWARD);
		input.add(Input.BLOCKEND);
		
		return input;
	}
	
	private static ArrayList<Input> simpleBranchTrueBothparts() {
		ArrayList<Input> input = new ArrayList<Input>();
		input.add(Input.BRANCHKEY);
		input.add(Input.BRANCHKEY);
		input.add(Input.FORWARD);
		input.add(Input.BRANCHKEY);
		input.add(Input.BACKWARD);
		input.add(Input.BLOCKEND);
		
		return input;
	}
	
	private static ArrayList<Input> simpleBranchTrueSinglepart() {
		ArrayList<Input> input = new ArrayList<Input>();
		input.add(Input.BRANCHKEY);
		input.add(Input.BRANCHKEY);
		input.add(Input.FORWARD);
		input.add(Input.BLOCKEND);
		
		return input;
	}
	
	/*private static ArrayList<Input> conditionsLoop() {
		ArrayList<Input> input = new ArrayList<>();
		input.add(Input.LOOPKEY);
		input.add(Input.CONDITIONKEY);
		input.add(Input.LOOPKEY);
		input.add(Input.BLOCKEND);
		input.add(Input.FORWARD);
		input.add(Input.LOOPKEY);
		input.add(Input.CONDITIONKEY);
		input.add(Input.CONDITIONKEY);
		input.add(Input.LOOPKEY);
		input.add(Input.BLOCKEND);
		
		
		return input;
	}*/
	
	/*private static ArrayList<Input> conditionsBranch() {
		ArrayList<Input> input = new ArrayList<>();
		input.add(Input.BRANCHKEY);
		input.add(Input.CONDITIONKEY);
		input.add(Input.BRANCHKEY);
		input.add(Input.BLOCKEND);
		input.add(Input.FORWARD);
		input.add(Input.BRANCHKEY);
		input.add(Input.CONDITIONKEY);
		input.add(Input.CONDITIONKEY);
		input.add(Input.BRANCHKEY);
		input.add(Input.BLOCKEND);
		
		
		return input;
	}*/
	
	private static ArrayList<Input> distControlBlocks() {
		ArrayList<Input> input = new ArrayList<Input>();
		input.add(Input.LOOPKEY);
		input.add(Input.LOOPKEY);
		input.add(Input.BRANCHKEY);
		input.add(Input.BRANCHKEY);
		input.add(Input.FORWARD);
		input.add(Input.BRANCHKEY);
		input.add(Input.BACKWARD);
		input.add(Input.BLOCKEND);
		input.add(Input.LEFTTURN);
		input.add(Input.BLOCKEND);
		input.add(Input.RIGHTTURN);
		
		return input;
	}
	
	/* Runs the given input with the CommandTestVistor
	 * @param
	 * input: input to be checked
	 */
	private static void testInput(ArrayList<Input> input) {
		Parser p = new Parser(input);
		p.parse();
		CommandTestVisitor visitor = new CommandTestVisitor();
		
		for(Command c: p.getCommands()) {
			c.accept(visitor);
		}
	}
}
