package net.axstudio.axparty.guessword;

import java.io.Serializable;
import java.util.Locale;
import java.util.Vector;

import android.content.Context;

class WordLibEntry implements Serializable
{
	private static final long serialVersionUID = 4939255979078222231L;

	Vector<WordLibElement> mElements;

	/**
	 * 
	 */
	private final Context mContext;
	int numChars;

	/**
	 * @param mContext
	 */
	WordLibEntry(Context context)
	{
		this.mContext = context;
	}

	public String[] genWord()
	{
		WordLibElement e = mElements.get((int) Math.floor(Math.random()
				* mElements.size()));

		String[] r = e.genWord();
		if (r == null)
			return null;
		String[] r3 = { r[0], r[1], e.key + r[0].length() };
		return r3;

	}

	public String toString()
	{
		return String.format(Locale.getDefault(), this.mContext.getResources()
				.getString(R.string.word_num_name), numChars);
	}
}
