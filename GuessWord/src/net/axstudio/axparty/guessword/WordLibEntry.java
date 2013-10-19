package net.axstudio.axparty.guessword;

import java.util.Vector;

class WordLibEntry
{

	int numChars;
	final Vector<WordLibElement> mElements = new Vector<WordLibElement>();
	WordLibEntry()
	{

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

//	public String toString()
//	{
//		return Integer.toString( numChars );
//		
////		return String.format(Locale.getDefault(), this.mContext.getResources()
////				.getString(R.string.word_num_name), numChars);
//	}
}
