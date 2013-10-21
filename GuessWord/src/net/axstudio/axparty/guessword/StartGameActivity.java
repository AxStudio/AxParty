package net.axstudio.axparty.guessword;

import java.util.Locale;
import java.util.Vector;

import net.axstudio.axparty.guessword.Rule.PlayerType;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

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

			mFragments = new DummySectionFragment[] { new DummySectionFragment()
			{

				@Override
				public View onCreateView(LayoutInflater inflater,
						ViewGroup container, Bundle savedInstanceState)
				{

					ListView view = new ListView(inflater.getContext());
					GuessWordApp app = (GuessWordApp) inflater.getContext()
							.getApplicationContext();

					Vector<RuleAdapter> data = new Vector<RuleAdapter>();
					for (Rule r : app.getDefaultRules())
						data.add(new RuleAdapter(inflater.getContext(), r));
					ArrayAdapter<RuleAdapter> rules = new ArrayAdapter<RuleAdapter>(
							inflater.getContext(),
							android.R.layout.simple_list_item_single_choice,
							data);
					view.setAdapter(rules);
					view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
					final int numPlayers = app.getDefaultGameSetting().getInt(
							NUM_PLAYERS_KEY, -1);
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

							SharedPreferences.Editor editor = app
									.getDefaultGameSetting().edit();
							editor.putInt(NUM_PLAYERS_KEY,
									rule.mRule.getTotalPlayers());
							editor.commit();

						}

					});
					for (int i = 0; i < rules.getCount(); ++i)
					{
						if (rules.getItem(i).mRule.getTotalPlayers() == numPlayers)
						{
							view.setItemChecked(i, true);
							break;
						}

					}

					return view;
				}

				public CharSequence getPageTitle()
				{
					return getApplicationContext().getString(
							R.string.title_select_player_num);
				}
			}

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
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

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

		// @Override
		// public View onCreateView(LayoutInflater inflater, ViewGroup
		// container,
		// Bundle savedInstanceState)
		// {
		// View rootView = inflater.inflate(
		// R.layout.fragment_start_game_dummy, container, false);
		// TextView dummyTextView = (TextView) rootView
		// .findViewById(R.id.section_label);
		// dummyTextView.setText(Integer.toString(getArguments().getInt(
		// ARG_SECTION_NUMBER)));
		// return rootView;
		// }
	}

}
