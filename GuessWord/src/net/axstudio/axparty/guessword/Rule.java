package net.axstudio.axparty.guessword;

public class Rule
{

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

	public Rule()
	{

		mPlayerInfos = new PlayerTypeInfo[PlayerType.values().length];
		for (PlayerType type : PlayerType.values())
		{
			PlayerTypeInfo info = new PlayerTypeInfo();
			info.playerType = type;
			info.numPlayers = 0;
			mPlayerInfos[type.ordinal()] = info;
		}
	}

	public Rule(int[] numPlayers)
	{
		this();

		resetPlayerNumbers(numPlayers != null ? numPlayers : new int[] { 3, 2,
				2 });
	}

	public Rule(int major, int minor, int idiot)
	{
		this(new int[] { major, minor, idiot });

	}

	public Rule(PlayerTypeInfo[] info)
	{
		this();
		for (PlayerTypeInfo i : info)
		{
			setNumPlayersByType(i.playerType, i.numPlayers);
		}

	}

	public PlayerTypeInfo[] getData()
	{
		return mPlayerInfos;
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

	public void setNumPlayersByType(PlayerType type, int numPlayer)
	{
		mPlayerInfos[type.ordinal()].numPlayers = numPlayer;

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

	// public String getDesciption()
	// {
	// return String.format(Locale.getDefault(),
	// mContext.getString(R.string.rule_name), getTotalPlayers(),
	// getNumPlayersByType(PlayerType.MAJOR),
	// getNumPlayersByType(PlayerType.MINOR),
	// getNumPlayersByType(PlayerType.IDIOT));
	//
	// }

	// public String toString()
	// {
	// return getDesciption();
	// }

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

	public int[] getNumPlayers()
	{
		int[] r = new int[PlayerType.values().length];
		for (PlayerTypeInfo t : mPlayerInfos)
			r[t.playerType.ordinal()] += t.numPlayers;
		return r;
	}

}
