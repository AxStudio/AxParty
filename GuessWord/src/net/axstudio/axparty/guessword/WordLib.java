package net.axstudio.axparty.guessword;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import android.content.Context;
import android.os.FileObserver;
import android.util.SparseArray;

public class WordLib
{

	static final byte MAGIC_NUMBER[] = { 'G', 'W', 'W', 'L' };
	static final int CURRENT_VERSION = 20131017;
	private static final String LOAD_BIN_PREFIX = "loadBin_V";
	private static final String WORDLIB_INDEX_FILE = "wordlib.idx";
	private static final String WORDLIB_DATA_FILE = "wordlib.dat";
	static final SparseArray<Method> READERS = new SparseArray<Method>()
	{
		{
			for (Method method : WordLib.class.getMethods())
			{
				if (method.getName().startsWith(LOAD_BIN_PREFIX))
				{
					try
					{
						int v = Integer.parseInt(method.getName().substring(
								LOAD_BIN_PREFIX.length()));
						put(v, method);
					}
					catch (NumberFormatException e)
					{
						continue;
					}
				}
			}
		}
	};
	private Context mContext;

	private final SparseArray<WordLibEntry> mEntries = new SparseArray<WordLibEntry>();

	public WordLib(Context context)
	{

		mContext = context;
	}

	private void loadBin(DataInputStream strm) throws IOException
	{
		int version = strm.readInt();
		Method method = READERS.get(version);
		if (method == null)
			throw (new IOException(String.format("version[%d] not supperted",
					version)));
		try
		{
			method.invoke(this, strm);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			throw (new IOException("invoke load method failed"));
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			throw (new IOException("invoke load method failed"));
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw (new IOException("invoke load method failed"));
		}

	}

	public void load()
	{
		try
		{
			DataInputStream strm = new DataInputStream(
					mContext.openFileInput("wordlib.bin"));
			{
				byte[] magic = new byte[4];
				strm.readFully(magic);
				if (!magic.equals(MAGIC_NUMBER))
					throw new IOException("invalid magic number");
			}
			{

				loadBin(strm);

			}
			return;

		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		loadFromText();
		saveBin();

	}

	public void loadFromText()
	{
		mEntries.clear();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				mContext.getResources().openRawResource(R.raw.words)));

		try
		{
			SparseArray<Map<String, WordLibElement>> mapLib = new SparseArray<Map<String, WordLibElement>>();

			for (String line = reader.readLine(); line != null; line = reader
					.readLine())
			{
				line = line.trim();
				if (line.length() == 0)
					continue;

				if (null == mapLib.get(line.length()))
					mapLib.put(line.length(),
							new HashMap<String, WordLibElement>());
				Map<String, WordLibElement> map = mapLib.get(line.length());

				for (int i = 0; i < line.length(); ++i)
				{
					String key = line.substring(i, i + 1);
					if (!map.containsKey(key))
					{
						WordLibElement e = new WordLibElement();
						e.key = key;
						map.put(key, e);
					}

					WordLibElement e = map.get(key);
					e.words.add(line);

				}

			}

			for (int i = 0; i < mapLib.size(); ++i)
			{
				WordLibEntry entry = new WordLibEntry(mContext);
				entry.numChars = mapLib.keyAt(i);
				entry.mElements = new Vector<WordLibElement>();
				for (WordLibElement element : mapLib.valueAt(i).values())
				{
					if (element.words.size() > 2)
						entry.mElements.add(element);
				}

				if (entry.mElements.size() > 0)
					mEntries.put(entry.numChars, entry);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public void saveBin()
	{

		try
		{
			DataOutputStream idx;
			idx = new DataOutputStream(mContext.openFileOutput(WORDLIB_INDEX_FILE,
					Context.MODE_PRIVATE));
			idx.write(MAGIC_NUMBER);
			idx.write(CURRENT_VERSION);
			
			for ( int i = 0;  i < mEntries.size(); ++i)
			{
				WordLibEntry entry  = mEntries.get(i);
				if ( entry != null)
				{
					idx.write(entry.numChars);
					idx.write(entry.mElements.size());
					
				}
				
			}

		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public WordLibEntry[] getEntries()
	{
		WordLibEntry[] r = new WordLibEntry[mEntries.size()];
		for (int i = 0; i < mEntries.size(); ++i)
			r[i] = mEntries.valueAt(i);
		return r;
	}

	public WordLibEntry getEntry(int numChars)
	{
		return mEntries.get(numChars);
	}

}
