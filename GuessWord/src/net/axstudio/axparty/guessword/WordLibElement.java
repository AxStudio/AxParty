package net.axstudio.axparty.guessword;

class WordLibElement
{
	String key;
	String words[];

	public String[] genWord()
	{
		if (null == words)
			return null;
		final int WORD_SIZE = words.length;
		if (WORD_SIZE < 2)
			return null;

		while (true)
		{
			int i = (int) Math.floor(Math.random() * WORD_SIZE);
			int j = (int) Math.floor(Math.random() * WORD_SIZE);
			if (i == j)
				continue;
			return new String[] { words[i], words[j] };
		}
		// return null;

	}
}
