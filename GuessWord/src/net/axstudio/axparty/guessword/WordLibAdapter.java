package net.axstudio.axparty.guessword;

import java.util.Locale;

import android.content.Context;

class WordLibAdapter
{
	Context mContext;
	WordLibEntry mEntry;

	public WordLibAdapter(Context context, WordLibEntry entry)
	{
		mContext = context;
		mEntry = entry;
	}

	@Override
	public String toString()
	{
		return String.format(Locale.getDefault(), this.mContext
				.getResources().getString(R.string.word_num_name),
				mEntry.mNumChars);
	}
}