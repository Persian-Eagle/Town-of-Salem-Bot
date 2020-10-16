package town.mafia.phases;

import town.DiscordGame;
import town.persons.Person;
import town.phases.Phase;
import town.phases.PhaseManager;
import town.util.RestHelper;

//Daytime is the phase where players can discuss what is happening. There are no features other than
//a voice and text chat that all can use.
public class Day extends Phase
{
	public Day(DiscordGame game, PhaseManager pm)
	{
		super(game, pm);
	}

	@Override
	public void start()
	{
		getGame().sendMessageToTextChannel("daytime_discussion", "Day " + getGame().getDayNum() + " started")
		.flatMap(msg -> getGame().setChannelVisibility("player", "daytime_discussion", true, true))
		.queue();

		getGame().toggleVC("Daytime", true).queue();

		getGame().getPlayers().forEach((person) -> checkVictory(person));
		RestHelper.queueAll(getGame().muteAllInRole("dead", true));
		getPhaseManager().setWarningInSeconds(5);
	}

	public void checkVictory(Person person)
	{
		if (!person.hasWon() && person.canWin())
			person.win();
	}

	@Override
	public Phase getNextPhase()
	{
		if (getGame().getAlivePlayers().size() > 2)
		return new Accusation(getGame(), getPhaseManager(), 3);
		else
		{
			getGame().sendMessageToTextChannel("daytime_discussion", "Accusation was skipped because there were only two people.").queue();
			return new Night(getGame(), getPhaseManager());
		}
	}

	@Override
	public int getDurationInSeconds()
	{
		return 60;
	}
}