package de.p2l.service.parser.commands;

import android.os.Parcel;
import android.os.Parcelable;

import de.p2l.service.parser.visitor.CommandVisitor;

public class Interaction extends Command{

	public static final Parcelable.Creator<Interaction> CREATOR = new Parcelable.Creator<Interaction>(){
		public Interaction createFromParcel(Parcel in){
			return new Interaction();
		}

		public Interaction[] newArray(int size){
			return new Interaction[size];
		}
	};

	@Override
	public void accept(CommandVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		String out = super.toString();
		out += "Interaction\n";
		return out;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		//pass
	}
}
