package net.axstudio.axparty.guessword;

import java.util.Locale;
import java.util.Vector;

import net.axstudio.axparty.guessword.Rule.PlayerType;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class StartGameActivity extends FragmentActivity
{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	public int mNumPlayers = 7;
	public int mNumWordChars = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_game);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(this,
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_game, menu);
		return true;
	}

	static final String NUM_PLAYERS_KEY = "num_players";
	static final String NUM_WORD_CHARS_KEY = "num_word_chars";

	class RuleAdapter
	{
		Context mContext;
		Rule mRule;

		RuleAdapter(Context context, Rule rule)
		{
			mContext = context;
			mRule = rule;
		}

		public String toString()
		{
			return String.format(Locale.getDefault(),
					mContext.getString(R.string.rule_name),
					mRule.getTotalPlayers(),
					mRule.getNumPlayersByType(PlayerType.MAJOR),
					mRule.getNumPlayersByType(PlayerType.MINOR),
					mRule.getNumPlayersByType(PlayerType.IDIOT));

		}

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{

		DummySectionFragment[] mFragments;

		public SectionsPagerAdapter(Context context, FragmentManager fm)
		{
			super(fm);

			mFragments = new DummySectionFragment[] {
					new DummySectionFragment()
					{
						@Override
						public View createChildView(LayoutInflater inflater,
								ViewGroup container, Bundle savedInstanceState)
						{

							ListView view = new ListView(inflater.getContext());
							GuessWordApp app = (GuessWordApp) inflater
									.getContext().getApplicationContext();

							Vector<RuleAdapter> data = new Vector<RuleAdapter>();
							for (Rule r : app.getDefaultRules())
								data.add(new RuleAdapter(inflater.getContext(),
										r));
							ArrayAdapter<RuleAdapter> rules = new ArrayAdapter<RuleAdapter>(
									inflater.getContext(),
									android.R.layout.simple_list_item_single_choice,
									data);
							view.setAdapter(rules);
							view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
							mNumPlayers = app.getDefaultGameSetting().getInt(
									NUM_PLAYERS_KEY, mNumPlayers);
							for (int i = 0; i < rules.getCount(); ++i)
							{
								if (rules.getItem(i).mRule.getTotalPlayers() == mNumPlayers)
								{
									view.setItemChecked(i, true);
									break;
								}

							}

							view.setOnItemClickListener(new OnItemClickListener()
							{

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id)
								{
									GuessWordApp app = GuessWordApp.getApp(view
											.getContext());

									RuleAdapter rule = (RuleAdapter) parent
											.getItemAtPosition(position);

									mNumPlayers = rule.mRule.getTotalPlayers();
									SharedPreferences.Editor editor = app
											.getDefaultGameSetting().edit();
									editor.putInt(NUM_PLAYERS_KEY, mNumPlayers);
									editor.commit();

								}

							});

							return view;
						}

						public CharSequence getPageTitle()
						{
							return getApplicationContext().getString(
									R.string.title_select_player_num);
						}
					}, new DummySectionFragment()
					{

						@Override
						public View createChildView(LayoutInflater inflater,
								ViewGroup container, Bundle savedInstanceState)
						{
							ListView view = new ListView(inflater.getContext());
							GuessWordApp app = (GuessWordApp) inflater
									.getContext().getApplicationContext();

							Vector<WordLibAdapter> data = new Vector<WordLibAdapter>();
							for (WordLibEntry e : app.getWordLib().getEntries())
							{
								data.add(new WordLibAdapter(inflater
										.getContext(), e));
							}

							ArrayAdapter<WordLibAdapter> adapter = new ArrayAdapter<WordLibAdapter>(
									inflater.getContext(),
									android.R.layout.simple_list_item_single_choice,// android.R.layout.simple_list_item_multiple_choice,
									data);
							view.setAdapter(adapter);
							// view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
							view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

							{
								// final Set<String> wordCharCountSet = app
								// .getDefaultGameSetting().getStringSet(
								// NUM_WORD_CHARS_KEY,
								// new HashSet<String>());
								mNumWordChars = app.getDefaultGameSetting()
										.getInt(NUM_WORD_CHARS_KEY,
												mNumWordChars);
								for (int i = 0; i < adapter.getCount(); ++i)
								{

									if (adapter.getItem(i).mEntry.mNumChars == mNumWordChars)
									{
										view.setItemChecked(i, true);
										break;
									}

								}
							}

							view.setOnItemClickListener(new OnItemClickListener()
							{

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id)
								{
									// if (((ListView) view)
									// .isItemChecked(position))
									{
										GuessWordApp app = GuessWordApp
												.getApp(view.getContext());

										// Set<String> wordCharCountSet =
										// app
										// .getDefaultGameSetting()
										// .getStringSet(
										// NUM_WORD_CHARS_KEY,
										// new HashSet<String>());

										WordLibAdapter lib = (WordLibAdapter) parent
												.getItemAtPosition(position);

										mNumWordChars = lib.mEntry.mNumChars;
										// if (!wordCharCountSet
										// .contains(lib.mEntry.mNumChars))
										{
											// wordCharCountSet.add(Integer
											// .toString(lib.mEntry.mNumChars));
											SharedPreferences.Editor editor = app
													.getDefaultGameSetting()
													.edit();
											editor.putInt(NUM_WORD_CHARS_KEY,
													mNumWordChars);
											editor.commit();
										}
									}

								}

							});

							return view;

						}

						public CharSequence getPageTitle()
						{
							return getApplicationContext().getString(
									R.string.title_select_player_num);
						}
					},

			};

		}

		@Override
		public Fragment getItem(int position)
		{
			return mFragments[position];
		}

		@Override
		public int getCount()
		{
			return mFragments.length;
		}

		@Override
		public int getItemPosition(Object object)
		{
			for (int i = 0; i < mFragments.length; ++i)
				if (mFragments[i] == object)
					return i;
			return POSITION_UNCHANGED;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return mFragments[position].getPageTitle();
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment
	{

		public DummySectionFragment()
		{
			super();
		}

		public CharSequence getPageTitle()
		{
			// if (getPageTitleID() > 0)
			// return getResources().getString(getPageTitleID());
			return this.toString();
			// return mTitle;
		}

		protected View createChildView(LayoutInflater inflater,
				ViewGroup container, Bundle savedInstanceState)
		{
			return null;
		}

		@Override
		final public View onCreateView(LayoutInflater inflater,
				ViewGroup container, Bundle savedInstanceState)
		{
			StartGameActivity activity = (StartGameActivity) getActivity();
			LinearLayout layout = new LinearLayout(activity);
			layout.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT, 0));
			layout.setOrientation(LinearLayout.VERTICAL);

			{
				View view = createChildView(inflater, container,
						savedInstanceState);
				view.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT, 1));
				layout.addView(view);
			}
			{
				Button btn = new Button(layout.getContext());
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT, 0);
				btn.setLayoutParams(params);
				//btn.setGravity(Gravity.CENTER);

				final int position = activity.mSectionsPagerAdapter.getItemPosition(this);
				if ((position == activity.mSectionsPagerAdapter.getCount() - 1))
				{
					btn.setText(R.string.start_game);
					btn.setOnClickListener(new OnClickListener()
					{

						@Override
						public void onClick(View v)
						{
							StartGameActivity activity = (StartGameActivity) v
									.getContext();
							activity.startGame();

						}
					});
				}
				else
				{
					btn.setText(R.string.next_step);
					btn.setOnClickListener(new OnClickListener()
					{

						@Override
						public void onClick(View v)
						{
							StartGameActivity activity = (StartGameActivity) v
									.getContext();
							activity.mViewPager
									.setCurrentItem(activity.mViewPager
											.getCurrentItem() + 1);

						}
					});

				}
				layout.addView(btn);
			}

			return layout;

		}

	}

	public void startGame()
	{
		GuessWordApp app = GuessWordApp.getApp(this);

		int[] numPlayers = { 3, 2, 2 };
		for (Rule r : app.getDefaultRules())
		{
			if (r.getTotalPlayers() == mNumPlayers)
			{
				numPlayers = r.getNumPlayers();
				break;
			}

		}

		{
			Intent intent = new Intent();
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("game",game);
			// intent.putExtra("bundle", bundle);
			intent.putExtra("numPlayers", numPlayers);
			intent.putExtra("numWordChars", mNumWordChars);
			intent.setClass(this, GameActivity.class);
			startActivity(intent);
			finish();
		}

	}
}
