package de.p2l.service.parser.commands;
import android.os.Parcelable;

import de.p2l.service.parser.visitor.CommandVisitor;

public abstract class Command implements Parcelable {

	public abstract void accept(CommandVisitor visitor);
	
	@Override
	public String toString() {
		return "Command of Type: ";
	}

	public String getClassName() {
		return "Weder Branch, noch Loop";
	}
}
