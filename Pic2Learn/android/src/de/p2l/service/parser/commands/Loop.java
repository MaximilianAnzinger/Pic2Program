package de.p2l.service.parser.commands;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import de.p2l.service.parser.visitor.CommandVisitor;

public class Loop extends Command{

    public static final Parcelable.Creator<Loop> CREATOR = new Parcelable.Creator<Loop>(){
        public Loop createFromParcel(Parcel in){
            return new Loop(in);
        }

        public Loop[] newArray(int size){
            return new Loop[size];
        }
    };

	private Condition condition;
	private ArrayList<Command> loopCommands;

	public Loop(){
		condition = null;
		loopCommands = new ArrayList<Command>();
	}

	private Loop(Parcel in){
	    condition = Condition.values()[in.readInt()];
	    loopCommands = in.readArrayList(Command.class.getClassLoader());
    }
	
	@Override
	/**
	 * Debugging-Methode for Parser
	 */
	public void accept(CommandVisitor visitor) {
		visitor.visit(this);
		for(Command c: loopCommands) {
			c.accept(visitor);
		}
	}
	
	@Override
	public String toString() {
		String out = super.toString();
		out += "Loop";
		out += "\nCondition ";
		switch(condition) {
		case BLOCKED:
			out += "isBlocked";
			break;
			
		case FREE:
			out += "isFree";
			break;
			
		case GOAL:
			out += "goal";
			break;
			
		case NOGOAL:
			out += "no goal";
			break;
			
		default:
			out += "notSet";
			break;
		
		}
		if(!loopCommands.isEmpty()) {
			out += "\nCommands in Loop\n";
			for(Command c: loopCommands) {
				out += "\t";
				out += c.toString();
			}
		} else {
			out += "\nNo Commands in Loop";
		}
		out += "\n";
		return out;
	}
	
	//getter/setter
	public Condition getCondition() {
		return condition;
	}
	
	public void setCondition(Condition newCondition) {
		condition = newCondition;
	}
	
	public ArrayList<Command> getLoopCommands() {
		return loopCommands;
	}
	
	public void setLoopCommands(ArrayList<Command> newCommands) {
		loopCommands = newCommands;
	}
	
	public void addLoopCommand(Command c) {
		loopCommands.add(c);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(condition.ordinal());
		dest.writeList(loopCommands);
	}

	@Override
	public String getClassName(){
		return "Schleife";
	}
}
