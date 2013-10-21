package net.axstudio.axparty.guessword;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class GuessWordApp extends Application
{

	private WordLib mWordLib;

	public GuessWordApp()
	{
		super();

	}

	public Rule[] getDefaultRules()
	{
		Rule[] rules = { new Rule(2, 1, 1), new Rule(3, 1, 1),
				new Rule(3, 2, 1), new Rule(3, 2, 2), new Rule(4, 2, 2),
				new Rule(5, 2, 2), new Rule(6, 2, 2), };

		return rules;
	}

	public WordLib getWordLib()
	{
		if (null == mWordLib)
		{
			mWordLib = new WordLib(this);
			mWordLib.load(this);
		}
		return mWordLib;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		// loadWordLib();
	}

	static GuessWordApp getApp(Context content)
	{
		return (GuessWordApp)content.getApplicationContext();
	}
	public SharedPreferences getDefaultGameSetting()
	{
		return getSharedPreferences("DefaultGameSetting", Context.MODE_PRIVATE);
	}

}
