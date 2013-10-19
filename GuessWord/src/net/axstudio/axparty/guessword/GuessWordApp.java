package net.axstudio.axparty.guessword;

import android.app.Application;

public class GuessWordApp extends Application
{

	private WordLib mWordLib;

	public GuessWordApp()
	{
		super();

	}

	public Rule[] getDefaultRules()
	{
		Rule[] rules = { new Rule(this, 2, 1, 1), new Rule(this, 3, 1, 1),
				new Rule(this, 3, 2, 1), new Rule(this, 3, 2, 2),
				new Rule(this, 4, 2, 2), new Rule(this, 5, 2, 2),
				new Rule(this, 6, 2, 2), };

		return rules;
	}


	public WordLib getWordLib()
	{
		if (null == mWordLib)
		{
			mWordLib = new WordLib(this);
			mWordLib.load();
		}
		return mWordLib;
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		// loadWordLib();
	}


}
