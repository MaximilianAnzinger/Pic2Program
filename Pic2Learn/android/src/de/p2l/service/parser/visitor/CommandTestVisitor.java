package de.p2l.service.parser.visitor;

import de.p2l.service.parser.commands.Branch;
import de.p2l.service.parser.commands.Idle;
import de.p2l.service.parser.commands.Interaction;
import de.p2l.service.parser.commands.Loop;
import de.p2l.service.parser.commands.StepBackward;
import de.p2l.service.parser.commands.StepForward;
import de.p2l.service.parser.commands.TurnLeft;
import de.p2l.service.parser.commands.TurnRight;
import de.p2l.service.parser.commands.Win;
import de.p2l.service.parser.commands.Falling;

//Visitor for String output (debug)
public class CommandTestVisitor implements CommandVisitor {

	@Override
	public void visit(Branch branch) {
		System.out.print(branch);
	}

	@Override
	public void visit(Interaction interaction) {
		System.out.print(interaction);
	}

	@Override
	public void visit(Loop loop) {
		System.out.print(loop);
	}

	@Override
	public void visit(StepBackward stepbackward) {
		System.out.print(stepbackward);
	}

	@Override
	public void visit(StepForward stepforward) {
		System.out.print(stepforward);
	}

	@Override
	public void visit(TurnLeft turnleft) {
		System.out.print(turnleft);
	}

	@Override
	public void visit(TurnRight turnright) {
		System.out.print(turnright);
	}
	
	@Override
	public void visit(Idle idle) {
		System.out.print(idle);
	}

	@Override
	public void visit(Win win){
		System.out.println(win);
	}

	@Override
	public void visit(Falling falling){
		System.out.println(falling);
	}

}
