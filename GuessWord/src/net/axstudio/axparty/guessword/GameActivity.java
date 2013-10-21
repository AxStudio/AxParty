package net.axstudio.axparty.guessword;

import java.util.Vector;

import net.axstudio.axparty.guessword.Game.Player;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class GameActivity extends Activity
{

	private Game mGame;
	private final Vector<Button> mPlayerViews = new Vector<Button>();

	static private class OnPlayerButtonClick implements OnClickListener
	{
		private final Player mPlayer;

		static enum State
		{
			NAME, WORD,
		}

		private State mState = State.NAME;

		public OnPlayerButtonClick(Player player)
		{
			mPlayer = player;
		}

		@Override
		public void onClick(View v)
		{

			Button button = (Button) v;
			switch (mState)
			{
			case NAME:
				button.setText(mPlayer.word);
				mState = State.WORD;
				break;
			case WORD:
				button.setText(mPlayer.name);
				mState = State.NAME;
				break;

			default:
				break;
			}

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		mGame = new Game(this);
		Rule rule = new Rule(this, getIntent().getIntArrayExtra("numPlayers"));

		WordLibEntry words = ((GuessWordApp) getApplication()).getWordLib()
				.getEntry(getIntent().getIntExtra("numWordChars", 2));

		mGame.init(rule, words);

		if (null == mGame)
		{
			Intent i = new Intent();
			i.setClass(this, MainActivity.class);
			startActivity(i);
			finish();
		}

		final LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		Player[] players = mGame.getPlayers();
		mPlayerViews.clear();

		for (Player player : mGame.getPlayers())
		{
			Button button = new Button(this);
			button.setOnClickListener(new OnPlayerButtonClick(player));
			button.setText(player.name);
			layout.addView(button);
			mPlayerViews.add(button);
		}
		((ScrollView) findViewById(R.id.scrollView1)).addView(layout);
		// setContentView(layout);

		mGame.start();

	}

	void updateView()
	{

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_BACK:
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(getResources().getDrawable(
					android.R.drawable.ic_dialog_alert));
			builder.setTitle(getString(R.string.exit_alert_title));
			builder.setMessage(getString(R.string.exit_alert_title));
			builder.setNegativeButton(android.R.string.yes,
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							System.exit(0);
						}
					});
			builder.setPositiveButton(android.R.string.no,
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							//finish();
						}
					});
			builder.create().show();

			return true;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
//
//	@Override
//	public void onBackPressed()
//	{
//
//		 super.onBackPressed();
//	}

}
