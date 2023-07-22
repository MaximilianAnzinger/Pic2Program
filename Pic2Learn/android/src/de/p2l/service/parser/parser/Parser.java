package de.p2l.service.parser.parser;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.Set;

import de.p2l.service.parser.commands.Branch;
import de.p2l.service.parser.commands.Command;
import de.p2l.service.parser.commands.Condition;
import de.p2l.service.parser.commands.Interaction;
import de.p2l.service.parser.commands.Loop;
import de.p2l.service.parser.commands.StepBackward;
import de.p2l.service.parser.commands.StepForward;
import de.p2l.service.parser.commands.TurnLeft;
import de.p2l.service.parser.commands.TurnRight;

public class Parser {

	private ArrayList<Input> input;
	private ArrayList<Command> commands;
	private Stack<Command> stack;
	private ArrayList<Set<Integer>> indexPairs;
	private Stack<Integer> indexStack;

	private static final String LOG_TAG = "Parser";
	
	public Parser() {
		input = new ArrayList<Input>();
		indexPairs = new ArrayList<>();
		indexStack = new Stack<>();
	}

	/**
	 * creats <object>Parser</object> with given Input
	 * @param in given Input
	 */
	public Parser(ArrayList<Input> in) {
		input = in;
		indexPairs = new ArrayList<>();
		indexStack = new Stack<>();
	}
	
	/**
	 * Parses given input and saves the result in commands
	 * @return result of the parse
	 */
	public String parse() {
		Log.i(LOG_TAG, "Starting normal parse");
		//cleanup from last Parse
		commands = new ArrayList<Command>();
		stack = new Stack<Command>();
		
		Command c;
		for(int i = 0; i<input.size(); i++) {
			switch(input.get(i)) {
			
			case BACKWARD:
				if(stack.empty()) {
					commands.add(new StepBackward());
				} else {
					c = stack.peek();
					if(c instanceof Loop) {
						((Loop) c).addLoopCommand(new StepBackward());
					} else if(c instanceof Branch) {
						((Branch) c).addCommand(new StepBackward());
					} else {
						throw new RuntimeException("Shouldn't be on the stack "+c);
					}
				}
				break;
				
			case BLOCKEND:
				if(stack.empty()) {
					return "Alle Kontrollblöcke sind bereits geschlossen, Blockende ist hier falsch! Index "+i;
				} else {
					stack.pop();
				}
				break;

			case BRANCHKEY:
				//new Branch with Condition ...
				if(i<input.size()-1 ) {
					Branch b = new Branch();
					boolean swap;
					//set condition
					switch(input.get(i+1)) {
					case FREEIN:
						b.setCondition(Condition.FREE);
						i++;
						swap = false;
						break;
					case BLOCKEDIN:
						b.setCondition(Condition.BLOCKED);
						i++;
						swap = false;
						break;
					case GOALIN:
						b.setCondition(Condition.GOAL);
						i++;
						swap = false;
						break;
					case NOGOALIN:
						b.setCondition(Condition.NOGOAL);
						i++;
						swap = false;
						break;
					default:
						swap = true;
					}

					//swap to else-part
					if(swap){
						if(stack.empty()) {
							//no condition/early close(swap to else)/missplaced
							return "Die Verzweigung wird falsch verwendet. Index "+i;
						}
						c = stack.peek();
						if(c instanceof Branch) {
							((Branch) c).setFalse();
						} else {
							//no condition/early close(swap to else)/missplaced
							return "Die Verzweigung wird falsch verwendet. Index "+i;
						}
					// create new branch
					} else {
						if(stack.empty()) {
							commands.add(b);
							stack.push(b);
						} else {
							c = stack.peek();
							if(c instanceof Loop) {
								((Loop) c).addLoopCommand(b);
								stack.push(b);
							} else if(c instanceof Branch) {
								((Branch) c).addCommand(b);
								stack.push(b);
							} else {
								throw new RuntimeException("Shouldn't be on the stack "+c);
							}
						}
					}
				// Programm can't stop with branch-symbol
				} else {
					return "Das Verzweigungssymbol kann nicht die letzte Eingabe sein. Index "+i;
				}
				break;

			case FORWARD:
				if(stack.empty()) {
					commands.add(new StepForward());
				} else {
					c = stack.peek();
					if(c instanceof Loop) {
						((Loop) c).addLoopCommand(new StepForward());
					} else if(c instanceof Branch) {
						((Branch) c).addCommand(new StepForward());
					} else {
						throw new RuntimeException("Shouldn't be on the stack "+c);
					}
				}
				break;
				
			case INTERACT:
				if(stack.empty()) {
					commands.add(new Interaction());
				} else {
					c = stack.peek();
					if(c instanceof Loop) {
						((Loop) c).addLoopCommand(new Interaction());
					} else if(c instanceof Branch) {
						((Branch) c).addCommand(new Interaction());
					} else {
						throw new RuntimeException("Shouldn't be on the stack "+c);
					}
				}
				break;
				
			case LEFTTURN:
				if(stack.empty()) {
					commands.add(new TurnLeft());
				} else {
					c = stack.peek();
					if(c instanceof Loop) {
						((Loop) c).addLoopCommand(new TurnLeft());
					} else if(c instanceof Branch) {
						((Branch) c).addCommand(new TurnLeft());
					} else {
						throw new RuntimeException("Shouldn't be on the stack "+c);
					}
				}
				break;

			case LOOPKEY:
				Loop l = new Loop();
				if(stack.empty()) {
					commands.add(l);
					stack.push(l);
				} else {
					c = stack.peek();
					if(c instanceof Loop) {
						((Loop) c).addLoopCommand(l);
						stack.push(l);
					} else if(c instanceof Branch) {
						((Branch) c).addCommand(l);
						stack.push(l);
					} else {
						throw new RuntimeException("Shouldn't be on the stack "+c);
					}
				}
				//set condition
				if(i<input.size()-1) {
					switch(input.get(i+1)){
						case FREEIN:
							l.setCondition(Condition.FREE);
							i++;
							break;
						case GOALIN:
							l.setCondition(Condition.GOAL);
							i++;
							break;
						case BLOCKEDIN:
							l.setCondition(Condition.BLOCKED);
							i++;
							break;
						case NOGOALIN:
							l.setCondition(Condition.NOGOAL);
							i++;
							break;
						default:
							return "Die Schleife an Position "+i+" hat keine Bedingung.";
					}

				} else if(i==input.size()-1) {
					return "Die Schleife hat keine Bedinung, keinen Inhalt und keinen Abschluss.";
				}
				break;
				
			case RIGHTTURN:
				if(stack.empty()) {
					commands.add(new TurnRight());
				} else {
					c = stack.peek();
					if(c instanceof Loop) {
						((Loop) c).addLoopCommand(new TurnRight());
					} else if(c instanceof Branch) {
						((Branch) c).addCommand(new TurnRight());
					} else {
						throw new RuntimeException("Shouldn't be on the stack "+c);
					}
				}
				break;
				
			case FREEIN:
			case BLOCKEDIN:
			case GOALIN:
			case NOGOALIN:
				return "Bedingungen können nicht ohne Verzweigung oder Schleife auftreten";
				
			default:
				throw new RuntimeException("Unknown input "+input.get(i));
			}
		}
		if(!stack.empty()) {
			return "Nicht alle Kontrollblöcke sind abgeschlossen";
		}

		Log.i(LOG_TAG, "Normal parse successfully completed");
		return "Die Eingabe ist ausführbar";
	}
	
	/**
	 * @return: parsed Commands
	 */
	public ArrayList<Command> getCommands() {
		return commands;
	}
	
	/**
	 * @param in: complete Input for parser
	 */
	public void setInput(ArrayList<Input> in) {
		input = in;
	}

	/**
	 * adds single input and executes checkParser. (care: object oriented)
	 * this method shouldn't be used
	 * @param in new input
	 * @return result of checkParser
	 */
	public String evalNewInput(Input in) {
		input.add(in);
		return this.checkParser();
	}

	/**
	 * returns list of index sets (L-E,B1-B2,B1-E), adds not opened/closed indices.
	 * Only request indexPairs via this method!
	 * @return list of index sets
	 */
	public ArrayList<Set<Integer>> getIndexPairs(){
		while(!indexStack.empty()){
			Set<Integer> tmp = new HashSet<>();
			tmp.add(indexStack.pop());
			indexPairs.add(tmp);
		}
		return this.indexPairs;
	}

	/**
	 * parses input, returns feedback and computes index sets
	 * @return first mistake in input explained as string
	 */
	public String checkParser(){
		Log.i(LOG_TAG, "Starting check-parse");

		//cleanup from last parse
		stack = new Stack<Command>();
		indexStack = new Stack<>();
		indexPairs = new ArrayList<>();

		Set<Integer> tmpSet;

		for(int i = 0; i<input.size(); i++){
			switch(input.get(i)){
				case LOOPKEY:
					if(i==input.size()-1){
						return "Es fehlen eine Bedigung, der Inhalt der Schleife und deren Ende. Position der Schleife: "+i;
					} else {
						switch(input.get(i+1)){
							case NOGOALIN:
							case GOALIN:
							case FREEIN:
							case BLOCKEDIN:
								i++;
								break;
							default:
								return "Die Schleife an Position "+i+" hat keine Bedingung";
						}
						stack.push(new Loop());
						indexStack.push(new Integer(i-1));
					}
					break;
				case BRANCHKEY:
					if(i==input.size()-1) {
						if (stack.empty()) {
							indexStack.push(new Integer(i));
							return "Es fehlen eine Bedingung, der Inhalt der Verzweigung, optional ein weiterer Zweig und das Ende. Position der Verzweigung: " + i;
						} else if (stack.peek() instanceof Branch && ((Branch) stack.peek()).getIntrue()) {
							//swap in unfinished branch
							indexStack.add(new Integer(i));
							return "Die Bedingung braucht noch Befehle für den 2. Zweig (optional) und muss abgeschlossen werden. Position des Beginns des zweiten Zweigs: " + i;
						} else if (stack.peek() instanceof  Branch ) {
							//new branch in else part of another branch
							indexStack.push(new Integer(i));
							return "Es fehlen eine Bedingung, der Inhalt der Verzweigung, optional ein weiterer Zweig und das Ende. Position der Verzweigung: " + i;
						}else {
							indexStack.push(new Integer(i));
							return "Es fehlen eine Bedingung, der Inhalt der Verzweigung, optional ein weiterer Zweig und das Ende. Position der Verzweigung: "+i;
						}
					} else {
						switch(input.get(i+1)){
							case NOGOALIN:
							case GOALIN:
							case FREEIN:
							case BLOCKEDIN:
								i++;
								stack.push(new Branch());
								indexStack.push(new Integer(i-1));
								break;
							default:
								if (!stack.empty() && stack.peek() instanceof Branch){
									if(!((Branch) stack.peek()).getIntrue()){
										//else part already exists
										indexStack.add(new Integer(i));
										return "Der Verzweigung an Position "+i+" fehlt eine Bedingung";
									} else {
										//swap to else part
										((Branch) stack.peek()).setFalse();
										indexStack.add(new Integer(i));
									}
								} else {
									indexStack.push(new Integer(i));
									return "Der Verzweigung an Position "+i+" fehlt eine Bedingung";
								}
						}
					}
					break;
				case BLOCKEND:
					if(stack.empty()){
						indexPairs.add(new HashSet<Integer>(new Integer(i)));
						return "Alle Kontrollblöcke sind bereits geschlossen! Abschluss an Position "+i+" wird nicht benötigt";
					} else {
						Command com = stack.pop();
						if(indexStack.empty()){				//shouldn't happen
							tmpSet = new HashSet<>();
							tmpSet.add(new Integer(i));
							indexPairs.add(tmpSet);
						} else {
							tmpSet = new HashSet<>();
							tmpSet.add(indexStack.pop());
							tmpSet.add(new Integer(i));
							if(com instanceof Branch){
								if(!((Branch) com).getIntrue() && !indexStack.empty()){
									tmpSet.add(indexStack.pop());
								}
							}
							indexPairs.add(tmpSet);
						}
					}
					break;
				case FREEIN:
				case BLOCKEDIN:
				case GOALIN:
				case NOGOALIN:
					return "Die Bedingung an Position " + i + " wird hier nicht gebraucht";
				default:
					//simple input can't be wrong
			}
		}
		if(stack.empty()){
			return "Der Code ist so ausführbar, aber bringt er dich auch zum Ziel?";
		} else {
			return "Ein Kontrollblock ist noch nicht abgeschlossen: "+stack.peek().getClassName();
		}
	}
}
