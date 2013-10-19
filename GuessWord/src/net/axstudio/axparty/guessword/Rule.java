package net.axstudio.axparty.guessword;

import java.io.Serializable;
import java.util.Locale;

import android.content.Context;

public class Rule extends Object implements Cloneable, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 851395172726679830L;
	private Context mContext;

	enum PlayerType
	{

		MAJOR, MINOR, IDIOT,
	}

	class PlayerTypeInfo
	{
		PlayerType playerType;
		int numPlayers;

	}

	private PlayerTypeInfo[] mPlayerInfos;

	public Rule(Context context)
	{

		mContext = context;
		mPlayerInfos = new PlayerTypeInfo[PlayerType.values().length];
		for (PlayerType type : PlayerType.values())
		{
			PlayerTypeInfo info = new PlayerTypeInfo();
			info.playerType = type;
			info.numPlayers = 0;
			mPlayerInfos[type.ordinal()] = info;
		}
	}

	public Rule(Context context, int[] numPlayers)
	{
		this(context);

		resetPlayerNumbers(numPlayers != null ? numPlayers : new int[] { 3, 2, 2 });
	}

	public Rule(Context context, int major, int minor, int idiot)
	{
		this(context, new int[] { major, minor, idiot });

	}

	private void resetPlayerNumbers(int[] numPlayers)
	{
		if (null != numPlayers)
		{
			for (PlayerType type : PlayerType.values())
			{
				mPlayerInfos[type.ordinal()].numPlayers = numPlayers[type
						.ordinal()];
			}
		}

	}

	public int getNumPlayersByType(PlayerType type)
	{
		return mPlayerInfos[type.ordinal()].numPlayers;
	}

	public Rule clone() // throws CloneNotSupportedException
	{
		try
		{
			return (Rule) super.clone();

		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	// public boolean genPlayers(int totalPlayers, boolean noMinorities)
	// {
	//
	// if (totalPlayers < 2)
	// {
	// return false;
	// }
	//
	// int majorities = 1;
	// int minorities = 0;
	// int idiots = 1;
	//
	// while (majorities + minorities + idiots < totalPlayers)
	// {
	// if ( !noMinorities && idiots < minorities )
	// {
	// ++idiots;
	// continue;
	// }
	//
	// if ( !noMinorities && minorities < majorities )
	// {
	// ++minorities;
	// }
	//
	//
	// ++majorities;
	//
	// }
	//
	// mTotalPlayers = totalPlayers;
	// mMajorities = 0;
	// mMinorities = 0;
	// mIdiots = 0;
	//
	// return true;
	// }

	public String getDesciption()
	{
		return String.format(Locale.getDefault(),
				mContext.getString(R.string.rule_name), getTotalPlayers(),
				getNumPlayersByType(PlayerType.MAJOR),
				getNumPlayersByType(PlayerType.MINOR),
				getNumPlayersByType(PlayerType.IDIOT));

	}

	public String toString()
	{
		return getDesciption();
	}

	public int getTotalPlayers()
	{
		int i = 0;
		for (PlayerTypeInfo t : mPlayerInfos)
			i += t.numPlayers;
		return i;
	}

	public void resetPlayerNumbers(int major, int minor, int idiot)
	{
		int[] i = { major, minor, idiot };
		resetPlayerNumbers(i);
	}

}
