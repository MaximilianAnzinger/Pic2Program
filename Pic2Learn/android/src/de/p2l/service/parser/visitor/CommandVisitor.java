package de.p2l.service.parser.visitor;
import de.p2l.service.parser.commands.Branch;
import de.p2l.service.parser.commands.Falling;
import de.p2l.service.parser.commands.Idle;
import de.p2l.service.parser.commands.Interaction;
import de.p2l.service.parser.commands.Loop;
import de.p2l.service.parser.commands.StepBackward;
import de.p2l.service.parser.commands.StepForward;
import de.p2l.service.parser.commands.TurnLeft;
import de.p2l.service.parser.commands.TurnRight;
import de.p2l.service.parser.commands.Win;


public interface CommandVisitor {
	void visit(Branch branch);
	void visit(Interaction interaction);
	void visit(Loop loop);
	void visit(StepBackward stepbackward);
	void visit(StepForward stepforward);
	void visit(TurnLeft turnleft);
	void visit(TurnRight turnright);
	void visit(Idle idle);
    void visit(Falling falling);
	void visit(Win win);
}
