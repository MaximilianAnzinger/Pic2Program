package de.p2l.ui.ingame.libgdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

import de.p2l.service.parser.commands.Command;

public class Pic2Learn extends Game{

	public interface MyGameCallback{
		public void startActivityWin(String level, int numberOfCommands);
	}

	public SpriteBatch batch;
	//private ArrayList<Command> commands;
	private ArrayList<Command> commands;
	private String level;
	public MyGameCallback callback;
	private int numberOfCommands;
	private int skin;

	@Override
	public void create () {
		batch = new SpriteBatch();
		LibgdxScreen libgdxScreen = new LibgdxScreen(this,level,commands,numberOfCommands, skin);
		setScreen(libgdxScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void setCommands(ArrayList<Command> commands){
		this.commands = commands;
	}

	public void setLevel(String level){
		this.level = level;
	}

	public void setNumberOfCommands(int numberOfCommands){
		this.numberOfCommands = numberOfCommands;
	}

	public void setMyGameCallback(MyGameCallback callback){
		this.callback=callback;
	}

	public void setSkin(int skin){
		this.skin = skin;
	}

}
