package net.axstudio.axparty.guessword;

import java.io.Serializable;
import java.util.Vector;

class WordLibElement implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7251630949800596354L;
	String key;
	Vector<String> words = new Vector<String>();

	public String[] genWord()
	{
		String[] r = { "", "" };
		r[0] = words.get((int) Math.floor(Math.random() * words.size()));
		for (int i = 0; i < 100; ++i)
		{
			r[1] = words
					.get((int) Math.floor(Math.random() * words.size()));
			if (r[0] != r[1])
				return r;
		}

		return null;

	}
}