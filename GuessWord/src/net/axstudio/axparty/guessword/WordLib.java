package net.axstudio.axparty.guessword;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

public class WordLib
{

	static final String MAGIC = "AxApGwWl";
	static final byte[] MAGIC_BYTES = MAGIC.getBytes();
	static final int CURRENT_VERSION = 20131017;
	private static final String LOAD_BIN_PREFIX = "loadBin_v";
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
	private byte[] mWordLibHash;
	private final SparseArray<WordLibEntry> mEntries = new SparseArray<WordLibEntry>();

	public WordLib(Context context)
	{

	}

	private void loadBin(DataInputStream strm) throws IOException
	{
		Log.i(this.getClass().getName(), "loadBin");
		int version = strm.readInt();
		Log.i(this.getClass().getName(), "version=" + version);
		Method method = READERS.get(version);
		Log.i(this.getClass().getName(), "method="
				+ ((method == null) ? "null" : method.getName()));

		byte[] hash = new byte[16];
		strm.readFully(hash);

		if (method == null)
		{
			throw (new IOException(String.format("version[%d] not supperted",
					version)));
		}
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
			e.printStackTrace();
			throw (new IOException("invoke load method failed"));
		}

		mWordLibHash = hash;
	}

	public void load(Context context)
	{
		Log.i(this.getClass().getName(), "load:" + WORDLIB_DATA_FILE);
		try
		{
			DataInputStream strm = new DataInputStream(
					context.openFileInput(WORDLIB_DATA_FILE));
			{
				byte[] magic = new byte[MAGIC_BYTES.length];
				strm.readFully(magic);

				Log.i(this.getClass().getName(), "magic=" + magic);

				if (0 != (new String(magic)).compareTo(new String(MAGIC_BYTES)))
					throw new IOException("invalid magic number");
			}
			{

				loadBin(strm);

			}
			return;

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();

		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		loadFromText(context);
		saveBin(context);

	}

	public void loadFromText(Context context)
	{
		Log.i(this.getClass().getName(), "loadFromText");
		mEntries.clear();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				context.getResources().openRawResource(R.raw.words)));

		try
		{
			MessageDigest md5Gen = MessageDigest.getInstance("MD5");
			SparseArray<Map<String, Vector<String>>> mapLib = new SparseArray<Map<String, Vector<String>>>();

			for (String word = reader.readLine(); word != null; word = reader
					.readLine())
			{
				md5Gen.update(word.getBytes());

				word = word.trim();
				if (word.length() == 0)
					continue;

				if (null == mapLib.get(word.length()))
					mapLib.put(word.length(),
							new HashMap<String, Vector<String>>());
				Map<String, Vector<String>> map = mapLib.get(word.length());

				for (int i = 0; i < word.length(); ++i)
				{
					String key = word.substring(i, i + 1);
					if (!map.containsKey(key))
					{
						map.put(key, new Vector<String>());
					}

					map.get(key).add(word);

				}

			}

			mWordLibHash = md5Gen.digest();

			for (int i = 0; i < mapLib.size(); ++i)
			{
				WordLibEntry entry = new WordLibEntry();
				entry.numChars = mapLib.keyAt(i);

				for (Entry<String, Vector<String>> element : mapLib.valueAt(i)
						.entrySet())
				{
					if (element.getValue().size() > 2)
					{
						WordLibElement e = new WordLibElement();
						e.key = element.getKey();
						e.words = new String[element.getValue().size()];
						element.getValue().toArray(e.words);
						entry.mElements.add(e);
					}
				}

				if (entry.mElements.size() > 0)
					mEntries.put(entry.numChars, entry);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e1)
		{
			e1.printStackTrace();
		}

	}

	public void loadBin_v20131017(DataInputStream strm)
	{
		Log.i(this.getClass().getName(), "loadBin_v20131017");

		try
		{
			final int numEntries = strm.readInt();
			for (int i = 0; i < numEntries; ++i)
			{
				WordLibEntry entry = new WordLibEntry();
				entry.numChars = strm.readInt();
				entry.mElements.setSize(strm.readInt());
				for (int j = 0; j < entry.mElements.size(); ++j)
				{
					WordLibElement element = new WordLibElement();
					element.key = strm.readUTF();
					element.words = new String[strm.readInt()];
					for (int k = 0; k < element.words.length; ++k)
					{
						element.words[k] = strm.readUTF();
					}
					entry.mElements.set(j, element);

				}
				this.mEntries.put(entry.numChars, entry);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	private void _saveBin_Header(DataOutputStream strm) throws IOException
	{
		strm.write(MAGIC_BYTES);
		strm.writeInt(CURRENT_VERSION);
		strm.write(mWordLibHash);

	}

	private void _saveBin_v20131017(DataOutputStream strm) throws IOException
	{
		strm.writeInt(mEntries.size());
		for (int i = 0; i < mEntries.size(); ++i)
		{
			WordLibEntry entry = mEntries.get(i);
			if (entry != null)
			{
				strm.writeInt(entry.numChars);
				strm.writeInt(entry.mElements.size());
				for (WordLibElement element : entry.mElements)
				{
					strm.writeUTF(element.key);
					strm.writeInt(element.words.length);
					for (String w : element.words)
						strm.writeUTF(w);
				}

			}

		}

	}

	public void saveBin(Context context)
	{
		Log.i(this.getClass().getName(), "saveBin");

		try
		{
			DataOutputStream strm = new DataOutputStream(
					context.openFileOutput(WORDLIB_DATA_FILE,
							Context.MODE_PRIVATE));

			_saveBin_Header(strm);

			_saveBin_v20131017(strm);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
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
