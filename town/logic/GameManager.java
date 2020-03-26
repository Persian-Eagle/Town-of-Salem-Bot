package town.logic;

import java.util.ArrayList;
import town.logic.phases.*;
import town.logic.delegates.ActionOne;

public class GameManager
{
	Phase currentPhase;
	GameInterface gameInterface;
	ArrayList<Player> players;

	// actionQueue will be sorted by a custom comperator from lowest to highest
	ArrayList<PlayerAction> actionQueue;
	boolean running;

	GameManager(GameInterface face, ArrayList<Player> players)
	{
		actionQueue = new ArrayList<>();
		currentPhase = new GameStart();

		gameInterface = face;
		this.players = players;
	
		init();
	}

	public void run()
	{
		while (running)
		{
			Event event = null;

			// It's assumed that if event is not assigned by polLEvent, then it should return false
			for (event = gameInterface.pollEvent(); event != null; event = gameInterface.pollEvent())
			{
				switch (event.name)
				{
					case "Exit":
						running = false;
						break;
					case "Player Left":
						break;
					case "Player Action":
						// event.target can be null
						addAction(new PlayerAction(event.actor, event.target));
						break;
					default: break;
				}
			}
			
			if (currentPhase.isOver())
			{
				switch (currentPhase.getName())
				{
					case "Day":
						dispatch(new Event("endDay"));
						break;
					case "Trial":
						dispatch(new Event("endTrial"));
						break;
					case "Night":
						sortActions();
						executeActionQueueBasedOnEvent((action) -> action.actor.visit(action.target));
						dispatch(new Event("endNight"));
						break;
					default: break;
				}
				currentPhase = currentPhase.nextPhase();
				currentPhase.start();
			}
		}
	}

	public void init()
	{
		// Assign players factions
		// Assign players roles using the factions
	}

	void dispatch(Event event)
	{
		for (Player player : players)
			player.onEvent(event);
	}

	void addAction(PlayerAction action)
	{
		// In case there's a duplicate
		if (!action.actor.getRole().canExecute(action.actor, action.target)) return;
		actionQueue.remove(action);
		actionQueue.add(action);
	}

	void executeActionQueueBasedOnEvent(ActionOne<PlayerAction> event)
	{
		for (PlayerAction action : actionQueue)
			event.run(action);
	}

	public void sortActions()
	{
		actionQueue.sort((one, two) -> one.compareTo(two));
	}
}



class PlayerAction
{
	public final Player actor;
	public final Player target;

	PlayerAction(Player a)
	{
		actor = a;
		target = null;
	}

	PlayerAction(Player a, Player t)
	{
		actor = a;
		target = t;
	}

	public boolean equals(PlayerAction action)
	{
		return actor.equals(action.actor);
	}

	public boolean equals(Object other)
	{
		if (other instanceof PlayerAction) return equals((PlayerAction)other);
		return false;
	}

	public int compareTo(Object other)
	{
		if (other instanceof PlayerAction) return compareTo(other);
		return 0;
	}
	
	public int compareTo(PlayerAction other)
	{
		return actor.getRole().getPriority() - other.actor.getRole().getPriority();
	}
}
