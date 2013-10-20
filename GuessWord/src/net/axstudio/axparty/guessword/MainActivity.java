package net.axstudio.axparty.guessword;

import net.axstudio.axparty.guessword.R;
import net.axstudio.axparty.guessword.Rule.PlayerType;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity
{

	static final int PLAYER_NUM_EDIT_IDS[] = { R.id.NumMajorityPlayers,
			R.id.NumMinorityPlayers, R.id.NumIdiotPlayers };

	public void startGame()
	{
		// Rule rule = ((Rule) ((Spinner)
		// findViewById(R.id.spinnerTotalPlayers))
		// .getSelectedItem()).clone();

		int[] numPlayers = new int[3];
		for (PlayerType t : PlayerType.values())
		{
			EditText edit = ((EditText) findViewById(PLAYER_NUM_EDIT_IDS[t
					.ordinal()]));
			String s = edit.getText().toString();
			numPlayers[t.ordinal()] = Integer.parseInt(s);

		}
		// Game game = new Game(this);
		// game.init(rule, major, minor, idiot,
		WordLibEntry entry = ((WordLibEntry) ((Spinner) findViewById(R.id.SpinnerNumChars))
				.getSelectedItem());

		// game.start();

		{
			Intent intent = new Intent();
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("game",game);
			// intent.putExtra("bundle", bundle);
			intent.putExtra("numPlayers", numPlayers);
			intent.putExtra("numWordChars", entry.mNumChars);
			intent.setClass(this, GameActivity.class);
			startActivity(intent);
			finish();
		}

	}

	public void updateRuleViews()
	{
		// if (mRule != null)
		{
			// {
			// Spinner spinner = (Spinner)
			// findViewById(R.id.spinnerTotalPlayers);
			// spinner.getSelectedItem();
			// }
			Rule rule = (Rule) ((Spinner) findViewById(R.id.spinnerTotalPlayers))
					.getSelectedItem();

			for (PlayerType t : PlayerType.values())
			{
				EditText edit = (EditText) findViewById(PLAYER_NUM_EDIT_IDS[t
						.ordinal()]);
				edit.setText(Integer.toString(rule.getNumPlayersByType(t)));
				edit.setEnabled(true);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		GuessWordApp app = ((GuessWordApp) getApplication());

		{
			Spinner spinner = (Spinner) findViewById(R.id.spinnerTotalPlayers);

			ArrayAdapter<Rule> rules = new ArrayAdapter<Rule>(this,
					android.R.layout.simple_spinner_item, app.getDefaultRules());
			rules.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(rules);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener()
			{

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id)
				{

					// Rule rule = (Rule) parent.getItemAtPosition(position);
					MainActivity activity = (MainActivity) view.getContext();
					activity.updateRuleViews();

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});
		}
		{

			Spinner spinner = (Spinner) findViewById(R.id.SpinnerNumChars);

			ArrayAdapter<WordLibEntry> rules = new ArrayAdapter<WordLibEntry>(
					this, android.R.layout.simple_spinner_item, app
							.getWordLib().getEntries());
			rules.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(rules);
		}
		{
			Button btn = (Button) findViewById(R.id.StartGame);
			btn.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					MainActivity activity = (MainActivity) v.getContext();
					activity.startGame();
				}
			});
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
