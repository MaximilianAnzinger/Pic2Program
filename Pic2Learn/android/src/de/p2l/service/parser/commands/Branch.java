package de.p2l.service.parser.commands;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import de.p2l.service.parser.visitor.CommandVisitor;

public class Branch extends Command{

    public static final Parcelable.Creator<Branch> CREATOR = new Parcelable.Creator<Branch>() {
      public Branch createFromParcel(Parcel in) {
          return new Branch(in);
      }

      public Branch[] newArray(int size){
          return new Branch[size];
      }
    };

	private Condition condition;
	private ArrayList<Command> trueStmts;
	private ArrayList<Command> falseStmts;
	private boolean intrue;
	
	public Branch() {
		condition = null;
		trueStmts = new ArrayList<Command>();
		falseStmts = new ArrayList<Command>();
		intrue = true;
	}

	private Branch(Parcel in){
	    condition = Condition.values()[in.readInt()];
	    trueStmts = in.readArrayList(Command.class.getClassLoader());
	    falseStmts = in.readArrayList(Command.class.getClassLoader());
	    intrue = (falseStmts.size() == 0 ? true : false);
    }
	
	@Override
	/**
	 * Debugging-Methode for Parser
	 */
	public void accept(CommandVisitor visitor) {
		visitor.visit(this);
		for(Command c: trueStmts) {
			c.accept(visitor);
		}
		for(Command c: falseStmts) {
			c.accept(visitor);
		}
	}
	
	@Override
	public String toString() {
		String out = super.toString();
		out += "Branch";
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
		if(!trueStmts.isEmpty()) {
			out += "\nContained Commands True-Part\n";
			for(Command c: trueStmts) {
				out +="\t";
				out += c.toString();
			}
		} else {
			out += "\nNo Commands in True-Part\n";
		}
		if(!intrue) {
			if(!falseStmts.isEmpty()) {
				out += "Contained Commands False-Part\n";
				for(Command c: falseStmts) {
					out += "\t";
					out += c.toString();
				}
			} else {
				out += "No Commands in Fals-part\n";
			}
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
	
	public ArrayList<Command> getTrueStmts() {
		return trueStmts;
	}
	
	public void setTrueStmts(ArrayList<Command> newStmts) {
		trueStmts = newStmts;
	}
	
	
	public ArrayList<Command> getFalseStmts() {
		return falseStmts;
	}
	
	public void setFalseStmts(ArrayList<Command> newStmts) {
		falseStmts = newStmts;
	}

	
	/**
	 * adds new command to activ part (if/else)
	 **/
	public void addCommand(Command c) {
		if(intrue) {
			trueStmts.add(c);
		} else {
			falseStmts.add(c);
		}
	}
	
	/**
	 * changes from if to else part
	 **/
	public void setFalse() {
		intrue = false;
	}
	
	public boolean getIntrue() {
		return intrue;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(condition.ordinal());
		dest.writeList(trueStmts);
		dest.writeList(falseStmts);
	}

	@Override
	public String getClassName(){
		return "Verzweigung";
	}
}
